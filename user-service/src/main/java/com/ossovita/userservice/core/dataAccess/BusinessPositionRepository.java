package com.ossovita.userservice.core.dataAccess;

import com.ossovita.userservice.core.entities.BusinessPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessPositionRepository extends JpaRepository<BusinessPosition, Long> {


}
