package com.ossovita.userservice.controllers;

import com.ossovita.userservice.business.abstracts.EmployeeService;
import com.ossovita.userservice.core.entities.dto.BossSaveFormDto;
import com.ossovita.userservice.core.entities.dto.EmployeeSaveFormDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/user/employees")
public class EmployeeController {

    EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }




    @PostMapping("/create-manager")
    public EmployeeSaveFormDto createManager(@Valid @RequestBody EmployeeSaveFormDto employeeSaveFormDto) {
        return employeeService.createManager(employeeSaveFormDto);
    }

    @PostMapping("/create-front-desk")
    public EmployeeSaveFormDto createFrontDesk(@Valid @RequestBody EmployeeSaveFormDto employeeSaveFormDto) {
        return employeeService.createFrontDesk(employeeSaveFormDto);
    }

    @GetMapping("/is-employee-available")
    public boolean isEmployeeAvailable(@RequestParam long employeePk) {
        return employeeService.isEmployeeAvailable(employeePk);
    }




}
