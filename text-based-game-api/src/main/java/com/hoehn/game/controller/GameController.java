package com.hoehn.game.controller;

import com.hoehn.game.dto.GameStartRequest;
import com.hoehn.game.models.GameState;
import com.hoehn.game.models.Theme;
import com.hoehn.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) { this.gameService = gameService; }


    @PostMapping("/games")
    public ResponseEntity<GameState> startGame(
            @RequestBody GameStartRequest request) {

        GameState gameState = gameService.startGame(
                request.getUserName(),
                request.getTheme()
        );

        return ResponseEntity.ok(gameState);
    }

}
