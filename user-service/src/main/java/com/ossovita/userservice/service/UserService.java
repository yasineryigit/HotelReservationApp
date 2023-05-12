package com.ossovita.userservice.service;

import com.ossovita.userservice.entity.User;

import java.util.Optional;

public interface UserService {


    Optional<User> findByUserEmail(String userEmail);

    User findByUserPk(long userPk);
}
