package com.ossovita.hotelservice.business.abstracts;

import com.ossovita.commonservice.core.payload.request.HotelEmployeeRequest;
import com.ossovita.commonservice.core.payload.request.HotelRequest;
import com.ossovita.hotelservice.core.entities.dto.response.HotelEmployeeResponse;
import com.ossovita.hotelservice.core.entities.Hotel;
import com.ossovita.hotelservice.core.entities.dto.request.HotelWithImagesRequest;

import java.io.IOException;
import java.util.List;

public interface HotelService {
    Hotel createHotel(HotelRequest hotelRequest);


    List<Hotel> getAllHotels();

    HotelEmployeeResponse createHotelEmployee(HotelEmployeeRequest hotelEmployeeRequest);

    boolean isHotelAvailable(long hotelPk);


    Hotel createHotelWithHotelImages(HotelWithImagesRequest hotelWithImagesRequest) throws IOException;

}
