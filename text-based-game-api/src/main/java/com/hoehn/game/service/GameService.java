package com.hoehn.game.service;

import com.hoehn.game.dto.GameResponse;
import com.hoehn.game.models.GameState;
import com.hoehn.game.models.Room;
import com.hoehn.game.models.Theme;
import com.hoehn.game.models.World;
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

    public GameResponse startGame(String userName, String themeChoice) {

        // Starts a new game
        GameState gameState = new GameState();

        // Ties the game to the user
        gameState.setUserName(userName);

        // Selects the theme given the user's choice
        Theme theme = themeService.chooseTheme(themeChoice);

        gameState.setRooms(World.createRooms(theme));

        // Sets the theme in the gamestate
        gameState.setTheme(theme.getName());

        // Sets the current room given the chose theme in the gamestate
        gameState.setCurrentRoom(theme.getStartingRoom());

        // Adds the game to the list of active games
        activeGames.put(gameState.getGameId(), gameState);

        return new GameResponse(
                gameState.getGameId(),
                theme.getName(),
                theme.getStory(),
                gameState.getCurrentRoom(),
                gameState.getInventory(),
                gameState.getGameOver(),
                "Game started."
        );

    }

    public GameResponse processCommand(String gameId, String command) {

        GameState gameState = getGame(gameId);

        if (gameState == null) {
            throw new IllegalArgumentException("Game not found.");
        }

        String message = "";

        String normalizedCommand = command.trim().toLowerCase();

        if (normalizedCommand.equals("go north")
                || normalizedCommand.equals("go south")
                || normalizedCommand.equals("go east")
                || normalizedCommand.equals("go west")) {

            String direction = normalizedCommand.substring(3);

            direction = direction.substring(0, 1).toUpperCase()
                    + direction.substring(1);

            Room currentRoom = gameState.getRooms().get(gameState.getCurrentRoom());

            Map<String, String> connectedRooms = currentRoom.getConnectedRooms();

            if (connectedRooms.containsKey(direction)) {

                String nextRoom = connectedRooms.get(direction);

                gameState.setCurrentRoom(nextRoom);

                gameState.incrementMoves();

                message = "You moved " + direction + ".";

            } else {

                message = "You cannot go that way.";
            }
        } else if (normalizedCommand.startsWith("get ")) {

            String itemName = normalizedCommand.substring(4).trim();

            Room currentRoom = gameState.getRooms().get(gameState.getCurrentRoom());

            if (currentRoom.getItem() != null
                    && currentRoom.getItem().equalsIgnoreCase(itemName)) {

                gameState.addToInventory(currentRoom.getItem());
                currentRoom.setItem(null);
                message = "You picked up the " + itemName + ".";

            } else {
                message = "That item is not here.";
            }

        }

        else if (normalizedCommand.equals("i")) {
            message = "instructions";
        }

        else {
            message = "Invalid command. Type 'I' to see the instructions.";
        }

        return new GameResponse(
                gameState.getGameId(),
                gameState.getTheme(),
                "",
                gameState.getCurrentRoom(),
                gameState.getInventory(),
                gameState.getGameOver(),
                message
        );
    }

    public GameState getGame(String gameId) {

        return activeGames.get(gameId);
    }

}
