package com.ossovita.accountingservice.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CreatePayment {

    private long reservationFk;

    private long customerFk;

}
