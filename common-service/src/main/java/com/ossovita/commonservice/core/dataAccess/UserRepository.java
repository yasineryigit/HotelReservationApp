package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserEmail(String userEmail);

}
