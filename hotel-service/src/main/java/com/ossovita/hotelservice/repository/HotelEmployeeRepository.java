package com.ossovita.hotelservice.repository;

import com.ossovita.hotelservice.entity.HotelEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelEmployeeRepository extends JpaRepository<HotelEmployee, Long> {
}
