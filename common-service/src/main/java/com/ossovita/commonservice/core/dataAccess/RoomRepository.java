package com.ossovita.commonservice.core.dataAccess;


import com.ossovita.commonservice.core.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
