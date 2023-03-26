package com.ossovita.userservice.business.abstracts;

import com.ossovita.commonservice.core.entities.Customer;
import com.ossovita.userservice.core.entities.dto.CustomerSignUpDto;

import java.util.List;

public interface CustomerService {

    CustomerSignUpDto createCustomer(CustomerSignUpDto customerSignUpDto);

    List<Customer> getAllCustomers();
}
