package com.banker.duckrace.controller;

import com.banker.duckrace.constant.PlayerService;
import com.banker.duckrace.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<?> getPlayers() {
        return ResponseEntity.ok(playerService.getPlayerSessions());
    }

}
