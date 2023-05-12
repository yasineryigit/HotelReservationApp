package com.ossovita.userservice.service;

import com.ossovita.userservice.payload.EmployeeSaveFormDto;

public interface EmployeeService {


    EmployeeSaveFormDto createManager(EmployeeSaveFormDto employeeSaveFormDto);

    EmployeeSaveFormDto createFrontDesk(EmployeeSaveFormDto employeeSaveFormDto);

    boolean isEmployeeAvailable(long employeePk);


}
