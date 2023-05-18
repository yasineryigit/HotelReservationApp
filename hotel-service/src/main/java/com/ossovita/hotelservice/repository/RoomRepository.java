package com.ossovita.hotelservice.repository;


import com.ossovita.commonservice.enums.RoomStatus;
import com.ossovita.hotelservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomPk(long roomPk);

    List<Room> findAllByHotelFk(long hotelFk);

    List<Room> findByHotelFkAndRoomStatus(long hotelFk, RoomStatus roomStatus);

    List<Room> findRoomsByHotel_Address_AddressCity(String addressCity);

}
