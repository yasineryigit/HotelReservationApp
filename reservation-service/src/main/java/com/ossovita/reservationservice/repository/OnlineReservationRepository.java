package com.ossovita.reservationservice.repository;

import com.ossovita.reservationservice.entity.OnlineReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineReservationRepository extends JpaRepository<OnlineReservation, Long> {


}
