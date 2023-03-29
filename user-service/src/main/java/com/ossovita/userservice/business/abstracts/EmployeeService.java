package com.ossovita.userservice.business.abstracts;

import com.ossovita.userservice.core.entities.dto.EmployeeSaveFormDto;

public interface EmployeeService {

    EmployeeSaveFormDto createBoss(EmployeeSaveFormDto employeeSaveFormDto);

    EmployeeSaveFormDto createManager(EmployeeSaveFormDto employeeSaveFormDto);

    EmployeeSaveFormDto createFrontDesk(EmployeeSaveFormDto employeeSaveFormDto);

    boolean isEmployeeAvailable(long employeePk);
}
