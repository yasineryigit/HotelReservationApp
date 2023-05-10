package com.ossovita.accountingservice.business.concretes;

import com.ossovita.accountingservice.business.abstracts.ReservationPaymentService;
import com.ossovita.accountingservice.business.abstracts.feign.ReservationClient;
import com.ossovita.accountingservice.core.dataAccess.ReservationPaymentRepository;
import com.ossovita.accountingservice.core.entities.ReservationPayment;
import com.ossovita.commonservice.core.enums.ReservationPaymentType;
import com.ossovita.commonservice.core.dto.ReservationDto;
import com.ossovita.commonservice.core.enums.ReservationPaymentStatus;
import com.ossovita.commonservice.core.kafka.model.ReservationPaymentResponse;
import com.ossovita.commonservice.core.payload.request.ReservationCreditCardPaymentRequest;
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


    //after payment page return
    @Override
    public String updateReservationPayment(ReservationCreditCardPaymentRequest reservationCreditCardPaymentRequest) {
        //getReservationByReservationFk method from reservation-service
        ReservationDto reservationDto = reservationClient.getReservationDtoByReservationFk(reservationCreditCardPaymentRequest.getReservationFk());
        if(reservationDto!=null){

            ReservationPayment reservationPayment = modelMapper.map(reservationCreditCardPaymentRequest, ReservationPayment.class);//update
            //TODO | implement payment provider
            //TODO | update reservationPayment object depends on payment status from the payment sdk (handle PAID & FAILED status)
            reservationPayment.setReservationPaymentStatus(ReservationPaymentStatus.PAID);
            reservationPayment.setReservationPaymentAmount(reservationDto.getReservationPrice());
            ReservationPayment savedReservationPayment = reservationPaymentRepository.save(reservationPayment);

            //Send an event to the payment-update topic
            ReservationPaymentResponse reservationPaymentResponse = ReservationPaymentResponse.builder()
                    .reservationFk(reservationPayment.getReservationFk())
                    .reservationPaymentPk(savedReservationPayment.getReservationPaymentPk())
                    .reservationPaymentAmount(reservationPayment.getReservationPaymentAmount())
                    .reservationPaymentType(ReservationPaymentType.CREDIT_CARD)
                    .reservationPaymentStatus(reservationPayment.getReservationPaymentStatus())
                    .build();

            //send kafka message to the reservation-service to update its reservationStatus & reservationIsApproved fields
            kafkaTemplate.send("reservation-payment-update", reservationPaymentResponse);
            log.info("Payment Update Request sent | ReservationPaymentRequest: " + reservationCreditCardPaymentRequest);

            return reservationPayment.getReservationPaymentStatus().toString();

        }else{
            throw new IdNotFoundException("Reservation not found by given reservationFk");
        }

    }


}
