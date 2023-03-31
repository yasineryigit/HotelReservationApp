package com.ossovita.commonservice.core.entities.dtos.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FeatureRequest {

    //hotel
    @NotNull
    private long hotelFk;

    //features
    @NotNull
    private String featureName;

    @NotNull
    private String featureDescription;

}