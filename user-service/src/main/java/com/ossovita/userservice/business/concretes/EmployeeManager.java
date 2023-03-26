package com.ossovita.userservice.business.concretes;

import com.ossovita.commonservice.core.dataAccess.EmployeeRepository;
import com.ossovita.commonservice.core.dataAccess.HotelEmployeesRepository;
import com.ossovita.commonservice.core.dataAccess.UserRepository;
import com.ossovita.commonservice.core.entities.Employee;
import com.ossovita.commonservice.core.entities.HotelEmployees;
import com.ossovita.commonservice.core.entities.User;
import com.ossovita.userservice.business.abstracts.EmployeeService;
import com.ossovita.userservice.core.entities.dto.EmployeeSaveFormDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public EmployeeSaveFormDto createBoss(EmployeeSaveFormDto employeeSaveFormDto) {
        return createEmployeeWithEmployeePositionFk(employeeSaveFormDto, 1);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('Admin', 'Boss')")
    public EmployeeSaveFormDto createManager(EmployeeSaveFormDto employeeSaveFormDto) {
        return createEmployeeWithEmployeePositionFk(employeeSaveFormDto, 2);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('Admin', 'Boss')")
    public EmployeeSaveFormDto createFrontDesk(EmployeeSaveFormDto employeeSaveFormDto) {
        return createEmployeeWithEmployeePositionFk(employeeSaveFormDto, 3);
    }

    /*
     * create and save employee & user  & hotel employees objects
     * */
    public EmployeeSaveFormDto createEmployeeWithEmployeePositionFk(EmployeeSaveFormDto employeeSaveFormDto, long employeePositionFk) {
        User user = modelMapper.map(employeeSaveFormDto, User.class);
        user.setUserRoleFk(3);//userRoleFk=3 is Business
        user.setEnabled(true);//TODO should be false after implementing email verification
        user.setLocked(false);
        user.setUserPassword(passwordEncoder.encode(employeeSaveFormDto.getUserPassword()));

        User savedUser = userRepository.save(user);

        Employee employee = Employee.builder()
                .userFk(savedUser.getUserPk())
                .employeePositionFk(employeePositionFk)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        if (employeeSaveFormDto.getHotelFk() != 0) {//if hotelFk available, save it with relation
            HotelEmployees hotelEmployees = HotelEmployees.builder()
                    .hotelFk(employeeSaveFormDto.getHotelFk())
                    .employeeFk(savedEmployee.getEmployeePk()).build();

            hotelEmployeesRepository.save(hotelEmployees);
        }


        return employeeSaveFormDto;

    }


}
