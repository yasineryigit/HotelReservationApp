package com.ossovita.reservationservice.repository;

import com.ossovita.reservationservice.entity.WalkInReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkInReservationRepository extends JpaRepository<WalkInReservation, Long> {

}
