package com.ossovita.reservationservice.producer;

import com.ossovita.clients.user.UserClient;
import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.commonservice.enums.NotificationType;
import com.ossovita.commonservice.enums.ReservationPaymentRefundReason;
import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.kafka.model.NotificationRequest;
import com.ossovita.kafka.model.ReservationPaymentRefundRequest;
import com.ossovita.kafka.model.ReservationPaymentResponse;
import com.ossovita.kafka.model.RoomStatusUpdateRequest;
import com.ossovita.reservationservice.dto.CheckingNotificationForCustomerDto;
import com.ossovita.reservationservice.dto.ReservationBookedNotificationForCustomerDto;
import com.ossovita.reservationservice.dto.ReservationPaymentRefundNotificationForCustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class ReservationEventProducer {

    UserClient userClient;
    KafkaTemplate<String, Object> kafkaTemplate;
    ModelMapper modelMapper;

    public ReservationEventProducer(UserClient userClient, KafkaTemplate<String, Object> kafkaTemplate, ModelMapper modelMapper) {
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
    }

    //room status update event
    public void sendRoomStatusUpdateEvent(long roomFk, RoomStatus roomStatus) {
        RoomStatusUpdateRequest roomStatusUpdateRequest = RoomStatusUpdateRequest.builder()
                .roomFk(roomFk)
                .roomStatus(roomStatus)
                .build();

        kafkaTemplate.send("room-status-update-topic", roomStatusUpdateRequest);
    }

    //reservation payment refund request event
    public void sendReservationPaymentRefundRequestEvent(long reservationPaymentPk, long customerFk) {
        ReservationPaymentRefundRequest reservationPaymentRefundRequest = ReservationPaymentRefundRequest.builder()
                .reservationPaymentPk(reservationPaymentPk)
                .reservationPaymentRefundReason(ReservationPaymentRefundReason.DUPLICATE_RESERVATION)
                .reservationPaymentRefundMessage("Your balance has been refunded because the room you have booked was previously reserved by another user due to a system error.")
                .build();
        log.info("sendReservationPaymentRefundRequest {} : " + reservationPaymentRefundRequest.toString());
        ReservationPaymentRefundNotificationForCustomerDto dto = modelMapper.map(reservationPaymentRefundRequest, ReservationPaymentRefundNotificationForCustomerDto.class);
        dto.setCustomerFk(customerFk);
        sendReservationPaymentRefundNotificationToTheCustomer(dto);
        kafkaTemplate.send("reservation-payment-refund-request-topic", reservationPaymentRefundRequest);
    }

    //notifications

    public void sendReservationPaymentRefundNotificationToTheCustomer(ReservationPaymentRefundNotificationForCustomerDto dto) {
        //fetch customer information
        CustomerDto customerDto = userClient.getCustomerDtoByCustomerPk(dto.getCustomerFk());
        HashMap<String, String> payload = new HashMap<>();

        payload.put("customer_email", customerDto.getCustomerEmail());
        payload.put("customer_first_name", customerDto.getCustomerFirstName());
        payload.put("customer_last_name", customerDto.getCustomerLastName());
        payload.put("reservation_payment_refund_reason", dto.getReservationPaymentRefundReason().toString());
        payload.put("reservation_payment_refund_message", dto.getReservationPaymentRefundMessage());
        payload.put("reservation_payment_amount", dto.getReservationPaymentAmount().toString());

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to(dto.getCustomerEmail())
                .payload(payload)
                .notificationType(NotificationType.RESERVATION_PAYMENT_REFUND_NOTIFICATION)
                .build();
        kafkaTemplate.send("notification-request-topic", notificationRequest);
        log.info("Reservation payment refund notification has been sent to the customer {}: " + notificationRequest.toString());
    }

    public void sendReservationBookedNotificationToTheCustomer(ReservationBookedNotificationForCustomerDto reservationBookedNotificationForCustomerDto) {

        HashMap<String, String> payload = new HashMap<>();

        payload.put("customer_email", reservationBookedNotificationForCustomerDto.getCustomerEmail());
        payload.put("customer_first_name", reservationBookedNotificationForCustomerDto.getCustomerFirstName());
        payload.put("customer_last_name", reservationBookedNotificationForCustomerDto.getCustomerLastName());
        payload.put("reservation_start_time", String.valueOf(reservationBookedNotificationForCustomerDto.getReservationStartTime()));
        payload.put("reservation_end_time", String.valueOf(reservationBookedNotificationForCustomerDto.getReservationEndTime()));
        payload.put("reservation_price", String.valueOf(reservationBookedNotificationForCustomerDto.getReservationPrice()));
        payload.put("reservation_price_currency", String.valueOf(reservationBookedNotificationForCustomerDto.getReservationPriceCurrency()));
        payload.put("hotel_name", reservationBookedNotificationForCustomerDto.getHotelName());
        payload.put("room_number", String.valueOf(reservationBookedNotificationForCustomerDto.getRoomNumber()));

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to(reservationBookedNotificationForCustomerDto.getCustomerEmail())
                .payload(payload)
                .notificationType(NotificationType.RESERVATION_BOOKED_NOTIFICATION)
                .build();
        kafkaTemplate.send("notification-request-topic", notificationRequest);
        log.info("Reservation booked notification has been sent to the customer {}: " + notificationRequest.toString());
    }

    public void sendCheckInNotificationToTheCustomer(CheckingNotificationForCustomerDto dto) {
        HashMap<String, String> payload = getPayloadOfTheCheckingNotificationForCustomer(dto);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to(dto.getCustomerEmail())
                .notificationType(NotificationType.CHECK_IN_NOTIFICATION)
                .payload(payload)
                .build();

        kafkaTemplate.send("notification-request-topic", notificationRequest);
        log.info("CheckIn notification has been sent to the customer {}: " + notificationRequest.toString());
    }

    public void sendCheckOutNotificationToTheCustomer(CheckingNotificationForCustomerDto dto) {
        HashMap<String, String> payload = getPayloadOfTheCheckingNotificationForCustomer(dto);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .to(dto.getCustomerEmail())
                .notificationType(NotificationType.CHECK_OUT_NOTIFICATION)
                .payload(payload)
                .build();

        kafkaTemplate.send("notification-request-topic", notificationRequest);
        log.info("CheckOut notification has been sent to the customer {}: " + notificationRequest.toString());
    }

    private HashMap<String, String> getPayloadOfTheCheckingNotificationForCustomer(CheckingNotificationForCustomerDto dto) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("customer_email", dto.getCustomerEmail());
        payload.put("customer_first_name", dto.getCustomerFirstName());
        payload.put("customer_last_name", dto.getCustomerLastName());
        payload.put("reservation_start_time", String.valueOf(dto.getReservationStartTime()));
        payload.put("reservation_end_time", String.valueOf(dto.getReservationEndTime()));
        payload.put("hotel_name", String.valueOf(dto.getHotelName()));
        payload.put("room_number", String.valueOf(dto.getRoomNumber()));
        return payload;
    }


}
