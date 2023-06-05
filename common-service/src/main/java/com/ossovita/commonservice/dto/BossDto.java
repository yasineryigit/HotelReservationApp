package com.ossovita.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BossDto {

    private long bossPk;

    private String bossEmail;

    private String bossStripeId;
}
