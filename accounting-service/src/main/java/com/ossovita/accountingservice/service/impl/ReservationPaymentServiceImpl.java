package com.ossovita.accountingservice.service.impl;

import com.ossovita.accountingservice.entity.ReservationPayment;
import com.ossovita.accountingservice.feign.HotelClient;
import com.ossovita.accountingservice.feign.ReservationClient;
import com.ossovita.accountingservice.repository.ReservationPaymentRepository;
import com.ossovita.accountingservice.service.ReservationPaymentService;
import com.ossovita.commonservice.dto.ReservationDto;
import com.ossovita.commonservice.enums.ReservationPaymentStatus;
import com.ossovita.commonservice.enums.ReservationPaymentType;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.exception.RoomNotAvailableException;
import com.ossovita.commonservice.payload.request.ReservationCreditCardPaymentRequest;
import com.ossovita.kafka.model.ReservationPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReservationPaymentServiceImpl implements ReservationPaymentService {

    ReservationClient reservationClient;
    HotelClient hotelClient;
    ReservationPaymentRepository reservationPaymentRepository;
    ModelMapper modelMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationPaymentServiceImpl(ReservationClient reservationClient, HotelClient hotelClient, ReservationPaymentRepository reservationPaymentRepository, ModelMapper modelMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.reservationClient = reservationClient;
        this.hotelClient = hotelClient;
        this.reservationPaymentRepository = reservationPaymentRepository;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    //after payment page return
    @Override
    public String updateReservationPayment(ReservationCreditCardPaymentRequest reservationCreditCardPaymentRequest) {
        //getReservationByReservationFk method from reservation-service
        ReservationDto reservationDto = reservationClient.getReservationDtoByReservationFk(reservationCreditCardPaymentRequest.getReservationFk());

        if (reservationDto != null) {
            boolean isRoomStatusEqualsAvailable = hotelClient.getRoomDtoWithRoomFk(reservationDto.getRoomFk()).getRoomStatus().equals(RoomStatus.AVAILABLE);//checking for avoid to create duplicate reservation payment
            if (isRoomStatusEqualsAvailable) {//is roomStatus available
                ReservationPayment reservationPayment = modelMapper.map(reservationCreditCardPaymentRequest, ReservationPayment.class);//update
                //TODO | implement payment provider
                //TODO | update reservationPayment object depends on payment status from the payment sdk (handle PAID & FAILED status)
                reservationPayment.setReservationPaymentStatus(ReservationPaymentStatus.PAID);
                reservationPayment.setReservationFk(reservationDto.getReservationPk());
                reservationPayment.setReservationPaymentAmount(reservationDto.getReservationPrice());
                reservationPayment.setReservationPaymentType(ReservationPaymentType.CREDIT_CARD);
                ReservationPayment savedReservationPayment = reservationPaymentRepository.save(reservationPayment);

                //Send an event to the payment-update topic
                ReservationPaymentResponse reservationPaymentResponse = ReservationPaymentResponse.builder()
                        .reservationPaymentPk(savedReservationPayment.getReservationPaymentPk())
                        .reservationFk(reservationPayment.getReservationFk())
                        .reservationPaymentAmount(reservationPayment.getReservationPaymentAmount())
                        .reservationPaymentType(ReservationPaymentType.CREDIT_CARD)
                        .reservationPaymentStatus(reservationPayment.getReservationPaymentStatus())
                        .build();

                //send kafka message to the reservation-service to update its reservationStatus & reservationIsApproved fields
                kafkaTemplate.send("reservation-payment-update-response-topic", reservationPaymentResponse);
                log.info("Payment Update Response sent | ReservationPaymentResponse: " + reservationPaymentResponse);

                return reservationPayment.getReservationPaymentStatus().toString();
            } else {
                throw new RoomNotAvailableException("Selected room is not available.");
            }
        } else {
            throw new IdNotFoundException("Reservation not found by given reservationFk");
        }

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
