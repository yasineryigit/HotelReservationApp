package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.business.abstracts.EmployeePositionService;
import com.ossovita.userservice.core.dataAccess.EmployeePositionRepository;
import com.ossovita.userservice.core.entities.EmployeePosition;
import org.springframework.stereotype.Service;

@Service
public class EmployeePositionManager implements EmployeePositionService {

    EmployeePositionRepository employeePositionRepository;

    public EmployeePositionManager(EmployeePositionRepository employeePositionRepository) {
        this.employeePositionRepository = employeePositionRepository;
    }

    @Override
    public EmployeePosition createEmployeePosition(EmployeePosition employeePosition) {
        return employeePositionRepository.save(employeePosition);
    }
}
