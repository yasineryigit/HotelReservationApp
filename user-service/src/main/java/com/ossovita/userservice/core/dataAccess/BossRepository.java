package com.ossovita.userservice.core.dataAccess;

import com.ossovita.userservice.core.entities.Boss;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BossRepository extends JpaRepository<Boss,Long> {

    boolean existsByBossPk(long bossPk);
}
