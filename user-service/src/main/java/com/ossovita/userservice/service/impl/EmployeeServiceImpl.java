package com.ossovita.userservice.service.impl;

import com.ossovita.clients.hotel.HotelClient;
import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.commonservice.payload.request.HotelEmployeeRequest;
import com.ossovita.userservice.entity.Employee;
import com.ossovita.userservice.entity.User;
import com.ossovita.userservice.payload.EmployeeSaveFormDto;
import com.ossovita.userservice.repository.BossRepository;
import com.ossovita.userservice.repository.EmployeeRepository;
import com.ossovita.userservice.repository.UserRepository;
import com.ossovita.userservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    BossRepository bossRepository;
    EmployeeRepository employeeRepository;
    UserRepository userRepository;
    HotelClient hotelClient;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;

    public EmployeeServiceImpl(BossRepository bossRepository, EmployeeRepository employeeRepository, UserRepository userRepository, HotelClient hotelClient, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.bossRepository = bossRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.hotelClient = hotelClient;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    @Override
    //@PreAuthorize("hasAnyAuthority('Admin', 'Boss')")
    public EmployeeSaveFormDto createManager(EmployeeSaveFormDto employeeSaveFormDto) {
        if ((hotelClient.isHotelAvailable(employeeSaveFormDto.getHotelFk()))) {
            return createEmployeeWithBusinessPositionFk(employeeSaveFormDto, 2);
        } else throw new IdNotFoundException("Hotel not found by the given id");
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('Admin', 'Boss')")
    public EmployeeSaveFormDto createFrontDesk(EmployeeSaveFormDto employeeSaveFormDto) {
        if ((hotelClient.isHotelAvailable(employeeSaveFormDto.getHotelFk()))) {
            return createEmployeeWithBusinessPositionFk(employeeSaveFormDto, 3);
        } else throw new IdNotFoundException("Hotel not found by the given id");
    }

    @Override
    public boolean isEmployeeAvailable(long employeePk) {
        return employeeRepository.existsByEmployeePk(employeePk);
    }


    /*
     * create and save employee & user  & hotel employees objects
     * */
    public EmployeeSaveFormDto createEmployeeWithBusinessPositionFk(EmployeeSaveFormDto employeeSaveFormDto, long businessPositionFk) {

        User user = modelMapper.map(employeeSaveFormDto, User.class);
        user.setUserRoleFk(3);//userRoleFk=3 is Business
        user.setEnabled(true);//TODO should be false after implementing email verification
        user.setLocked(false);
        user.setUserPassword(passwordEncoder.encode(employeeSaveFormDto.getUserPassword()));

        User savedUser = userRepository.save(user);

        Employee employee = Employee.builder()
                .userFk(savedUser.getUserPk())
                .businessPositionFk(businessPositionFk)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        //TODO throw an event | name:createEmployee payload:employeeSaveFormDto | Implement Message Broker

        createHotelEmployeeInHotelService(employeeSaveFormDto.getHotelFk(), savedEmployee.getEmployeePk());


        return employeeSaveFormDto;

    }


    public void createHotelEmployeeInHotelService(long hotelFk, long employeeFk) {
        //feign client
        HotelEmployeeRequest hotelEmployeeRequest = HotelEmployeeRequest.builder().hotelFk(hotelFk).employeeFk(employeeFk).build();
        hotelClient.createHotelEmployee(hotelEmployeeRequest);
    }


}
