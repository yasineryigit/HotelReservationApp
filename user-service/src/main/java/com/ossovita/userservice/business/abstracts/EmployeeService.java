package com.ossovita.userservice.business.abstracts;

import com.ossovita.userservice.core.entities.dtos.EmployeeSaveFormDto;

public interface EmployeeService {

    EmployeeSaveFormDto createManager(EmployeeSaveFormDto employeeSaveFormDto);

    EmployeeSaveFormDto createFrontDesk(EmployeeSaveFormDto employeeSaveFormDto);

    EmployeeSaveFormDto createBoss(EmployeeSaveFormDto employeeSaveFormDto);
}
