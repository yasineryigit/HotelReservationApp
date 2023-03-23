package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
