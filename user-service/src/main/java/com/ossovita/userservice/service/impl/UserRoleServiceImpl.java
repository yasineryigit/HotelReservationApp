package com.ossovita.userservice.service.impl;

import com.ossovita.userservice.entity.UserRole;
import com.ossovita.userservice.service.UserRoleService;
import com.ossovita.userservice.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    //@PreAuthorize("hasAuthority('Admin')")
    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}
