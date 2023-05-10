package com.ossovita.reservationservice.core.dataAccess;

import com.ossovita.reservationservice.core.entities.OnlineReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineReservationRepository extends JpaRepository<OnlineReservation, Long> {


}
