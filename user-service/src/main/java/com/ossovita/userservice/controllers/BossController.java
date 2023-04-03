package com.ossovita.userservice.controllers;

import com.ossovita.userservice.business.abstracts.BossService;
import com.ossovita.userservice.core.entities.dto.BossSaveFormDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/user/boss")
public class BossController {

    BossService bossService;

    public BossController(BossService bossService) {
        this.bossService = bossService;
    }

    @PostMapping("/create-boss")
    public BossSaveFormDto createBoss(@Valid @RequestBody BossSaveFormDto bossSaveFormDto) {
        return bossService.createBoss(bossSaveFormDto);
    }

    @GetMapping("/is-boss-available")
    public boolean isBossAvailable(@RequestParam long bossPk) {
        return bossService.isBossAvailable(bossPk);
    }
}
