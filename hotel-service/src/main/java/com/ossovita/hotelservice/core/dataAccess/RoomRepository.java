package com.ossovita.hotelservice.core.dataAccess;


import com.ossovita.commonservice.core.entities.enums.RoomStatus;
import com.ossovita.hotelservice.core.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomPk(long roomPk);

    List<Room> findAllByHotelFk(long hotelFk);

    List<Room> findByHotelFkAndRoomStatus(long hotelFk, RoomStatus roomStatus);


}
