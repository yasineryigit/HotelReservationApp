package com.ossovita.userservice.controllers;

import com.ossovita.userservice.core.entities.Customer;
import com.ossovita.userservice.core.entities.dto.CustomerSignUpDto;
import com.ossovita.userservice.business.abstracts.CustomerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/user/customers")
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


    @GetMapping("/is-customer-available")
    public boolean isCustomerAvailable(@RequestParam long customerPk){
        return customerService.isCustomerAvailable(customerPk);
    }

}
