package com.ossovita.userservice.payload.request;

import lombok.Data;

@Data
public class SubscriptionRequest {


    private long bossFk;

    private long subscriptionPlanFk;


}
