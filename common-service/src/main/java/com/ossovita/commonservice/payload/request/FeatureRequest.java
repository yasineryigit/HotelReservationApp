package com.ossovita.commonservice.payload.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class FeatureRequest {//TODO | implement the feature part

    //hotel
    @NotNull
    private long hotelFk;

    //features
    @NotNull
    private String featureName;

    @NotNull
    private String featureDescription;

}
