package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.business.abstracts.UserService;
import com.ossovita.userservice.core.dataAccess.CustomerRepository;
import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.entities.Customer;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.core.entities.dtos.CustomerSignUpDto;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserManager implements UserService {

    CustomerRepository customerRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserManager(CustomerRepository customerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerSignUpDto createCustomer(CustomerSignUpDto customerSignUpDto) {
        User user = User.builder()
                .userEmail(customerSignUpDto.getUserEmail())
                .userPassword(this.passwordEncoder.encode(customerSignUpDto.getUserPassword()))
                .userRoleFk(customerSignUpDto.getUserRoleFk())
                .userFirstName(customerSignUpDto.getUserFirstName())
                .userLastName(customerSignUpDto.getUserLastName())
                //.enabled(false)
                //.locked(false)//TODO Security will be added
                .build();
        User savedUser = userRepository.save(user);


        Customer customer = Customer.builder()
                .userFk(savedUser.getUserPk())
                .build();

        customerRepository.save(customer);
        customerSignUpDto.setCustomerPk(customer.getCustomerPk());


        return customerSignUpDto;
    }
}
