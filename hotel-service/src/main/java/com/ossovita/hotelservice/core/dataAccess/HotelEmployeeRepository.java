package com.ossovita.hotelservice.core.dataAccess;

import com.ossovita.hotelservice.core.entities.HotelEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelEmployeeRepository extends JpaRepository<HotelEmployee, Long> {
}
