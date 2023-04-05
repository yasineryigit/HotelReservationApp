package com.ossovita.userservice.core.entities.vm;

import lombok.Data;

@Data
public class EmployeeVM {

    private long employeeFk;

    //TODO removable
    private long businessPositionFk;
}