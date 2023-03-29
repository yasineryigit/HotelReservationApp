package com.ossovita.userservice.business.abstracts;

import com.ossovita.userservice.core.entities.User;

import java.util.Optional;

public interface UserService {


    Optional<User> findByUserEmail(String userEmail);

    User findByUserPk(long userPk);
}
