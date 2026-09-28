package com.hoehn.game.service;

import com.hoehn.game.models.Theme;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

    private final GameWorldService gameWorldService;

    public ThemeService(GameWorldService gameWorldService) {
        this.gameWorldService = gameWorldService;
    }

    public Theme chooseTheme(String themeChoice) {

        String filepath = switch (themeChoice) {
            case "space" -> "src/main/resources/space-theme.json";
            case "medieval" -> "src/main/resources/medieval-theme.json";
            case "cyberpunk" -> "src/main/resources/cyberpunk-theme.json";
            default -> throw new IllegalArgumentException("Invalid theme");
        };

        return gameWorldService.createTheme(filepath);
    }
}
