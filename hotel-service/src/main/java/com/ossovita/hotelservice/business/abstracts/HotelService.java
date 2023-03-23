package com.ossovita.hotelservice.business.abstracts;

import com.ossovita.commonservice.core.entities.Hotel;
import com.ossovita.commonservice.core.entities.dtos.HotelSaveFormDto;

import java.util.List;

public interface HotelService {
    Hotel createHotel(HotelSaveFormDto hotelSaveFormDto);


    List<Hotel> getAllHotels();
}
