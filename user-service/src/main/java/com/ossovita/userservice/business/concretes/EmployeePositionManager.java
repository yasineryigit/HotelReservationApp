package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.core.entities.EmployeePosition;
import com.ossovita.userservice.business.abstracts.EmployeePositionService;
import com.ossovita.userservice.core.dataAccess.EmployeePositionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class EmployeePositionManager implements EmployeePositionService {

    EmployeePositionRepository employeePositionRepository;

    public EmployeePositionManager(EmployeePositionRepository employeePositionRepository) {
        this.employeePositionRepository = employeePositionRepository;
    }

    @Override
    //@PreAuthorize("hasAuthority('Admin')")
    public EmployeePosition createEmployeePosition(EmployeePosition employeePosition) {
        return employeePositionRepository.save(employeePosition);
    }
}
