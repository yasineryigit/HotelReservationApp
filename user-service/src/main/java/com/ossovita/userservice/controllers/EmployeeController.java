package com.ossovita.userservice.controllers;

import com.ossovita.commonservice.core.entities.dtos.BossSignUpDto;
import com.ossovita.commonservice.core.entities.dtos.EmployeeSaveFormDto;
import com.ossovita.userservice.business.abstracts.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/user/employees")
public class EmployeeController {

    EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    //For create Manager and Front Desk employees
    @PostMapping("/create-employee-with-user-with-employeeposition-with-hotelemployees")
    public EmployeeSaveFormDto createEmployee(@Valid @RequestBody EmployeeSaveFormDto employeeSaveFormDto) {
        return employeeService.addEmployeeWithUserWithEmployeePositionWithHotelEmployees(employeeSaveFormDto);
    }

    @PostMapping("/create-boss")
    public BossSignUpDto createBoss(@Valid @RequestBody BossSignUpDto bossSignUpDto) {
        return employeeService.createBoss(bossSignUpDto);
    }


}
