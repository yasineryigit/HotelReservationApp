package com.ossovita.userservice.controllers;

import com.ossovita.userservice.business.abstracts.UserService;
import com.ossovita.userservice.core.entities.dtos.CustomerSignUpDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Working..";
    }

    @PostMapping("/create-customer")
    public CustomerSignUpDto createCustomerWithUser(@RequestBody CustomerSignUpDto customerSignUpDto) {
        return userService.createCustomer(customerSignUpDto);
    }
}
