package com.ossovita.commonservice.core.entities.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeatureSaveFormDto {

    //hotel
    @NotNull
    private long hotelFk;

    //features
    @NotNull
    private String featureName;

    @NotNull
    private String featureDescription;

}