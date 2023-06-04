package com.ossovita.userservice.repository;

import com.ossovita.userservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByCustomerPk(long customerPk);


}
