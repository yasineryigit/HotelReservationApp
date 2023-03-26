package com.ossovita.userservice.business.concretes;

import com.ossovita.commonservice.core.entities.UserRole;
import com.ossovita.userservice.business.abstracts.UserRoleService;
import com.ossovita.commonservice.core.dataAccess.UserRoleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class UserRoleManager implements UserRoleService {

    UserRoleRepository userRoleRepository;

    public UserRoleManager(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}
