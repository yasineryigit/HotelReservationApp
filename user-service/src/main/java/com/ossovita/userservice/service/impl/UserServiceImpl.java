package com.ossovita.userservice.service.impl;

import com.ossovita.userservice.repository.UserRepository;
import com.ossovita.userservice.entity.User;
import com.ossovita.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    @Override
    public User findByUserPk(long userPk) {
        return userRepository.findByUserPk(userPk);
    }

}
