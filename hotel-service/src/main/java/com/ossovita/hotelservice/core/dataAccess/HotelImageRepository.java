package com.ossovita.hotelservice.core.dataAccess;

import com.ossovita.hotelservice.core.entities.Hotel;
import com.ossovita.hotelservice.core.entities.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {


}
