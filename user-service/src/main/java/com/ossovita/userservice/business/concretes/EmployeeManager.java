package com.ossovita.userservice.business.concretes;

import com.ossovita.commonservice.core.entities.Employee;
import com.ossovita.commonservice.core.entities.HotelEmployees;
import com.ossovita.commonservice.core.entities.User;
import com.ossovita.commonservice.core.entities.dtos.BossSignUpDto;
import com.ossovita.commonservice.core.entities.dtos.EmployeeSaveFormDto;
import com.ossovita.userservice.business.abstracts.EmployeeService;
import com.ossovita.commonservice.core.dataAccess.EmployeeRepository;
import com.ossovita.commonservice.core.dataAccess.HotelEmployeesRepository;
import com.ossovita.commonservice.core.dataAccess.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeManager implements EmployeeService {

    EmployeeRepository employeeRepository;
    UserRepository userRepository;
    HotelEmployeesRepository hotelEmployeesRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;

    public EmployeeManager(EmployeeRepository employeeRepository, UserRepository userRepository, HotelEmployeesRepository hotelEmployeesRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.hotelEmployeesRepository = hotelEmployeesRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public EmployeeSaveFormDto addEmployeeWithUserWithEmployeePositionWithHotelEmployees(EmployeeSaveFormDto employeeSaveFormDto) {
        User user = modelMapper.map(employeeSaveFormDto, User.class);
        User savedUser = userRepository.save(user);

        Employee employee = Employee.builder()
                .userFk(savedUser.getUserPk())
                .employeePositionFk(employeeSaveFormDto.getEmployeePositionFk())//manager or frontdesk (1 or 2)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);


        HotelEmployees hotelEmployees = HotelEmployees.builder()
                .hotelFk(employeeSaveFormDto.getHotelFk())
                .employeeFk(savedEmployee.getEmployeePk()).build();

        hotelEmployeesRepository.save(hotelEmployees);


        return employeeSaveFormDto;
    }

    @Override
    public BossSignUpDto createBoss(BossSignUpDto bossSignUpDto) {
        return null;
        //TODO
    }
}
