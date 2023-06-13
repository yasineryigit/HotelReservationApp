package com.ossovita.reservationservice.repository;

import com.ossovita.reservationservice.entity.ReservationChecking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationCheckingRepository extends JpaRepository<ReservationChecking, Long> {
}
