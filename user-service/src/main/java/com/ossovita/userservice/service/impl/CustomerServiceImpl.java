package com.ossovita.userservice.service.impl;

import com.ossovita.userservice.service.CustomerService;
import com.ossovita.userservice.repository.CustomerRepository;
import com.ossovita.userservice.repository.UserRepository;
import com.ossovita.userservice.entity.Customer;
import com.ossovita.userservice.entity.User;
import com.ossovita.userservice.payload.CustomerSignUpDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerSignUpDto createCustomer(CustomerSignUpDto customerSignUpDto) {
        User user = User.builder()
                .userEmail(customerSignUpDto.getUserEmail())
                .userPassword(this.passwordEncoder.encode(customerSignUpDto.getUserPassword()))
                .userRoleFk(2)//TODO MAKE CONSTANT
                .userFirstName(customerSignUpDto.getUserFirstName())
                .userLastName(customerSignUpDto.getUserLastName())
                .enabled(true)
                .locked(false)
                .build();
        User savedUser = userRepository.save(user);


        Customer customer = Customer.builder()
                .userFk(savedUser.getUserPk())
                .build();

        customerRepository.save(customer);

        return customerSignUpDto;
    }

    @Override
    //@PreAuthorize("hasAuthority('Admin')")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public boolean isCustomerAvailable(long customerPk) {
        return customerRepository.existsByCustomerPk(customerPk);
    }
}
