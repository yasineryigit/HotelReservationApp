package com.ossovita.reservationservice.repository;

import com.ossovita.reservationservice.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByReservationPk(long reservationPk);

    List<Reservation> findByRoomFkIn(List<Long> roomFkList);

    // if booked and overlapping reservations > 0 false ELSE true
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN false ELSE true END " +
            "FROM Reservation r " +
            "WHERE r.roomFk = :roomFk " +
            "AND r.reservationStatus = com.ossovita.commonservice.enums.ReservationStatus.BOOKED " +
            "AND NOT (" +
            "   (r.reservationStartTime < :requestStart AND r.reservationEndTime < :requestStart) " +
            "   OR (r.reservationStartTime > :requestEnd AND r.reservationEndTime > :requestEnd)" +
            ")")
    boolean isRoomAvailableByGivenDateRange(
            @Param("roomFk") long roomFk,
            @Param("requestStart") LocalDateTime requestStart,
            @Param("requestEnd") LocalDateTime requestEnd);







}
