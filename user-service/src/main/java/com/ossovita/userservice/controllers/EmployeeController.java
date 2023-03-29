package com.ossovita.userservice.controllers;

import com.ossovita.userservice.business.abstracts.EmployeeService;
import com.ossovita.userservice.core.entities.dto.EmployeeSaveFormDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/1.0/user/employees")
public class EmployeeController {

    EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping("/create-boss")
    public EmployeeSaveFormDto createBoss(@Validated @RequestBody EmployeeSaveFormDto employeeSaveFormDto) {
        return employeeService.createBoss(employeeSaveFormDto);
    }

    @PostMapping("/create-manager")
    public EmployeeSaveFormDto createManager(@Validated @RequestBody EmployeeSaveFormDto employeeSaveFormDto) {
        return employeeService.createManager(employeeSaveFormDto);
    }

    @PostMapping("/create-front-desk")
    public EmployeeSaveFormDto createFrontDesk(@Validated @RequestBody EmployeeSaveFormDto employeeSaveFormDto) {
        return employeeService.createFrontDesk(employeeSaveFormDto);
    }

    @GetMapping("/is-employee-available")
    public boolean isEmployeeAvailable(@RequestParam long employeePk) {
        return employeeService.isEmployeeAvailable(employeePk);
    }


}
