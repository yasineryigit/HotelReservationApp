package com.ossovita.userservice.business.abstracts;

import com.ossovita.commonservice.core.entities.dtos.BossSignUpDto;
import com.ossovita.commonservice.core.entities.dtos.EmployeeSaveFormDto;

public interface EmployeeService {


    EmployeeSaveFormDto addEmployeeWithUserWithEmployeePositionWithHotelEmployees(EmployeeSaveFormDto employeeSaveFormDto);

    BossSignUpDto createBoss(BossSignUpDto bossSignUpDto);
}
