package com.hoehn.game.service;

import com.hoehn.game.dto.GameResponse;
import com.hoehn.game.entities.Score;
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

    private final ScoreService scoreService;

    private static final int NUM_ITEMS_TO_WIN = 6;

    @Autowired
    public GameService(ThemeService themeService, ScoreService scoreService) {
        this.themeService = themeService;
        this.scoreService = scoreService;
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

        gameState.setBoss(theme.getBoss());

        gameState.setLoseBattleMessage(theme.getBattleLossMessage());

        // Sets the current room given the chose theme in the gamestate
        gameState.setCurrentRoom(theme.getStartingRoom());

        // Adds the game to the list of active games
        activeGames.put(gameState.getGameId(), gameState);

        Room startingRoom = gameState.getRooms().get(gameState.getCurrentRoom());

        return new GameResponse(
                gameState.getGameId(),
                theme.getName(),
                theme.getStory(),
                gameState.getCurrentRoom(),
                startingRoom.getItemDescription(),
                gameState.getInventory(),
                gameState.getGameOver(),
                "Game started.",
                0,
                0,
                0
        );

    }

    public GameResponse processCommand(String gameId, String command) {

        GameState gameState = getGame(gameId);

        if (gameState == null) {
            throw new IllegalArgumentException("Game not found.");
        }

        // Prevents the user from entering another command after the game is over
        if (gameState.getGameOver()) {
            throw new IllegalStateException("Game is already over.");
        }

        String message = "";

        long finalScore = 0;

        long totalTime = 0;

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

                String nextRoomName = connectedRooms.get(direction);

                gameState.setCurrentRoom(nextRoomName);

                gameState.incrementMoves();

                if (gameState.getMoves() > 10) {
                    gameState.scorePenalty(10);
                }

                Room nextRoom = gameState.getRooms().get(nextRoomName);

                if (nextRoom.getBoss()) {

                    totalTime = (System.currentTimeMillis()
                            - gameState.getStartTime()) / 1000;

                    int finalMoveScore = gameState.getMoveScore();

                    if (gameState.getInventory().size() == NUM_ITEMS_TO_WIN) {
                        message = "You see the " + gameState.getBoss() + ".\n"
                                + "A battle ensues.\n"
                                + "...\n"
                                + "Congratulations! You defeated "
                                + gameState.getBoss() + "!";

                    } else {
                        message = "You see the " + gameState.getBoss() + ".\n"
                                + "A battle ensues...\n"
                                + "...\n"
                                + gameState.getLoseBattleMessage() + " Game over";

                        gameState.setGameOver(true);

                        finalMoveScore = 0;
                    }

                    finalScore = ScoreCalculator.calculateScore(
                            totalTime,
                            finalMoveScore
                    );

                    Score score = new Score();

                    score.setUser(gameState.getUserName());
                    score.setScore((int) finalScore);
                    score.setMoves(gameState.getMoves());
                    score.setTime((int) totalTime);
                    score.setTheme(gameState.getTheme());

                    scoreService.createScore(score);

                    gameState.setGameOver(true);

                } else {
                    message = "You moved " + direction + ".";
                }

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
                currentRoom.setItemDescription(null);
                message = "You picked up the " + itemName + ".";

            } else {
                message = "That item is not here.";
            }

        }  else if (normalizedCommand.equals("i")) {
            message = "instructions";

        } else if (normalizedCommand.equals("exit")) {
            gameState.setGameOver(true);

        } else {
            message = "Invalid command. Type 'I' to see the instructions.";
        }

        Room currentRoom = gameState.getRooms().get(gameState.getCurrentRoom());

        return new GameResponse(
                gameState.getGameId(),
                gameState.getTheme(),
                "",
                gameState.getCurrentRoom(),
                currentRoom.getItemDescription(),
                gameState.getInventory(),
                gameState.getGameOver(),
                message,
                finalScore,
                gameState.getMoves(),
                totalTime
        );
    }

    public GameState getGame(String gameId) {

        return activeGames.get(gameId);
    }

}
