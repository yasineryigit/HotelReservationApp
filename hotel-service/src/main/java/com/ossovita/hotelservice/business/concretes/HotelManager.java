package com.ossovita.hotelservice.business.concretes;

import com.ossovita.commonservice.core.entities.dtos.request.HotelEmployeeRequest;
import com.ossovita.commonservice.core.entities.dtos.request.HotelRequest;
import com.ossovita.commonservice.core.entities.dtos.response.HotelEmployeeResponse;
import com.ossovita.commonservice.core.utilities.error.EmployeeNotFoundException;
import com.ossovita.commonservice.core.utilities.error.HotelNotFoundException;
import com.ossovita.hotelservice.business.abstracts.HotelService;
import com.ossovita.hotelservice.business.abstracts.feign.UserClient;
import com.ossovita.hotelservice.core.dataAccess.AddressRepository;
import com.ossovita.hotelservice.core.dataAccess.HotelEmployeeRepository;
import com.ossovita.hotelservice.core.dataAccess.HotelRepository;
import com.ossovita.hotelservice.core.entities.Address;
import com.ossovita.hotelservice.core.entities.Hotel;
import com.ossovita.hotelservice.core.entities.HotelEmployee;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HotelManager implements HotelService {

    HotelRepository hotelRepository;
    AddressRepository addressRepository;
    HotelEmployeeRepository hotelEmployeeRepository;
    UserClient userClient;
    ModelMapper modelMapper;

    public HotelManager(HotelRepository hotelRepository, AddressRepository addressRepository, HotelEmployeeRepository hotelEmployeeRepository, UserClient userClient, ModelMapper modelMapper) {
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
        this.hotelEmployeeRepository = hotelEmployeeRepository;
        this.userClient = userClient;
        this.modelMapper = modelMapper;
    }

    @Override
    public Hotel createHotel(HotelRequest hotelRequest) {
        //we need to check employeeFk is real or not
        if (userClient.isEmployeeAvailable(hotelRequest.getEmployeeFk())) {
            Address address = modelMapper.map(hotelRequest, Address.class);
            Address savedAddress = addressRepository.save(address);

            Hotel hotel = modelMapper.map(hotelRequest, Hotel.class);
            hotel.setAddressFk(savedAddress.getAddressPk());
            hotel.setAddress(address);//to give more info at post request return

            Hotel savedHotel = hotelRepository.save(hotel);

            //also save to the hotelemployees table
            HotelEmployee hotelEmployee = HotelEmployee.builder()
                    .hotelFk(savedHotel.getHotelPk())
                    .employeeFk(hotelRequest.getEmployeeFk())
                    .build();
            hotelEmployeeRepository.save(hotelEmployee);
            return savedHotel;
        } else {
            log.error("HotelManager | createHotel | Employee not found by the given id");
            throw new EmployeeNotFoundException("Employee not found by the given id");
        }

    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }


    @Override
    public HotelEmployeeResponse createHotelEmployee(HotelEmployeeRequest hotelEmployeeRequest) {
        hotelRepository.findById(hotelEmployeeRequest.getHotelFk()).orElseThrow(() -> new HotelNotFoundException("Hotel not found by the given id"));
        HotelEmployee hotelEmployee = modelMapper.map(hotelEmployeeRequest, HotelEmployee.class);
        HotelEmployee savedHotelEmployee = hotelEmployeeRepository.save(hotelEmployee);
        return modelMapper.map(savedHotelEmployee, HotelEmployeeResponse.class);
    }

    @Override
    public boolean isHotelAvailable(long hotelPk) {
        return hotelRepository.existsByHotelPk(hotelPk);
    }
}


