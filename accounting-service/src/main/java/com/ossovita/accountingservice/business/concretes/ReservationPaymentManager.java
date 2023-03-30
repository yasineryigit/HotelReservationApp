package com.ossovita.accountingservice.business.concretes;

import com.ossovita.accountingservice.business.abstracts.ReservationPaymentService;
import com.ossovita.accountingservice.business.abstracts.feign.ReservationClient;
import com.ossovita.accountingservice.core.dataAccess.ReservationPaymentRepository;
import com.ossovita.accountingservice.core.entities.ReservationPayment;
import com.ossovita.accountingservice.core.entities.dto.request.ReservationPaymentRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationPaymentManager implements ReservationPaymentService {

    ReservationClient reservationClient;
    ReservationPaymentRepository reservationPaymentRepository;
    ModelMapper modelMapper;

    public ReservationPaymentManager(ReservationClient reservationClient, ReservationPaymentRepository reservationPaymentRepository, ModelMapper modelMapper) {
        this.reservationClient = reservationClient;
        this.reservationPaymentRepository = reservationPaymentRepository;
        this.modelMapper = modelMapper;
    }

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
}
