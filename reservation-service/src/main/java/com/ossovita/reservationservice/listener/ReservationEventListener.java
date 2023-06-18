package com.ossovita.reservationservice.listener;

import com.ossovita.commonservice.enums.PaymentStatus;
import com.ossovita.commonservice.payload.request.CheckRoomAvailabilityRequest;
import com.ossovita.kafka.model.ReservationPaymentResponse;
import com.ossovita.reservationservice.dto.ReservationBookedNotificationForCustomerDto;
import com.ossovita.reservationservice.entity.Reservation;
import com.ossovita.reservationservice.producer.ReservationEventProducer;
import com.ossovita.reservationservice.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservationEventListener {

    ReservationService reservationService;
    ModelMapper modelMapper;
    ReservationEventProducer reservationEventProducer;

    public ReservationEventListener(ReservationService reservationService, ModelMapper modelMapper, ReservationEventProducer reservationEventProducer) {
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
        this.reservationEventProducer = reservationEventProducer;
    }

    @KafkaListener(
            topics = "reservation-payment-response-topic",
            groupId = "foo",
            containerFactory = "reservationPaymentResponseKafkaListenerContainerFactory"//we need to assign containerFactory
    )
    public void listenReservationPaymentResponse(ReservationPaymentResponse reservationPaymentResponse) {
        log.info("Reservation Payment Response Topic - ReservationPaymentResponseModel: {}", reservationPaymentResponse.toString());
        Reservation reservationInDB = reservationService.getReservation(reservationPaymentResponse.getReservationFk());
        CheckRoomAvailabilityRequest checkRoomAvailabilityRequest = CheckRoomAvailabilityRequest.builder()
                .roomFk(reservationInDB.getRoomFk())
                .reservationStartTime(reservationInDB.getReservationStartTime())
                .reservationEndTime(reservationInDB.getReservationEndTime())
                .build();

        if (reservationPaymentResponse.getReservationPaymentStatus().equals(PaymentStatus.PAID)) {
            if (!reservationService.isRoomAvailableByGivenDateRange(checkRoomAvailabilityRequest)) {//is room already booked
                reservationEventProducer.sendReservationPaymentRefundRequestEvent(reservationPaymentResponse, reservationInDB.getCustomerFk());
            } else {
                reservationService.updateReservationAsBooked(reservationInDB);
                //send reservation completed email
                ReservationBookedNotificationForCustomerDto reservationBookedNotificationForCustomerDto = modelMapper.map(reservationPaymentResponse, ReservationBookedNotificationForCustomerDto.class);
                reservationBookedNotificationForCustomerDto.setReservationStartTime(reservationInDB.getReservationStartTime());
                reservationBookedNotificationForCustomerDto.setReservationEndTime(reservationInDB.getReservationEndTime());
                reservationBookedNotificationForCustomerDto.setReservationPrice(reservationInDB.getReservationPrice());
                reservationBookedNotificationForCustomerDto.setReservationPriceCurrency(reservationInDB.getReservationPriceCurrency());
                reservationEventProducer.sendReservationBookedNotificationToTheCustomer(reservationBookedNotificationForCustomerDto);
            }
        } else {
            // TODO: Handle ReservationPaymentStatus.FAILED case
        }
    }
}
