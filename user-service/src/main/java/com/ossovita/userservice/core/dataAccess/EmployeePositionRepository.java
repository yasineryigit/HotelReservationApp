package com.ossovita.userservice.core.dataAccess;

import com.ossovita.userservice.core.entities.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {


}
