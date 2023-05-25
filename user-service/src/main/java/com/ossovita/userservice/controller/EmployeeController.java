package com.ossovita.userservice.controller;

import com.ossovita.userservice.service.EmployeeService;
import com.ossovita.userservice.payload.EmployeeSaveFormDto;
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
