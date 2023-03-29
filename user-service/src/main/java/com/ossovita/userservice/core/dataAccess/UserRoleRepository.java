package com.ossovita.userservice.core.dataAccess;

import com.ossovita.userservice.core.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
