package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
