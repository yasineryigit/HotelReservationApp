package com.ossovita.userservice.controllers;

import com.ossovita.commonservice.core.entities.EmployeePosition;
import com.ossovita.userservice.business.abstracts.EmployeePositionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/user/employee-positions")
public class EmployeePositionController {

    EmployeePositionService employeePositionService;

    public EmployeePositionController(EmployeePositionService employeePositionService) {
        this.employeePositionService = employeePositionService;
    }

    @PostMapping("/create-employee-position")
    public EmployeePosition createEmployeePosition(@RequestBody EmployeePosition employeePosition) {
        return employeePositionService.createEmployeePosition(employeePosition);
    }


}
