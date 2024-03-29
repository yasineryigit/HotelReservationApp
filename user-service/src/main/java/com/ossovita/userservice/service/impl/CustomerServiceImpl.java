package com.ossovita.userservice.service.impl;

import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.stripe.service.StripeUserService;
import com.ossovita.userservice.entity.Customer;
import com.ossovita.userservice.entity.User;
import com.ossovita.userservice.payload.CustomerSignUpDto;
import com.ossovita.userservice.producer.CustomerEventProducer;
import com.ossovita.userservice.repository.CustomerRepository;
import com.ossovita.userservice.repository.UserRepository;
import com.ossovita.userservice.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    StripeUserService stripeUserService;
    CustomerRepository customerRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    CustomerEventProducer customerEventProducer;

    public CustomerServiceImpl(StripeUserService stripeUserService, CustomerRepository customerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, CustomerEventProducer customerEventProducer) {
        this.stripeUserService = stripeUserService;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.customerEventProducer = customerEventProducer;
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

        com.stripe.model.Customer savedStripeCustomer = stripeUserService.createCustomer(savedUser.getUserFirstName(),
                savedUser.getUserLastName(),
                savedUser.getUserEmail()
        );
        customer.setCustomerStripeId(savedStripeCustomer.getId());

        customerRepository.save(customer);

        //send notification
        customerEventProducer.sendCustomerWelcomeNotification(savedUser.getUserEmail(), savedUser.getUserFirstName(), savedUser.getUserLastName());

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

    @Override
    public CustomerDto getCustomerDtoByCustomerPk(long customerPk) {
        Customer customerInDB = getCustomer(customerPk);
        return CustomerDto.builder()
                .customerPk(customerInDB.getCustomerPk())
                .customerEmail(customerInDB.getUser().getUserEmail())
                .customerFirstName(customerInDB.getUser().getUserFirstName())
                .customerLastName(customerInDB.getUser().getUserLastName())
                .customerStripeId(customerInDB.getCustomerStripeId())
                .build();
    }


    public Customer getCustomer(long customerPk) {
        return customerRepository.findById(customerPk).orElseThrow(() -> {
            throw new IdNotFoundException("Customer not found by given id");
        });
    }
}
