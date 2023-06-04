package com.ossovita.userservice.service.impl;

import com.ossovita.commonservice.exception.IdNotFoundException;
import com.ossovita.userservice.entity.Boss;
import com.ossovita.userservice.entity.User;
import com.ossovita.userservice.payload.BossSaveFormDto;
import com.ossovita.userservice.repository.BossRepository;
import com.ossovita.userservice.repository.UserRepository;
import com.ossovita.userservice.service.BossService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BossServiceImpl implements BossService {

    UserRepository userRepository;
    BossRepository bossRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;

    public BossServiceImpl(UserRepository userRepository, BossRepository bossRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bossRepository = bossRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    //TODO | refactor
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

    @Override
    public Boss getBoss(long bossPk) {
        return bossRepository.findById(bossPk)
                .orElseThrow(() -> {throw new IdNotFoundException("Boss not available by given id");});
    }

}
