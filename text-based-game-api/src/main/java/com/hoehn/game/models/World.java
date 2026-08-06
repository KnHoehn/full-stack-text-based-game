package com.hoehn.game.models;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

public final class World {

    private World() {}

    /** This method creates the game theme given the theme choice from the user.
     *
     * @param filePath the filepath for the json of the theme the player chose.
     * @return the theme of the game.
     */
    public static Theme createTheme(final String filePath) {

        Theme theme = new Theme();

        Gson gson = new Gson();

        // Parses the json file and creates the theme object.
        try (Reader reader = new FileReader(filePath)) {

            theme = gson.fromJson(reader, Theme.class);


        } catch (IOException e) {
            System.err.println("Unable to open file");
        }

        return theme;
    }

    /**This method creates the rooms of the game given the theme.
     *
     * @param theme the theme of the game.
     * @return the rooms mapping.
     * */
    public static Map<String, Room> createRooms(final Theme theme) {

        Map<String, Room> rooms = new HashMap<>();

        for (Room room : theme.getRooms()) {
            rooms.put(room.getName(), room);
        }

        return rooms;
    }
}
