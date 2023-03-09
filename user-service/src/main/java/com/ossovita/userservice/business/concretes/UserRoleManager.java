package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.business.abstracts.UserRoleService;
import com.ossovita.userservice.core.dataAccess.UserRoleRepository;
import com.ossovita.userservice.core.entities.UserRole;
import org.springframework.stereotype.Service;

import java.io.Serial;

@Service
public class UserRoleManager implements UserRoleService {

    UserRoleRepository userRoleRepository;

    public UserRoleManager(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}
