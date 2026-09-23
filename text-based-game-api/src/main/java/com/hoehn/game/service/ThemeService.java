package com.hoehn.game.service;

import com.hoehn.game.models.Room;
import com.hoehn.game.models.Theme;
import com.hoehn.game.models.World;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ThemeService {

    public Theme chooseTheme(String themeChoice) {

        String filepath = null;

        switch (themeChoice) {

            case "space":
                filepath = "src/main/resources/space-theme.json";
                break;
            case "medieval":
                filepath = "src/main/resources/medieval-theme.json";
                break;
            case "cyberpunk":
                filepath = "src/main/resources/cyberpunk-theme.json";
                break;
            default:
                throw new IllegalArgumentException("Invalid theme");
        }

        return World.createTheme(filepath);
    }
}
