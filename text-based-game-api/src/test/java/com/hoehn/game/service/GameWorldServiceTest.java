package com.hoehn.game.service;

import com.hoehn.game.models.Room;
import com.hoehn.game.models.Theme;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameWorldServiceTest {

    private final GameWorldService gameWorldService = new GameWorldService();

    @Test
    void chooseThemeSpace() {
        Theme theme = gameWorldService.chooseTheme("space");

        assertNotNull(theme);
        assertNotNull(theme.getName());
        assertNotNull(theme.getRooms());
    }

    @Test
    void chooseThemeMedieval() {
        Theme theme = gameWorldService.chooseTheme("medieval");

        assertNotNull(theme);
        assertNotNull(theme.getName());
        assertNotNull(theme.getRooms());
    }

    @Test
    void chooseThemeCyberpunk() {
        Theme theme = gameWorldService.chooseTheme("cyberpunk");

        assertNotNull(theme);
        assertEquals("Cyberpunk Text Adventure Game", theme.getName());
        assertNotNull(theme.getRooms());
    }

    @Test
    void chooseThemeInvalidTheme() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gameWorldService.chooseTheme("invalid")
        );
    }

    @Test
    void createTheme() {
        Theme theme = gameWorldService.createTheme(
                "src/main/resources/space-theme.json"
        );

        assertNotNull(theme);
        assertNotNull(theme.getName());
        assertNotNull(theme.getRooms());
    }

    @Test
    void createThemeFileNotFound() {
        Theme theme = gameWorldService.createTheme("invalid-file.json");

        assertNotNull(theme);
    }

    @Test
    void createRooms() {
        Theme theme = gameWorldService.chooseTheme("cyberpunk");

        Map<String, Room> rooms = gameWorldService.createRooms(theme);

        assertNotNull(rooms);
        assertEquals(theme.getRooms().length, rooms.size());

        for (Room room : theme.getRooms()) {
            assertEquals(room, rooms.get(room.getName()));
        }
    }
}