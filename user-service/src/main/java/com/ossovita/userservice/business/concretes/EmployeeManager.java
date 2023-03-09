package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.business.abstracts.EmployeeService;
import com.ossovita.userservice.core.dataAccess.EmployeeRepository;
import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.entities.Employee;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.core.entities.dtos.EmployeeSaveFormDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeManager implements EmployeeService {

    EmployeeRepository employeeRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public EmployeeManager(EmployeeRepository employeeRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EmployeeSaveFormDto createBoss(EmployeeSaveFormDto employeeSaveFormDto) {
        return createEmployeeByPositionFk(employeeSaveFormDto, 1);
    }

    @Override
    public EmployeeSaveFormDto createManager(EmployeeSaveFormDto employeeSaveFormDto) {
        return createEmployeeByPositionFk(employeeSaveFormDto, 2);
    }

    @Override
    public EmployeeSaveFormDto createFrontDesk(EmployeeSaveFormDto employeeSaveFormDto) {
        return createEmployeeByPositionFk(employeeSaveFormDto, 3);
    }

    public EmployeeSaveFormDto createEmployeeByPositionFk(EmployeeSaveFormDto employeeSaveFormDto, long employeePositionFk) {
        //save user
        User user = User.builder()
                .userEmail(employeeSaveFormDto.getUserEmail())
                .userPassword(this.passwordEncoder.encode(employeeSaveFormDto.getUserPassword()))
                .userFirstName(employeeSaveFormDto.getUserFirstName())
                .userLastName(employeeSaveFormDto.getUserLastName())
                .userRoleFk(3)
                //.enabled(false)
                //.locked(false)//TODO Security will be added
                .build();

        User savedUser = userRepository.save(user);

        //save employee
        Employee employee = Employee.builder()
                .userFk(savedUser.getUserPk())
                .employeePositionFk(employeePositionFk)
                .isApproved(true)//TODO if employeePosition=Boss, isApproved should be false initially
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        //TODO
        //save to the hotelemployees table (to the hotelmicroservice via rabbitmq)
        //send employeeSaveFormDto.getHotelFk() and savedEmployee.getEmployeePk()

        return employeeSaveFormDto;
    }
}
