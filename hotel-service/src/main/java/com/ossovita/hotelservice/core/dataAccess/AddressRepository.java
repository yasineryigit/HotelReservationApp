package com.ossovita.hotelservice.core.dataAccess;

import com.ossovita.hotelservice.core.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {


}
