package com.ossovita.userservice.controllers;

import com.ossovita.userservice.business.abstracts.EmployeePositionService;
import com.ossovita.userservice.core.entities.EmployeePosition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/employee-position")
public class EmployeePositionController {

    EmployeePositionService employeePositionService;

    public EmployeePositionController(EmployeePositionService employeePositionService) {
        this.employeePositionService = employeePositionService;
    }

    @PostMapping("/create-employee-position")
    public EmployeePosition createEmployeePosition(@RequestBody EmployeePosition employeePosition){
        return employeePositionService.createEmployeePosition(employeePosition);
    }


}
