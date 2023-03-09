package com.ossovita.userservice.controllers;

import com.ossovita.userservice.business.abstracts.EmployeeService;
import com.ossovita.userservice.core.entities.dtos.EmployeeSaveFormDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {

    EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create-boss")
    public EmployeeSaveFormDto createBoss(@RequestBody EmployeeSaveFormDto employeeSaveFormDto){
        return employeeService.createBoss(employeeSaveFormDto);
    }

    @PostMapping("/create-manager")
    public EmployeeSaveFormDto createManager(@RequestBody EmployeeSaveFormDto employeeSaveFormDto){
        return employeeService.createManager(employeeSaveFormDto);
    }

    @PostMapping("/create-front-desk")
    public EmployeeSaveFormDto createFrontDesk(@RequestBody EmployeeSaveFormDto employeeSaveFormDto){
        return employeeService.createFrontDesk(employeeSaveFormDto);
    }


}
