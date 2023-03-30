package com.ossovita.hotelservice.core.dataAccess;


import com.ossovita.hotelservice.core.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomPk(long roomPk);
}
