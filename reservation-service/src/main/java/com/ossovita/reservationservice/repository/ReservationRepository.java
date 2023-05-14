package com.ossovita.reservationservice.repository;

import com.ossovita.reservationservice.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByReservationPk(long reservationPk);

    List<Reservation> findByRoomFkIn(List<Long> roomFkList);
}
