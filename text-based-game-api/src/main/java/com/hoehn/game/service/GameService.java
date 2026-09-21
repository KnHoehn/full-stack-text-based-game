package com.hoehn.game.service;

import com.hoehn.game.models.GameState;
import com.hoehn.game.models.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {

    private final ThemeService themeService;

    @Autowired
    public GameService(ThemeService themeService) {
        this.themeService = themeService;
    }

    private final Map<String, GameState> activeGames = new HashMap<>();

    public GameState startGame(String userName, String themeChoice) {

        // Starts a new game
        GameState gameState = new GameState();

        // Ties the game to the user
        gameState.setUserName(userName);

        // Selects the theme given the user's choice
        Theme theme = themeService.chooseTheme(themeChoice);

        // Sets the theme in the gamestate
        gameState.setTheme(theme.getName());

        // Sets the current room given the chose theme in the gamestate
        gameState.setCurrentRoom(theme.getStartingRoom());

        // Adds the game to the list of active games
        activeGames.put(gameState.getGameId(), gameState);

        return gameState;

    }
}
