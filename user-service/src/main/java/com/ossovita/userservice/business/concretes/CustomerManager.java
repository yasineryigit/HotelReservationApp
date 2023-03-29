package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.business.abstracts.CustomerService;
import com.ossovita.userservice.core.dataAccess.CustomerRepository;
import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.entities.Customer;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.core.entities.dto.CustomerSignUpDto;
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
                .enabled(true)
                .locked(false)
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
    //@PreAuthorize("hasAuthority('Admin')")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
