package com.ossovita.userservice.core.utilities;

import com.ossovita.userservice.core.dataAccess.BusinessPositionRepository;
import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.dataAccess.UserRoleRepository;
import com.ossovita.userservice.core.entities.BusinessPosition;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.core.entities.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyCommandLineRunner implements CommandLineRunner {


    UserRoleRepository userRoleRepository;
    BusinessPositionRepository businessPositionRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public MyCommandLineRunner(UserRoleRepository userRoleRepository, BusinessPositionRepository businessPositionRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRoleRepository = userRoleRepository;
        this.businessPositionRepository = businessPositionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        //check if not saved
        if (userRoleRepository.findAll().isEmpty()) {
            //save user roles into db
            UserRole userRoleAdmin = UserRole.builder()
                    .userRole("Admin")
                    .build();
            UserRole userRoleCustomer = UserRole.builder()
                    .userRole("Customer")
                    .build();
            UserRole userRoleBusiness = UserRole.builder()
                    .userRole("Business")
                    .build();
            userRoleRepository.save(userRoleAdmin);
            userRoleRepository.save(userRoleCustomer);
            userRoleRepository.save(userRoleBusiness);
            log.info("UserService | My CommandLineRunner | user role data initialized");

            //save demo admin into db
            User user = User.builder()
                    .userEmail("admin@gmail.com")
                    .userPassword(passwordEncoder.encode("User.123"))
                    .userFirstName("Demo")
                    .userLastName("Admin")
                    .userRoleFk(1)
                    .enabled(true)
                    .locked(false)
                    .build();
            log.info("UserService | My CommandLineRunner | demo admin initialized");
            userRepository.save(user);

        }

        //check if not saved
        if (businessPositionRepository.findAll().isEmpty()) {
            //save employeepositions into db
            BusinessPosition businessPositionBoss = BusinessPosition.builder()
                    .businessPositionName("Boss").build();
            BusinessPosition businessPositionManager = BusinessPosition.builder()
                    .businessPositionName("Manager").build();
            BusinessPosition businessPositionFrontDesk = BusinessPosition.builder()
                    .businessPositionName("FrontDesk").build();
            businessPositionRepository.save(businessPositionBoss);
            businessPositionRepository.save(businessPositionManager);
            businessPositionRepository.save(businessPositionFrontDesk);
            log.info("UserService | My CommandLineRunner | employee position data initialized");
        }




    }
}
