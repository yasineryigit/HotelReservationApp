package com.ossovita.hotelservice.business.abstracts;

import com.ossovita.commonservice.core.entities.dtos.request.HotelEmployeeRequest;
import com.ossovita.commonservice.core.entities.dtos.response.HotelEmployeeResponse;
import com.ossovita.hotelservice.core.entities.Hotel;
import com.ossovita.commonservice.core.entities.dtos.request.HotelRequest;
import com.ossovita.hotelservice.core.entities.HotelEmployee;

import java.util.List;

public interface HotelService {
    Hotel createHotel(HotelRequest hotelRequest);


    List<Hotel> getAllHotels();

    HotelEmployeeResponse createHotelEmployee(HotelEmployeeRequest hotelEmployeeRequest);

    boolean isHotelAvailable(long hotelPk);


}
