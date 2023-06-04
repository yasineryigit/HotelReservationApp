package com.ossovita.clients.user;

import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.commonservice.dto.SubscriptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service", url = "http://localhost:8888/api/1.0/user")
public interface UserClient {

    @GetMapping("/employees/is-employee-available")
    boolean isEmployeeAvailable(@RequestParam long employeePk);

    @GetMapping("/boss/is-boss-available")
    boolean isBossAvailable(@RequestParam long bossPk);

    @GetMapping("/customers/is-customer-available")
    boolean isCustomerAvailable(@RequestParam long customerPk);


    @GetMapping("/customers/get-customer-dto-by-customer-pk")
    CustomerDto getCustomerDtoByCustomerPk(@RequestParam long customerPk);

    @GetMapping("/subscriptions/get-subscription-dto-by-subscription-fk")
    SubscriptionDto getSubscriptionDtoBySubscriptionFk(@RequestParam long subscriptionFk);
}
