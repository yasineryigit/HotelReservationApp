package com.ossovita.accountingservice.payload.request;

import lombok.Data;

@Data
public class ReservationPaymentRequest {


    private long reservationFk;

    private long customerFk;


}
