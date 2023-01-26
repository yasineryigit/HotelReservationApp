package com.ossovita.accountingservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounting")
public class AccountingController {

    @GetMapping("/status")
    public String getStatus() {
        return "Working..";
    }
    
}
