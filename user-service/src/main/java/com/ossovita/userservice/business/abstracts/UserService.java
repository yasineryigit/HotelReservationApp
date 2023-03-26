package com.ossovita.userservice.business.abstracts;

import com.ossovita.commonservice.core.entities.User;

import java.util.Optional;

public interface UserService {


    Optional<User> findByUsername(String username);

    User findByUserPk(long userPk);
}
