package com.ossovita.hotelservice.core.dataAccess;

import com.ossovita.hotelservice.core.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    boolean existsByHotelPk(long hotelFk);

}
