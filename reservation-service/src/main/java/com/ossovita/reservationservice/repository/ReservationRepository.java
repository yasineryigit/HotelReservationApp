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

    //return true if there is no booked reservation belongs to given room on given date range
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN false ELSE true END " +
            "FROM Reservation r " +
            "WHERE r.roomFk = :roomFk " +
            "AND (r.reservationStatus = com.ossovita.commonservice.enums.ReservationStatus.BOOKED " +
            "OR r.reservationStatus = com.ossovita.commonservice.enums.ReservationStatus.CHECKED_IN " +
            "OR r.reservationStatus = com.ossovita.commonservice.enums.ReservationStatus.STAY_OVER) " +
            "AND NOT (" +//if dates are overlapping
            "   (r.reservationStartTime < :requestStart AND r.reservationEndTime < :requestStart) " +
            "   OR (r.reservationStartTime > :requestEnd AND r.reservationEndTime > :requestEnd)" +
            ")")
    boolean isRoomAvailableByGivenDateRange(
            @Param("roomFk") long roomFk,
            @Param("requestStart") LocalDateTime requestStart,
            @Param("requestEnd") LocalDateTime requestEnd);

    //return roompk list which has not booked | checked | stay-over reservation by given date range
    @Query("SELECT r.roomFk " +
            "FROM Reservation r " +
            "WHERE r.roomFk IN :roomFkList " +
            "AND (r.reservationStatus = com.ossovita.commonservice.enums.ReservationStatus.BOOKED " +
            "OR r.reservationStatus = com.ossovita.commonservice.enums.ReservationStatus.CHECKED_IN " +
            "OR r.reservationStatus = com.ossovita.commonservice.enums.ReservationStatus.STAY_OVER) " +
            "AND NOT (" +//if dates are overlapping
            "   (r.reservationStartTime < :requestStart AND r.reservationEndTime < :requestStart) " +
            "   OR (r.reservationStartTime > :requestEnd AND r.reservationEndTime > :requestEnd)" +
            ")")
    List<Long> getNotAvailableRoomFkListByGivenDateRange(
            @Param("roomFkList") List<Long> roomFkList,
            @Param("requestStart") LocalDateTime requestStart,
            @Param("requestEnd") LocalDateTime requestEnd);

}
