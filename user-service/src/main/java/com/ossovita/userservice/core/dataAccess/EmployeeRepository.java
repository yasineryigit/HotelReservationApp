package com.ossovita.userservice.core.dataAccess;

import com.ossovita.userservice.core.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
