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
import org.springframework.security.core.Authentication;

@RestController
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) { this.gameService = gameService; }


    // Endpoint for starting a new game
    @PostMapping("/games")
    public ResponseEntity<GameState> startGame(
            @RequestBody GameStartRequest request, Authentication authentication) {

        String userName = authentication.getName();

        // Creates a new game
        GameState gameState = gameService.startGame(
                userName,
                request.getTheme()
        );

        return ResponseEntity.ok(gameState);
    }

}
