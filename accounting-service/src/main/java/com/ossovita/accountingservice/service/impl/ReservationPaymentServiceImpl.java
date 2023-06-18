package com.ossovita.accountingservice.service.impl;

import com.ossovita.accountingservice.entity.ReservationPayment;
import com.ossovita.accountingservice.enums.PaymentType;
import com.ossovita.accountingservice.payload.request.ReservationPaymentRequest;
import com.ossovita.accountingservice.payload.response.CreatePaymentResponse;
import com.ossovita.accountingservice.producer.ReservationPaymentEventProducer;
import com.ossovita.accountingservice.repository.ReservationPaymentRepository;
import com.ossovita.accountingservice.service.ReservationPaymentService;
import com.ossovita.clients.hotel.HotelClient;
import com.ossovita.clients.reservation.ReservationClient;
import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.dto.RoomDto;
import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.commonservice.enums.ReservationPaymentType;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.UnexpectedRequestException;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import com.ossovita.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.kafka.model.ReservationPaymentResponse;
import com.ossovita.stripe.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class ReservationPaymentServiceImpl implements ReservationPaymentService {

    StripePaymentService stripePaymentService;
    ReservationClient reservationClient;
    HotelClient hotelClient;
    UserClient userClient;
    ReservationPaymentRepository reservationPaymentRepository;
    ModelMapper modelMapper;
    ReservationPaymentEventProducer reservationPaymentEventProducer;

    public ReservationPaymentServiceImpl(StripePaymentService stripePaymentService, ReservationClient reservationClient, HotelClient hotelClient, UserClient userClient, ReservationPaymentRepository reservationPaymentRepository, ModelMapper modelMapper, ReservationPaymentEventProducer reservationPaymentEventProducer) {
        this.stripePaymentService = stripePaymentService;
        this.reservationClient = reservationClient;
        this.hotelClient = hotelClient;
        this.userClient = userClient;
        this.reservationPaymentRepository = reservationPaymentRepository;
        this.modelMapper = modelMapper;
        this.reservationPaymentEventProducer = reservationPaymentEventProducer;
    }

    //after payment page return
    @Override
    public CreatePaymentResponse createReservationPaymentIntent(ReservationPaymentRequest reservationPaymentRequest) {

        //getReservationByReservationFk method from reservation-service
        ReservationDto reservationDto = reservationClient.getReservationDtoByReservationFk(reservationPaymentRequest.getReservationFk());

        RoomDto roomDto = hotelClient.getRoomDtoByRoomPk(reservationDto.getRoomFk());

        CheckRoomAvailabilityRequest checkRoomAvailabilityRequest = CheckRoomAvailabilityRequest
                .builder()
                .roomFk(reservationPaymentRequest.getRoomFk())
                .reservationStartTime(reservationPaymentRequest.getReservationStartTime())
                .reservationEndTime(reservationPaymentRequest.getReservationEndTime())
                .build();

        if (!reservationClient.isRoomAvailableByGivenDateRange(checkRoomAvailabilityRequest)) {//if roomStatus is not available
            throw new UnexpectedRequestException("Selected room is not available.");
        }

        //fetch stripeCustomerId from user client & use it
        CustomerDto customerDto = userClient.getCustomerDtoByCustomerPk(reservationPaymentRequest.getCustomerFk());

        ReservationPayment reservationPayment = ReservationPayment.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .reservationFk(reservationDto.getReservationPk())
                .reservationPaymentAmount(reservationDto.getReservationPrice())
                .reservationPaymentType(ReservationPaymentType.CREDIT_CARD)
                .build();
        ReservationPayment savedReservationPayment = reservationPaymentRepository.save(reservationPayment);

        //put extra information to the metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("payment_type", String.valueOf(PaymentType.RESERVATION_PAYMENT));
        metadata.put("customer_email", customerDto.getCustomerEmail());
        metadata.put("customer_first_name", customerDto.getCustomerFirstName());
        metadata.put("customer_last_name", customerDto.getCustomerLastName());
        metadata.put("reservation_fk", String.valueOf(reservationDto.getReservationPk()));//reservationFk
        metadata.put("customer_fk", String.valueOf(customerDto.getCustomerPk()));//customerFk
        metadata.put("reservation_payment_fk", String.valueOf(savedReservationPayment.getReservationPaymentPk()));//reservationPaymentFk
        metadata.put("hotel_name",roomDto.getHotelName());
        metadata.put("room_number", String.valueOf(roomDto.getRoomNumber()));

        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setCustomer(customerDto.getCustomerStripeId())
                .setCurrency(reservationDto.getReservationPriceCurrency().toString().toLowerCase(Locale.ENGLISH))
                .setAmount(reservationDto.getReservationPrice().longValue() * 100L)//product cost
                .putAllMetadata(metadata)
                .build();


        //Create a PaymentIntent with params
        PaymentIntent intent;
        try {
            intent = stripePaymentService.createPaymentIntent(createParams);
            return new CreatePaymentResponse(intent.getClientSecret());
        } catch (StripeException e) {//if fails, reflect to the database
            ReservationPayment reservationPaymentInDB = getReservationPayment(savedReservationPayment.getReservationPaymentPk());//TODO: optimize
            reservationPaymentInDB.setPaymentStatus(PaymentStatus.FAILED);
            reservationPaymentRepository.save(reservationPaymentInDB);
            throw new RuntimeException(e);
        }

    }


    @Override
    public void processReservationPaymentCharge(Charge charge) {
        Map<String, String> chargeMetadata = charge.getMetadata();
        long customerFk = Long.parseLong(chargeMetadata.get("customer_fk"));
        long reservationFk = Long.parseLong(chargeMetadata.get("reservation_fk"));
        long reservationPaymentFk = Long.parseLong(chargeMetadata.get("reservation_payment_fk"));
        String customerEmail = chargeMetadata.get("customer_email");
        String customerFirstName = chargeMetadata.get("customer_first_name");
        String customerLastName = chargeMetadata.get("customer_last_name");
        String hotelName = chargeMetadata.get("hotel_name");
        int roomNumber = Integer.parseInt(chargeMetadata.get("room_number"));


        log.info("processReservationPaymentCharge" + "customerFk: " + customerFk + " reservationFk: " + reservationFk + " reservationPaymentFk: " + reservationPaymentFk);

        //update reservationPaymentStripeChargeId in the database
        ReservationPayment reservationPaymentInDB = getReservationPayment(reservationPaymentFk);
        reservationPaymentInDB.setReservationPaymentStripeChargeId(charge.getId());
        reservationPaymentInDB.setPaymentStatus(PaymentStatus.PAID);
        reservationPaymentRepository.save(reservationPaymentInDB);

        //update reservation object in the reservation-service with an event
        ReservationPaymentResponse reservationPaymentResponse = ReservationPaymentResponse.builder()
                .customerEmail(customerEmail)
                .customerFirstName(customerFirstName)
                .customerLastName(customerLastName)
                .reservationPaymentPk(reservationPaymentInDB.getReservationPaymentPk())
                .reservationFk(reservationPaymentInDB.getReservationFk())
                .reservationPaymentAmount(reservationPaymentInDB.getReservationPaymentAmount())
                .reservationPaymentType(ReservationPaymentType.CREDIT_CARD)
                .reservationPaymentStatus(PaymentStatus.PAID)
                .hotelName(hotelName)
                .roomNumber(roomNumber)
                .build();

        reservationPaymentEventProducer.sendReservationPaymentEvent(reservationPaymentResponse);
        log.info("Payment Update Response sent | ReservationPaymentResponse: " + reservationPaymentResponse.toString());
    }

    @Override
    public ReservationPayment getReservationPayment(long reservationPaymentPk) {
        return reservationPaymentRepository.findById(reservationPaymentPk).orElseThrow(() -> {
            throw new IdNotFoundException("Reservation not found by given id");
        });
    }

}
