package com.ossovita.userservice.service;

import com.ossovita.userservice.payload.BossSaveFormDto;

public interface BossService {
    BossSaveFormDto createBoss(BossSaveFormDto bossSaveFormDto);

    boolean isBossAvailable(long bossPk);
}
