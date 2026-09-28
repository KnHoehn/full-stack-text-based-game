package com.hoehn.game.controller;

import com.hoehn.game.dto.CommandRequest;
import com.hoehn.game.dto.GameResponse;
import com.hoehn.game.dto.GameStartRequest;
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
    public ResponseEntity<GameResponse> startGame(
            @RequestBody GameStartRequest request, Authentication authentication) {

        String userName = authentication.getName();

        GameResponse gameResponse = gameService.startGame(
                userName,
                request.getTheme()
        );

        return ResponseEntity.ok(gameResponse);
    }

    // Endpoint for processing the player's game commands
    @PostMapping("/games/{gameId}/command")
    public ResponseEntity<GameResponse> processCommand(
            @PathVariable String gameId,
            @RequestBody CommandRequest request) {

        GameResponse gameResponse = gameService.processCommand(
                gameId,
                request.getCommand()
        );

        return ResponseEntity.ok(gameResponse);
    }

}
