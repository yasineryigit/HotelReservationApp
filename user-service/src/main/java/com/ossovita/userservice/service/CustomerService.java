package com.ossovita.userservice.service;

import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.userservice.entity.Customer;
import com.ossovita.userservice.payload.CustomerSignUpDto;

import java.util.List;

public interface CustomerService {

    CustomerSignUpDto createCustomer(CustomerSignUpDto customerSignUpDto);

    List<Customer> getAllCustomers();

    boolean isCustomerAvailable(long customerPk);

    CustomerDto getCustomerDtoByCustomerPk(long customerPk);
}
