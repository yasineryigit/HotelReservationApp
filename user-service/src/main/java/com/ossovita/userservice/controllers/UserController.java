package com.ossovita.userservice.controllers;

import com.ossovita.userservice.core.entities.dtos.CustomerSignUpDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class    UserController {

    @GetMapping("/status")
    public String getStatus(){
        return "Working..";
    }

    @GetMapping("/create-customer")
    public CustomerSignUpDto createCustomerWithUser(CustomerSignUpDto customerSignUpDto){
        return null;
    }
}
