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

    private final ScoreService scoreService;

    private final UserService userService;

    private final GameWorldService gameWorldService;

    // Variable constant that represents the number of items the player needs to win the game
    private static final int NUM_ITEMS_TO_WIN = 6;

    @Autowired
    public GameService(
            ScoreService scoreService,
            UserService userService,
            GameWorldService gameWorldService) {

        this.scoreService = scoreService;
        this.userService = userService;
        this.gameWorldService = gameWorldService;
    }

    // Variable that holds the current active game sessions
    private final Map<String, GameState> activeGames = new HashMap<>();

    // This method starts the game
    public GameResponse startGame(String userName, String themeChoice) {

        // Starts a new game
        GameState gameState = new GameState();

        // Ties the game to the user
        gameState.setUserName(userName);

        // Selects the theme given the user's choice
        Theme theme = gameWorldService.chooseTheme(themeChoice);

        // Creates the game world given the chosen theme
        gameState.setRooms(gameWorldService.createRooms(theme));

        // Sets the starting variables to the gamestate
        gameState.setTheme(theme.getName());
        gameState.setBoss(theme.getBoss());
        gameState.setLoseBattleMessage(theme.getBattleLossMessage());
        gameState.setCurrentRoom(theme.getStartingRoom());

        // Adds the game to the list of active games
        activeGames.put(gameState.getGameId(), gameState);

        // Sets the room the player starts in
        Room startingRoom = gameState.getRooms().get(gameState.getCurrentRoom());

        // Returns a game response to the front end
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

    // This method determines what to do, given the user's command input
    public GameResponse processCommand(String gameId, String command) {

        GameState gameState = validateGame(gameId);

        String message = "";
        long finalScore = 0;
        long totalTime = 0;

        String normalizedCommand = command.trim().toLowerCase();

        Room currentRoom = gameState.getRooms()
                .get(gameState.getCurrentRoom());

        if (normalizedCommand.equals("go north")
                || normalizedCommand.equals("go south")
                || normalizedCommand.equals("go east")
                || normalizedCommand.equals("go west")) {

            message = processMovement(gameState, normalizedCommand, currentRoom);

            currentRoom = gameState.getRooms()
                    .get(gameState.getCurrentRoom());

            if (currentRoom.getBoss()) {

                EndGameResult result = processEndGame(gameState);

                message = result.message();
                finalScore = result.finalScore();
                totalTime = result.totalTime();
            }

        } else if (normalizedCommand.startsWith("get ")) {

            message = processGetItem(gameState, normalizedCommand, currentRoom);

        }  else if (normalizedCommand.equals("i")) {
            message = "instructions";

        } else if (normalizedCommand.equals("exit")) {
            gameState.setGameOver(true);

        } else {
            message = "Invalid command. Type 'I' to see the instructions.";
        }

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

    // Processes the player's movement command
    private String processMovement(
            GameState gameState,
            String command,
            Room currentRoom) {

        // Gets the direction the player is trying to move
        String direction = getDirection(command);

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

    // Processes the player's command if they are trying to pick up an item
    private String processGetItem(
            GameState gameState,
            String command,
            Room currentRoom) {

        // Gets the name of the item the user is trying to pick up
        String itemName = command.substring(4).trim();

        // Adds the item to inventory if that item is present in the room
        if (currentRoom.getItem() != null
                && currentRoom.getItem().equalsIgnoreCase(itemName)) {

            gameState.addToInventory(currentRoom.getItem());
            currentRoom.setItem(null);
            currentRoom.setItemDescription(null);

            return "You picked up the " + itemName + ".";
        }

        // Otherwise tells the player there is no such item
        return "That item is not here.";
    }

    // Record that will hold the end game results
    private record EndGameResult(
            String message,
            long finalScore,
            long totalTime
    ) {}

    // Determines if the user won or lost and calculates the end game score
    private EndGameResult processEndGame(GameState gameState) {

        // Calculates the total elapsed time and stores it
        long totalTime = (System.currentTimeMillis()
                - gameState.getStartTime()) / 1000;

        // Gets the player's final move count
        int finalMoveScore = gameState.getMoveScore();

        String message;

        // If player wins, displays winning message
        if (gameState.getInventory().size() == NUM_ITEMS_TO_WIN) {

            message = "You see the " + gameState.getBoss() + ".\n"
                    + "A battle ensues.\n"
                    + "...\n"
                    + "Congratulations! You defeated "
                    + gameState.getBoss() + "!";

            // If player lost, displays losing message and zeros out the move score
        } else {

            message = "You see the " + gameState.getBoss() + ".\n"
                    + "A battle ensues...\n"
                    + "...\n"
                    + gameState.getLoseBattleMessage() + " Game over";

            gameState.setGameOver(true);

            finalMoveScore = 0;
        }

        // Calls the ScoreCalculator class to calculate the final score
        long finalScore = ScoreCalculator.calculateScore(
                totalTime,
                finalMoveScore
        );

        // Creates a new score object
        Score score = new Score();

        // Creates a new user object from the currently logged-in user
        User user = userService.getMatchingUserName(gameState.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Sets the score object values
        score.setUser(user);
        score.setScore((int) finalScore);
        score.setMoves(gameState.getMoves());
        score.setTime((int) totalTime);
        score.setTheme(gameState.getTheme());

        // Saves the score into the database
        scoreService.createScore(score);

        // Tell the program that game has ended
        gameState.setGameOver(true);

        return new EndGameResult(message, finalScore, totalTime);
    }

    // Returns the active game
    public GameState getGame(String gameId) {

        return activeGames.get(gameId);
    }

}
