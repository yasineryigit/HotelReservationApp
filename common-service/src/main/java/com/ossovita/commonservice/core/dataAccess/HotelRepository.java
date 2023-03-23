package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {


}
