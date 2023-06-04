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
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class ReservationPaymentServiceImpl implements ReservationPaymentService {

    ReservationClient reservationClient;
    HotelClient hotelClient;
    UserClient userClient;
    ReservationPaymentRepository reservationPaymentRepository;
    ModelMapper modelMapper;
    KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public ReservationPaymentServiceImpl(ReservationClient reservationClient, HotelClient hotelClient, UserClient userClient, ReservationPaymentRepository reservationPaymentRepository, ModelMapper modelMapper, KafkaTemplate<String, Object> kafkaTemplate) {
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


        //process payment
        Stripe.apiKey = stripeApiKey;
        //fetch stripeCustomerId from user client & use it
        CustomerDto customerDto = userClient.getCustomerDtoByCustomerPk(reservationPaymentRequest.getCustomerFk());

        ReservationPayment reservationPayment = ReservationPayment.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .reservationFk(reservationDto.getReservationPk())
                .reservationPaymentAmount(reservationDto.getReservationPrice())
                .reservationPaymentType(ReservationPaymentType.CREDIT_CARD)
                .build();

        //put customer email as metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("customer_email", customerDto.getCustomerEmail());

        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setCustomer(customerDto.getCustomerStripeId())
                .setCurrency(Currency.USD.toString().toLowerCase(Locale.ENGLISH))
                .setAmount(reservationDto.getReservationPrice().longValue() * 100L)//product cost
                .putAllMetadata(metadata)
                .build();

        //Create a PaymentIntent with the order amount and currency
        PaymentIntent intent = null;
        try {
            intent = PaymentIntent.create(createParams);
            ReservationPayment savedReservationPayment = reservationPaymentRepository.save(reservationPayment);
            return new CreatePaymentResponse(intent.getClientSecret());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        /*
        //TODO: implement result listener for the payment
        //TODO: update reservation object in the database

        ReservationPaymentResponse reservationPaymentResponse = ReservationPaymentResponse.builder()
                .reservationPaymentPk(savedReservationPayment.getReservationPaymentPk())
                .reservationFk(savedReservationPayment.getReservationFk())
                .reservationPaymentAmount(savedReservationPayment.getReservationPaymentAmount())
                .reservationPaymentType(ReservationPaymentType.CREDIT_CARD)
                .paymentStatus(savedReservationPayment.getPaymentStatus())
                .build();

        //TODO: send kafka message to the reservation-service to update its reservationStatus & reservationIsApproved fields
        kafkaTemplate.send("reservation-payment-response-topic", reservationPaymentResponse);
        log.info("Payment Update Response sent | ReservationPaymentResponse: " + reservationPaymentResponse.toString());

        return savedReservationPayment.getPaymentStatus().toString();*/

    }


    @KafkaListener(
            topics = "reservation-payment-refund-request-topic",
            groupId = "foo",
            containerFactory = "reservationPaymentRefundRequestKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void consumeReservationPaymentRefundRequest() {
        //TODO | refund balance
    }

}
