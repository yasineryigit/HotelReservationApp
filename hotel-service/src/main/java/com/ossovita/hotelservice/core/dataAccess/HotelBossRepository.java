package com.ossovita.hotelservice.core.dataAccess;

import com.ossovita.hotelservice.core.entities.HotelBoss;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelBossRepository extends JpaRepository<HotelBoss, Long> {
}
