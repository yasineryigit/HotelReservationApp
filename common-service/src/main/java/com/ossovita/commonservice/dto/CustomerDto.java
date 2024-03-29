package com.ossovita.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {

    private long customerPk;

    private String customerFirstName;

    private String customerLastName;

    private String customerEmail;

    private String customerStripeId;


}
