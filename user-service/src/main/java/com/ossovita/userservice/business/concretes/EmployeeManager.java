package com.ossovita.userservice.business.concretes;

import com.ossovita.commonservice.core.entities.dtos.request.HotelEmployeeRequest;
import com.ossovita.commonservice.core.utilities.error.exception.IdNotFoundException;
import com.ossovita.userservice.business.abstracts.EmployeeService;
import com.ossovita.userservice.business.abstracts.feign.HotelClient;
import com.ossovita.userservice.core.dataAccess.BossRepository;
import com.ossovita.userservice.core.dataAccess.EmployeeRepository;
import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.entities.Employee;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.core.entities.dto.EmployeeSaveFormDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeManager implements EmployeeService {

    BossRepository bossRepository;
    EmployeeRepository employeeRepository;
    UserRepository userRepository;
    HotelClient hotelClient; //feign client
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;

    public EmployeeManager(BossRepository bossRepository, EmployeeRepository employeeRepository, UserRepository userRepository, HotelClient hotelClient, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
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

        //TODO throw an event | name:createEmployee payload:employeeSaveFormDto | Imlement Message Broker

        createHotelEmployeeInHotelService(employeeSaveFormDto.getHotelFk(), savedEmployee.getEmployeePk());


        return employeeSaveFormDto;

    }


    public void createHotelEmployeeInHotelService(long hotelFk, long employeeFk) {
        //feign client
        HotelEmployeeRequest hotelEmployeeRequest = HotelEmployeeRequest.builder().hotelFk(hotelFk).employeeFk(employeeFk).build();
        hotelClient.createHotelEmployee(hotelEmployeeRequest);
    }


}
