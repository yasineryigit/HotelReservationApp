package com.ossovita.userservice.controllers;

import com.ossovita.userservice.business.abstracts.CustomerService;
import com.ossovita.userservice.core.entities.Customer;
import com.ossovita.userservice.core.entities.dtos.CustomerSignUpDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/customer")
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create-customer")
    public CustomerSignUpDto createCustomerWithUser(@Validated @RequestBody CustomerSignUpDto customerSignUpDto) {
        return customerService.createCustomer(customerSignUpDto);
    }

    @GetMapping("/get-all-customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }


}