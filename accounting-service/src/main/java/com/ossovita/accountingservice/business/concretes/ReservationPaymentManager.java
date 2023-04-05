package com.ossovita.accountingservice.business.concretes;

import com.ossovita.accountingservice.business.abstracts.ReservationPaymentService;
import com.ossovita.accountingservice.business.abstracts.feign.ReservationClient;
import com.ossovita.accountingservice.core.dataAccess.ReservationPaymentRepository;
import com.ossovita.accountingservice.core.entities.ReservationPayment;
import com.ossovita.commonservice.core.entities.dtos.request.ReservationPaymentRequest;
import com.ossovita.commonservice.core.entities.enums.ReservationPaymentStatus;
import com.ossovita.commonservice.core.utilities.error.exception.IdNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReservationPaymentManager implements ReservationPaymentService {

    ReservationClient reservationClient;
    ReservationPaymentRepository reservationPaymentRepository;
    ModelMapper modelMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationPaymentManager(ReservationClient reservationClient, ReservationPaymentRepository reservationPaymentRepository, ModelMapper modelMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.reservationClient = reservationClient;
        this.reservationPaymentRepository = reservationPaymentRepository;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    //before payment page return
    @Override
    public ReservationPayment createReservationPayment(ReservationPaymentRequest reservationPaymentRequest) throws Exception {
        //check reservationfk w/ feign client
        if (reservationClient.isReservationAvailable(reservationPaymentRequest.getReservationFk())) {
            ReservationPayment reservationPayment = modelMapper.map(reservationPaymentRequest, ReservationPayment.class);
            return reservationPaymentRepository.save(reservationPayment);
        } else {
            throw new Exception("This request contains invalid id");
        }
    }

    //after payment page return
    @Override
    public String updateReservationPayment(ReservationPaymentRequest reservationPaymentRequest) throws Exception {
        if (reservationClient.isReservationAvailable(reservationPaymentRequest.getReservationFk())) {//if reservation available
            ReservationPayment reservationPayment = modelMapper.map(reservationPaymentRequest, ReservationPayment.class);//update
            reservationPayment.setReservationPaymentStatus(ReservationPaymentStatus.PAID);
            reservationPaymentRepository.save(reservationPayment);

            //TODO | implement payment provider
            //TODO | getReservationPrice(reservationFk) from reservation-service
            //TODO | update reservationPayment object
            //TODO | if payment provider returns success, then send it to reservation-service

            //send kafka message to the reservation-service to update its reservationStatus & reservationIsApproved fields
            kafkaTemplate.send("payment-update", reservationPaymentRequest);//todo | create new reservationPaymentUpdateRequestDto in common-service & replace
            log.info("Payment Update Request sent | ReservationPaymentRequest: " + reservationPaymentRequest.toString());

            return "Payment Successful.";
        } else {
            throw new IdNotFoundException("Reservation not found by given reservationFk");
        }
    }


}
