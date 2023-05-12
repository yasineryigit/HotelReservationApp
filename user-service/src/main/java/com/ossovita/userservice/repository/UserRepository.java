package com.ossovita.userservice.repository;

import com.ossovita.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserEmail(String userEmail);

    Optional<User> findByUserEmail(String userEmail);

    User findByUserPk(long userPk);
}
