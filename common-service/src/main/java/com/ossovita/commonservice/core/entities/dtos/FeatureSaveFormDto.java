package com.ossovita.commonservice.core.entities.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;

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
