package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {


}
