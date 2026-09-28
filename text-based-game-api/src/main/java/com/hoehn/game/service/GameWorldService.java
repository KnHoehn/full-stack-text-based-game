package com.hoehn.game.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.hoehn.game.models.Room;
import com.hoehn.game.models.Theme;
import org.springframework.stereotype.Service;

@Service
public final class GameWorldService {

    // This method loads the corresponding JSON file given the chosen theme
    public Theme chooseTheme(final String themeChoice) {

        String filePath = switch (themeChoice) {
            case "space" -> "src/main/resources/space-theme.json";
            case "medieval" -> "src/main/resources/medieval-theme.json";
            case "cyberpunk" -> "src/main/resources/cyberpunk-theme.json";
            default -> throw new IllegalArgumentException("Invalid theme");
        };

        return createTheme(filePath);
    }

    // method creates the game theme given theme file path
    public Theme createTheme(final String filePath) {

        Theme theme = new Theme();

        Gson gson = new Gson();

        // Parses the json file and creates the theme object.
        try (Reader reader = new FileReader(filePath)) {

            theme = gson.fromJson(reader, Theme.class);


        } catch (IOException _) {
            System.err.println("Unable to open file");
        }

        return theme;
    }

    // This method creates the rooms and maps the game world given the theme.
    public Map<String, Room> createRooms(final Theme theme) {

        Map<String, Room> rooms = new HashMap<>();

        for (Room room : theme.getRooms()) {
            rooms.put(room.getName(), room);
        }

        return rooms;
    }
}

