package com.ossovita.userservice.core.dataAccess;

import com.ossovita.userservice.core.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByCustomerPk(long customerPk);
}
