package com.ossovita.userservice.business.concretes;

import com.ossovita.commonservice.core.entities.Customer;
import com.ossovita.commonservice.core.entities.User;
import com.ossovita.commonservice.core.entities.dtos.CustomerSignUpDto;
import com.ossovita.userservice.business.abstracts.CustomerService;
import com.ossovita.commonservice.core.dataAccess.CustomerRepository;
import com.ossovita.commonservice.core.dataAccess.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerManager implements CustomerService {

    CustomerRepository customerRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public CustomerManager(CustomerRepository customerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
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
                //.enabled(false)
                //.locked(false)//TODO Security will be added
                .build();
        User savedUser = userRepository.save(user);


        Customer customer = Customer.builder()
                .userFk(savedUser.getUserPk())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        customerSignUpDto.setCustomerPk(savedCustomer.getCustomerPk());
        return customerSignUpDto;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
