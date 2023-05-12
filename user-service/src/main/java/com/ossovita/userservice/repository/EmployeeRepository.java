package com.ossovita.userservice.repository;

import com.ossovita.userservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmployeePk(long employeePk);
}
