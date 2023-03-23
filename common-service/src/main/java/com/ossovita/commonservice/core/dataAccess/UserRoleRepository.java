package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
