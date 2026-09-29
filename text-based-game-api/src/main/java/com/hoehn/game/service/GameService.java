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

    // Constant that represents the number of items the player needs to win the game
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

    // Holds the current active game sessions
    private final Map<String, GameState> activeGames = new HashMap<>();

    // Starts a new game
    public GameResponse startGame(String userName, String themeChoice) {

        GameState gameState = new GameState();

        // Ties the game to the user
        gameState.setUserName(userName);

        // Selects the theme given the user's choice
        Theme theme = gameWorldService.chooseTheme(themeChoice);

        // Creates the game world given the chosen theme
        gameState.setRooms(gameWorldService.createRooms(theme));

        // Sets the starting variables for the game state
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
                "",
                0,
                0,
                0
        );

    }

    // This method determines what to do, given the user's command input
    public GameResponse processCommand(String gameId, String command) {

        // Validates the player's game
        GameState gameState = validateGame(gameId);

        String movementMessage = "";
        String message = "";
        long finalScore = 0;
        long totalTime = 0;

        String normalizedCommand = command.trim().toLowerCase();

        // Retrieves the current room
        Room currentRoom = gameState.getRooms()
                .get(gameState.getCurrentRoom());

        // If the user inputs a valid move command, processes the movement
        if (normalizedCommand.equals("go north")
                || normalizedCommand.equals("go south")
                || normalizedCommand.equals("go east")
                || normalizedCommand.equals("go west")) {

            movementMessage = processMovement(gameState, normalizedCommand, currentRoom);

            currentRoom = gameState.getRooms()
                    .get(gameState.getCurrentRoom());

            // If the current room contains the boss, processes the end game results
            if (currentRoom.getBoss()) {

                EndGameResult result = processEndGame(gameState);

                message = result.message();
                finalScore = result.finalScore();
                totalTime = result.totalTime();
            }

            // If the player is trying to pick up an item, processes that command
        } else if (normalizedCommand.startsWith("get ")) {

            movementMessage = processGetItem(gameState, normalizedCommand, currentRoom);

            // If the player enters "i", displays the instructions
        }  else if (normalizedCommand.equals("i")) {
            message = "instructions";

            // If the player enters "exit", quits the game
        } else if (normalizedCommand.equals("exit")) {
            gameState.setGameOver(true);
            // Displays a message if the command is invalid
        } else {
            message = "Invalid command. Type 'I' to see the instructions.";
        }

        // Returns the updated game response information to the front end
        return new GameResponse(
                gameState.getGameId(),
                gameState.getTheme(),
                "",
                gameState.getCurrentRoom(),
                currentRoom.getItemDescription(),
                gameState.getInventory(),
                gameState.getGameOver(),
                movementMessage,
                message,
                finalScore,
                gameState.getMoves(),
                totalTime
        );
    }

    // Validates the user's game
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

    // Processes the user's string to get the direction they are trying to move
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

        // Gets the direction from the user's command
        String direction = getDirection(command);

        // Retrieves what rooms are connected to the current one
        Map<String, String> connectedRooms =
                currentRoom.getConnectedRooms();

        // If there are no connected rooms in the direction, returns an error message
        if (!connectedRooms.containsKey(direction)) {
            return "You cannot go that way.";
        }

        // Gets the connected room that corresponds with the direction the player is moving
        String nextRoomName = connectedRooms.get(direction);

        // Sets the current room to the next room
        gameState.setCurrentRoom(nextRoomName);

        // Increments the player's move count
        gameState.incrementMoves();

        // Applies a 10-point penalty for each move over 10 moves
        if (gameState.getMoves() > 10) {
            gameState.scorePenalty(10);
        }

        // Returns a message saying which direction the player moved
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

        // Otherwise tells the player there is no such item in the room
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

        // Gets the player's final move score
        int finalMoveScore = gameState.getMoveScore();

        String message;

        // If player wins, displays winning message
        if (gameState.getInventory().size() == NUM_ITEMS_TO_WIN) {

            message = "You see the " + gameState.getBoss() + ".\n\n"
                    + "A battle ensues.\n\n"
                    + "...\n\n"
                    + "Congratulations! You defeated the "
                    + gameState.getBoss() + "!";

            // If player lost, displays losing message and zeros out the move score
        } else {

            message = "You see the " + gameState.getBoss() + ".\n\n"
                    + "A battle ensues...\n\n"
                    + "...\n\n"
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

        // Retrieves the current user from the database
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

        // Set the game as over
        gameState.setGameOver(true);

        return new EndGameResult(message, finalScore, totalTime);
    }

    // Returns the active game
    public GameState getGame(String gameId) {

        return activeGames.get(gameId);
    }

}
