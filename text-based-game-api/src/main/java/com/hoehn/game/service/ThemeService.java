package com.hoehn.game.service;

import com.hoehn.game.models.Room;
import com.hoehn.game.models.Theme;
import com.hoehn.game.models.World;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ThemeService {

    //TODO handle if user enters invalid number

    public Theme chooseTheme(String themeChoice) {

        String filepath = null;

        switch (themeChoice) {

            case "1":
                filepath = "src/main/resources/space-theme.json";
                break;
            case "2":
                filepath = "src/main/resources/medieval-theme.json";
                break;
            case "3":
                filepath = "src/main/resources/cyberpunk-theme.json";
                break;
            default:
                System.out.println("Please enter a valid number");
        }

        return World.createTheme(filepath);
    }
}
