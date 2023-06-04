package com.ossovita.userservice.service.impl;

import com.ossovita.commonservice.dto.CustomerDto;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.userservice.entity.Customer;
import com.ossovita.userservice.entity.User;
import com.ossovita.userservice.payload.CustomerSignUpDto;
import com.ossovita.userservice.repository.CustomerRepository;
import com.ossovita.userservice.repository.UserRepository;
import com.ossovita.userservice.service.CustomerService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.CustomerCreateParams;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {


    CustomerRepository customerRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
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

        Stripe.apiKey = stripeApiKey;
        // Stripe müşteri nesnesini oluşturmak için parametreleri ayarlayın
        CustomerCreateParams createParams = CustomerCreateParams.builder()
                .setName(savedUser.getUserFirstName() + " " + savedUser.getUserLastName())
                .setEmail(savedUser.getUserEmail())
                .build();

        try {
            // Müşteriyi Stripe API'sinde oluşturun ve kaydedin
            com.stripe.model.Customer savedStripeCustomer = com.stripe.model.Customer.create(createParams);
            customer.setCustomerStripeId(savedStripeCustomer.getId());
            // Müşteri kimliğini saklayabilirsiniz (örneğin veritabanında)
        } catch (StripeException e) {
            // Stripe API'sinden bir hata durumunda hata yönetimini gerçekleştirin
            e.printStackTrace();
        }

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

    @Override
    public CustomerDto getCustomerDtoByCustomerPk(long customerPk) {
        Customer customerInDB = getCustomer(customerPk);
        return CustomerDto.builder()
                .customerEmail(customerInDB.getUser().getUserEmail())
                .customerStripeId(customerInDB.getCustomerStripeId())
                .build();
    }


    public Customer getCustomer(long customerPk) {
        return customerRepository.findById(customerPk).orElseThrow(() -> {
            throw new IdNotFoundException("Customer not found by given id");
        });
    }
}
