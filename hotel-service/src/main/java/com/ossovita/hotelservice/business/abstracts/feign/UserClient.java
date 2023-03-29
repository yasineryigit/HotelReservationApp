package com.ossovita.hotelservice.business.abstracts.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service", url = "http://localhost:8888/api/1.0/user")
public interface UserClient {

    @GetMapping("/employees/is-employee-available")
    boolean isEmployeeAvailable(@RequestParam long employeePk);

}
