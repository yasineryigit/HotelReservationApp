package com.ossovita.userservice.repository;

import com.ossovita.userservice.entity.Boss;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BossRepository extends JpaRepository<Boss,Long> {

    boolean existsByBossPk(long bossPk);
}
