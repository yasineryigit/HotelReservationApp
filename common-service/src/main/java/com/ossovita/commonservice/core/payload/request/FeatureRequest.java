package com.ossovita.commonservice.core.payload.request;

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
