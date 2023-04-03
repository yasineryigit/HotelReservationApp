package com.ossovita.userservice.business.abstracts;

import com.ossovita.userservice.core.entities.dto.BossSaveFormDto;

public interface BossService {
    BossSaveFormDto createBoss(BossSaveFormDto bossSaveFormDto);

    boolean isBossAvailable(long bossPk);
}
