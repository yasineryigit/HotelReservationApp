package com.ossovita.userservice.business.concretes;

import com.ossovita.userservice.business.abstracts.BossService;
import com.ossovita.userservice.core.dataAccess.BossRepository;
import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.entities.Boss;
import com.ossovita.userservice.core.entities.User;
import com.ossovita.userservice.core.entities.dto.BossSaveFormDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BossManager implements BossService {

    UserRepository userRepository;
    BossRepository bossRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;

    public BossManager(UserRepository userRepository, BossRepository bossRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bossRepository = bossRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    //Boss can ve saved without make relation with hotel initially
    @Override
    public BossSaveFormDto createBoss(BossSaveFormDto bossSaveFormDto) {

        User user = modelMapper.map(bossSaveFormDto, User.class);
        user.setUserRoleFk(3);//userRoleFk=3 is Business
        user.setEnabled(true);//TODO should be false after implementing email verification
        user.setLocked(false);
        user.setUserPassword(passwordEncoder.encode(bossSaveFormDto.getUserPassword()));

        User savedUser = userRepository.save(user);

        Boss boss = Boss.builder()
                .userFk(savedUser.getUserPk())
                .businessPositionFk(1)
                .build();

        bossRepository.save(boss);


        return bossSaveFormDto;
    }

    @Override
    public boolean isBossAvailable(long bossPk) {
        return bossRepository.existsByBossPk(bossPk);
    }


}
