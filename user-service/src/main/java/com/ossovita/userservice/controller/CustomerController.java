package com.ossovita.userservice.controller;

import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.userservice.entity.Customer;
import com.ossovita.userservice.payload.CustomerSignUpDto;
import com.ossovita.userservice.service.CustomerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-customer-dto-by-customer-pk")
    public CustomerDto getCustomerDtoByCustomerPk(@RequestParam long customerPk){
        return customerService.getCustomerDtoByCustomerPk(customerPk);
    }

}
