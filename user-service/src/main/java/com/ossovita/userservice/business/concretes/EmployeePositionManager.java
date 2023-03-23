package com.ossovita.userservice.business.concretes;

import com.ossovita.commonservice.core.entities.EmployeePosition;
import com.ossovita.userservice.business.abstracts.EmployeePositionService;
import com.ossovita.commonservice.core.dataAccess.EmployeePositionRepository;
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
