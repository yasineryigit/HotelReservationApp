package com.ossovita.userservice.core.dataAccess;

import com.ossovita.userservice.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserEmail(String userEmail);

}
