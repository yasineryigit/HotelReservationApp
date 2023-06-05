package com.ossovita.userservice.controller;

import com.ossovita.commonservice.dto.BossDto;
import com.ossovita.userservice.service.BossService;
import com.ossovita.userservice.payload.BossSaveFormDto;
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

    @GetMapping("/boss/get-boss-dto-by-boss-pk")
    public BossDto getBossDtoByBossPk(@RequestParam long bossPk){
        return bossService.getBossDtoByBossPk(bossPk);
    }

    @GetMapping("/is-boss-available")
    public boolean isBossAvailable(@RequestParam long bossPk) {
        return bossService.isBossAvailable(bossPk);
    }
}
