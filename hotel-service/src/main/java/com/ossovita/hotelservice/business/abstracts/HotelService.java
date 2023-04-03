package com.ossovita.hotelservice.business.abstracts;

import com.ossovita.commonservice.core.entities.dtos.request.HotelEmployeeRequest;
import com.ossovita.commonservice.core.entities.dtos.request.HotelRequest;
import com.ossovita.hotelservice.core.entities.dto.response.HotelEmployeeResponse;
import com.ossovita.hotelservice.core.entities.Hotel;
import com.ossovita.hotelservice.core.entities.dto.request.HotelWithImagesRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface HotelService {
    Hotel createHotel(HotelRequest hotelRequest);


    List<Hotel> getAllHotels();

    HotelEmployeeResponse createHotelEmployee(HotelEmployeeRequest hotelEmployeeRequest);

    boolean isHotelAvailable(long hotelPk);


    Hotel createHotelWithHotelImages(HotelWithImagesRequest hotelWithImagesRequest) throws IOException;

}
