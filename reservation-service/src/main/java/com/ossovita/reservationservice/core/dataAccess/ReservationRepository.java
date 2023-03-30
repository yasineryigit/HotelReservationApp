package com.ossovita.reservationservice.core.dataAccess;

import com.ossovita.reservationservice.core.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByReservationPk(long reservationPk);
}
