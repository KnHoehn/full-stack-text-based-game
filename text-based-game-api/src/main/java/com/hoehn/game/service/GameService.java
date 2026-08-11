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

        GameState gameState = new GameState();

        gameState.setUserName(userName);

        Theme theme = themeService.chooseTheme(themeChoice);

        gameState.setTheme(theme.getName());

        gameState.setCurrentRoom(theme.getStartingRoom());

        activeGames.put(gameState.getGameId(), gameState);

        return gameState;

    }
}
