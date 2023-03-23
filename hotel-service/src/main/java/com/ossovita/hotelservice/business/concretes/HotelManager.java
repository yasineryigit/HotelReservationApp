package com.ossovita.hotelservice.business.concretes;

import com.ossovita.commonservice.core.dataAccess.AddressRepository;
import com.ossovita.commonservice.core.dataAccess.HotelRepository;
import com.ossovita.commonservice.core.entities.Address;
import com.ossovita.commonservice.core.entities.Hotel;
import com.ossovita.commonservice.core.entities.dtos.HotelSaveFormDto;
import com.ossovita.hotelservice.business.abstracts.HotelService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelManager implements HotelService {

    HotelRepository hotelRepository;
    AddressRepository addressRepository;

    ModelMapper modelMapper;

    public HotelManager(HotelRepository hotelRepository, AddressRepository addressRepository, ModelMapper modelMapper) {
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Hotel createHotel(HotelSaveFormDto hotelSaveFormDto) {
        Address address = modelMapper.map(hotelSaveFormDto, Address.class);
        Address savedAddress = addressRepository.save(address);

        Hotel hotel = modelMapper.map(hotelSaveFormDto, Hotel.class);
        hotel.setAddressFk(savedAddress.getAddressPk());
        hotel.setAddress(address);//to give more info at post request return

        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
}


