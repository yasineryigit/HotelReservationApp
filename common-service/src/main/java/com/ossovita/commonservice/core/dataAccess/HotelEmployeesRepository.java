package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.HotelEmployees;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelEmployeesRepository extends JpaRepository<HotelEmployees, Long> {
}
