package com.hoehn.game.service;

import com.hoehn.game.dto.GameResponse;
import com.hoehn.game.entities.Score;
import com.hoehn.game.models.GameState;
import com.hoehn.game.models.Room;
import com.hoehn.game.models.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hoehn.game.entities.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {

    private final ThemeService themeService;

    private final ScoreService scoreService;

    private final UserService userService;

    private final GameWorldService gameWorldService;

    private static final int NUM_ITEMS_TO_WIN = 6;

    @Autowired
    public GameService(
            ThemeService themeService,
            ScoreService scoreService,
            UserService userService,
            GameWorldService gameWorldService) {

        this.themeService = themeService;
        this.scoreService = scoreService;
        this.userService = userService;
        this.gameWorldService = gameWorldService;
    }

    private final Map<String, GameState> activeGames = new HashMap<>();

    public GameResponse startGame(String userName, String themeChoice) {

        // Starts a new game
        GameState gameState = new GameState();

        // Ties the game to the user
        gameState.setUserName(userName);

        // Selects the theme given the user's choice
        Theme theme = themeService.chooseTheme(themeChoice);

        gameState.setRooms(gameWorldService.createRooms(theme));

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

        GameState gameState = validateGame(gameId);

        String message = "";

        long finalScore = 0;

        long totalTime = 0;

        String normalizedCommand = command.trim().toLowerCase();

        if (normalizedCommand.equals("go north")
                || normalizedCommand.equals("go south")
                || normalizedCommand.equals("go east")
                || normalizedCommand.equals("go west")) {

            message = processMovement(gameState, normalizedCommand);

            Room currentRoom = gameState.getRooms()
                    .get(gameState.getCurrentRoom());

            if (currentRoom.getBoss()) {

                EndGameResult result = processEndGame(gameState);

                message = result.message();
                finalScore = result.finalScore();
                totalTime = result.totalTime();
            }

        } else if (normalizedCommand.startsWith("get ")) {

            message = processGetItem(gameState, normalizedCommand);

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

    private GameState validateGame(String gameId) {
        GameState gameState = getGame(gameId);

        if (gameState == null) {
            throw new IllegalArgumentException("Game not found.");
        }

        if (gameState.getGameOver()) {
            throw new IllegalStateException("Game is already over.");
        }

        return gameState;
    }

    private String getDirection(String command) {
        String direction = command.substring(3);

        return direction.substring(0, 1).toUpperCase()
                + direction.substring(1);
    }

    private String processMovement(
            GameState gameState,
            String command) {

        String direction = getDirection(command);

        Room currentRoom = gameState.getRooms()
                .get(gameState.getCurrentRoom());

        Map<String, String> connectedRooms =
                currentRoom.getConnectedRooms();

        if (!connectedRooms.containsKey(direction)) {
            return "You cannot go that way.";
        }

        String nextRoomName = connectedRooms.get(direction);

        gameState.setCurrentRoom(nextRoomName);

        gameState.incrementMoves();

        if (gameState.getMoves() > 10) {
            gameState.scorePenalty(10);
        }

        return "You moved " + direction + ".";
    }

    private String processGetItem(
            GameState gameState,
            String command) {

        String itemName = command.substring(4).trim();

        Room currentRoom = gameState.getRooms()
                .get(gameState.getCurrentRoom());

        if (currentRoom.getItem() != null
                && currentRoom.getItem().equalsIgnoreCase(itemName)) {

            gameState.addToInventory(currentRoom.getItem());
            currentRoom.setItem(null);
            currentRoom.setItemDescription(null);

            return "You picked up the " + itemName + ".";
        }

        return "That item is not here.";
    }

    private record EndGameResult(
            String message,
            long finalScore,
            long totalTime
    ) {}

    private EndGameResult processEndGame(GameState gameState) {

        long totalTime = (System.currentTimeMillis()
                - gameState.getStartTime()) / 1000;

        int finalMoveScore = gameState.getMoveScore();

        String message;

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

        long finalScore = ScoreCalculator.calculateScore(
                totalTime,
                finalMoveScore
        );

        Score score = new Score();

        User user = userService.getMatchingUserName(gameState.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        score.setUser(user);
        score.setScore((int) finalScore);
        score.setMoves(gameState.getMoves());
        score.setTime((int) totalTime);
        score.setTheme(gameState.getTheme());

        scoreService.createScore(score);

        gameState.setGameOver(true);

        return new EndGameResult(message, finalScore, totalTime);
    }


    public GameState getGame(String gameId) {

        return activeGames.get(gameId);
    }

}
