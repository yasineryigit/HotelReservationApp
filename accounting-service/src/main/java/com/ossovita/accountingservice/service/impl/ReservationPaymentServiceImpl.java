package com.ossovita.accountingservice.service.impl;

import com.ossovita.accountingservice.dto.CreatePaymentResponse;
import com.ossovita.accountingservice.entity.ReservationPayment;
import com.ossovita.accountingservice.payload.request.ReservationPaymentRequest;
import com.ossovita.accountingservice.repository.ReservationPaymentRepository;
import com.ossovita.accountingservice.service.ReservationPaymentService;
import com.ossovita.clients.hotel.HotelClient;
import com.ossovita.clients.reservation.ReservationClient;
import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.enums.Currency;
import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.commonservice.enums.ReservationPaymentType;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.RoomNotAvailableException;
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
    KafkaTemplate<String, Object> kafkaTemplate;


    public ReservationPaymentServiceImpl(StripePaymentService stripePaymentService, ReservationClient reservationClient, HotelClient hotelClient, UserClient userClient, ReservationPaymentRepository reservationPaymentRepository, ModelMapper modelMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.stripePaymentService = stripePaymentService;
        this.reservationClient = reservationClient;
        this.hotelClient = hotelClient;
        this.userClient = userClient;
        this.reservationPaymentRepository = reservationPaymentRepository;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    //after payment page return
    @Override
    public CreatePaymentResponse createReservationPaymentIntent(ReservationPaymentRequest reservationPaymentRequest) {

        //getReservationByReservationFk method from reservation-service
        ReservationDto reservationDto = reservationClient.getReservationDtoByReservationFk(reservationPaymentRequest.getReservationFk());

        if (reservationDto == null) {
            throw new IdNotFoundException("Reservation not found by given reservationFk");
        }

        boolean isRoomStatusEqualsAvailable = hotelClient.getRoomDtoWithRoomFk(reservationDto.getRoomFk()).getRoomStatus().equals(RoomStatus.AVAILABLE);//checking for avoid to create duplicate reservation payment

        if (!isRoomStatusEqualsAvailable) {//if roomStatus is not available
            throw new RoomNotAvailableException("Selected room is not available.");
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

        //put customer email as metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("customer_email", customerDto.getCustomerEmail());
        metadata.put("reservation_fk", String.valueOf(reservationDto.getReservationPk()));//reservationFk
        metadata.put("customer_fk", String.valueOf(customerDto.getCustomerPk()));//customerFk
        metadata.put("reservation_payment_fk", String.valueOf(savedReservationPayment.getReservationPaymentPk()));//reservationPaymentFk


        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setCustomer(customerDto.getCustomerStripeId())
                .setCurrency(Currency.USD.toString().toLowerCase(Locale.ENGLISH))
                .setAmount(reservationDto.getReservationPrice().longValue() * 100L)//product cost
                .putAllMetadata(metadata)
                .build();


        //Create a PaymentIntent with params
        PaymentIntent intent;
        try {
            intent = stripePaymentService.createPaymentIntent(createParams);
            return new CreatePaymentResponse(intent.getClientSecret());
        } catch (StripeException e) {//if fails, reflect to the database
            ReservationPayment reservationPaymentInDB = getReservationPayment(savedReservationPayment.getReservationPaymentPk());
            reservationPaymentInDB.setPaymentStatus(PaymentStatus.FAILED);
            reservationPaymentRepository.save(reservationPaymentInDB);
            throw new RuntimeException(e);
        }

    }

    @KafkaListener(
            topics = "reservation-payment-refund-request-topic",
            groupId = "foo",
            containerFactory = "reservationPaymentRefundRequestKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void consumeReservationPaymentRefundRequest(ReservationPaymentRefundRequest reservationPaymentRefundRequest) {
        //retrieve reservationPayment object from the database
        ReservationPayment reservationPaymentInDB = getReservationPayment(reservationPaymentRefundRequest.getReservationPaymentPk());
        //prepare metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("reservation_payment_pk", reservationPaymentRefundRequest.getReservationPaymentPk());
        metadata.put("message", reservationPaymentRefundRequest.getMessage());
        //prepare params
        Map<String, Object> params = new HashMap<>();
        params.put("charge", reservationPaymentInDB.getReservationPaymentStripeChargeId());
        params.put("metadata", metadata);
        params.put("reason", reservationPaymentRefundRequest.getReservationPaymentRefundReason());

        try {
            //refund balance
            stripePaymentService.createPaymentRefund(reservationPaymentInDB.getReservationPaymentStripeChargeId(), params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void processReservationPaymentCharge(Charge charge) {
        Map<String, String> chargeMetadata = charge.getMetadata();
        long customerFk = Long.parseLong(chargeMetadata.get("customer_fk"));
        long reservationFk = Long.parseLong(chargeMetadata.get("reservation_fk"));
        long reservationPaymentFk = Long.parseLong(chargeMetadata.get("reservation_payment_fk"));

        log.info("processReservationPaymentCharge" + "customerFk: " + customerFk + " reservationFk: " + reservationFk + " reservationPaymentFk: " + reservationPaymentFk);

        //update reservationPaymentStripeChargeId in the database
        ReservationPayment reservationPaymentInDB = getReservationPayment(reservationPaymentFk);
        reservationPaymentInDB.setReservationPaymentStripeChargeId(charge.getId());
        reservationPaymentRepository.save(reservationPaymentInDB);

        //update reservation object in the reservation-service with an event
        ReservationPaymentResponse reservationPaymentResponse = ReservationPaymentResponse.builder()
                .reservationPaymentPk(reservationPaymentInDB.getReservationPaymentPk())
                .reservationFk(reservationPaymentInDB.getReservationFk())
                .reservationPaymentAmount(reservationPaymentInDB.getReservationPaymentAmount())
                .reservationPaymentType(ReservationPaymentType.CREDIT_CARD)
                .paymentStatus(PaymentStatus.PAID)
                .build();

        //send kafka message to the reservation-service to update its reservationStatus & reservationIsApproved fields
        kafkaTemplate.send("reservation-payment-response-topic", reservationPaymentResponse);
        log.info("Payment Update Response sent | ReservationPaymentResponse: " + reservationPaymentResponse.toString());


    }


    public ReservationPayment getReservationPayment(long reservationPaymentPk) {
        return reservationPaymentRepository.findById(reservationPaymentPk).orElseThrow(() -> {
            throw new IdNotFoundException("Reservation not found by given id");
        });
    }

}
