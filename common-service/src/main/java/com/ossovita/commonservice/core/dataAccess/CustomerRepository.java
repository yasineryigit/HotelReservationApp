package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
