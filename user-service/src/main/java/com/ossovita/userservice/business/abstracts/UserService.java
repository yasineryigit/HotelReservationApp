package com.ossovita.userservice.business.abstracts;

import com.ossovita.userservice.core.entities.dtos.CustomerSignUpDto;

public interface UserService {

    CustomerSignUpDto createCustomer(CustomerSignUpDto customerSignUpDto);
}
