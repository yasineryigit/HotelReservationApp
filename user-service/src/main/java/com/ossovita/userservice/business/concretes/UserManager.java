package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.business.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    @Override
    public User findByUserPk(long userPk) {
        return userRepository.findByUserPk(userPk);
    }

}
