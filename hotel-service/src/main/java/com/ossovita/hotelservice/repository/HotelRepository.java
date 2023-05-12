package com.ossovita.hotelservice.repository;

import com.ossovita.hotelservice.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    boolean existsByHotelPk(long hotelFk);

}
