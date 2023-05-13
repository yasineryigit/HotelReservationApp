package com.ossovita.hotelservice.service;

import com.ossovita.commonservice.payload.request.HotelEmployeeRequest;
import com.ossovita.commonservice.payload.request.HotelRequest;
import com.ossovita.hotelservice.payload.response.HotelEmployeeResponse;
import com.ossovita.hotelservice.entity.Hotel;
import com.ossovita.hotelservice.payload.request.HotelWithImagesRequest;

import java.io.IOException;
import java.util.List;

public interface HotelService {
    Hotel createHotel(HotelRequest hotelRequest);


    List<Hotel> getAllHotels();

    HotelEmployeeResponse createHotelEmployee(HotelEmployeeRequest hotelEmployeeRequest);

    boolean isHotelAvailable(long hotelPk);


    Hotel createHotelWithHotelImages(HotelWithImagesRequest hotelWithImagesRequest) throws IOException;

}
