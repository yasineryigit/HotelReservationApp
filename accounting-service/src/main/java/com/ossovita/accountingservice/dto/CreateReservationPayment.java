package com.ossovita.accountingservice.dto;

import lombok.Data;

@Data
public class CreateReservationPayment {

    private long reservationFk;

    private long customerFk;

}
