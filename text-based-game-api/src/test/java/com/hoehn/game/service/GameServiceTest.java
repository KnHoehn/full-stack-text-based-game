package com.hoehn.game.service;

import com.google.gson.Gson;
import com.hoehn.game.dto.GameResponse;
import com.hoehn.game.entities.Score;
import com.hoehn.game.entities.User;
import com.hoehn.game.models.GameState;
import com.hoehn.game.models.Room;
import com.hoehn.game.models.Theme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private ScoreService scoreService;

    @Mock
    private UserService userService;

    @Mock
    private GameWorldService gameWorldService;

    private GameService createGameService() {
        return new GameService(
                scoreService,
                userService,
                gameWorldService
        );
    }

    @Test
    void startGame() {
        String json = """
                {
                    "name": "Space Adventure",
                    "boss": "Alien",
                    "story": "Test story",
                    "loseBattleMessage": "You lost.",
                    "startingRoom": "Starting Room",
                    "rooms": [
                        {
                            "name": "Starting Room",
                            "item": "Test Item",
                            "itemDescription": "A test item.",
                            "hasBoss": false,
                            "connectedRooms": {}
                        }
                    ]
                }
                """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse result =
                gameService.startGame("testUser", "space");

        assertNotNull(result.gameId());
        assertEquals("Space Adventure", result.gameName());
        assertEquals("Test story", result.story());
        assertEquals("Starting Room", result.currentRoom());
        assertEquals("A test item.", result.itemDescription());
        assertEquals(0, result.inventory().size());
        assertFalse(result.gameOver());
        assertEquals("Game started.", result.message());
        assertEquals(0, result.score());
        assertEquals(0, result.moves());
        assertEquals(0, result.time());

        assertNotNull(gameService.getGame(result.gameId()));
        assertEquals(
                "testUser",
                gameService.getGame(result.gameId()).getUserName()
        );
    }

    @Test
    void processCommandMovement() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "Starting room.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "North Room"
                        }
                    },
                    {
                        "name": "North Room",
                        "itemDescription": "North room.",
                        "hasBoss": false,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];
        Room northRoom = theme.getRooms()[1];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom,
                northRoom.getName(), northRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(startResult.gameId(), "go north");

        assertEquals("North Room", result.currentRoom());
        assertEquals("You moved North.", result.message());
        assertEquals(1, result.moves());
        assertFalse(result.gameOver());
        assertEquals("North room.", result.itemDescription());
    }

    @Test
    void processCommandInvalidMovement() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "Starting room.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "North Room"
                        }
                    },
                    {
                        "name": "North Room",
                        "itemDescription": "North room.",
                        "hasBoss": false,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];
        Room northRoom = theme.getRooms()[1];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom,
                northRoom.getName(), northRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(startResult.gameId(), "go east");

        assertEquals("Starting Room", result.currentRoom());
        assertEquals("You cannot go that way.", result.message());
        assertEquals(0, result.moves());
        assertFalse(result.gameOver());
    }

    @Test
    void processCommandGetItem() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "item": "Test Item",
                        "itemDescription": "A test item.",
                        "hasBoss": false,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(startResult.gameId(), "get test item");

        assertEquals("You picked up the test item.", result.message());
        assertEquals(1, result.inventory().size());
        assertEquals("Test Item", result.inventory().getFirst());
        assertNull(startingRoom.getItem());
        assertNull(startingRoom.getItemDescription());
    }

    @Test
    void processCommandGetItemNotFound() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "itemDescription": "An empty room.",
                "hasBoss": false,
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "An empty room.",
                        "hasBoss": false,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(
                        startResult.gameId(),
                        "get test item"
                );

        assertEquals("That item is not here.", result.message());
        assertEquals(0, result.inventory().size());
        assertNull(startingRoom.getItem());
    }

    @Test
    void processCommandInstructions() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "Starting room.",
                        "hasBoss": false,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(startResult.gameId(), "i");

        assertEquals("instructions", result.message());
        assertEquals("Starting Room", result.currentRoom());
        assertEquals(0, result.moves());
        assertFalse(result.gameOver());
    }

    @Test
    void processCommandExit() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "Starting room.",
                        "hasBoss": false,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(
                        startResult.gameId(),
                        "exit"
                );

        assertTrue(result.gameOver());
        assertEquals("", result.message());
    }

    @Test
    void processCommandInvalidCommand() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "Starting room.",
                        "hasBoss": false,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(
                        startResult.gameId(),
                        "dance"
                );

        assertEquals(
                "Invalid command. Type 'I' to see the instructions.",
                result.message()
        );
        assertEquals("Starting Room", result.currentRoom());
        assertEquals(0, result.moves());
        assertFalse(result.gameOver());
    }

    @Test
    void processCommandGameNotFound() {
        GameService gameService = createGameService();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> gameService.processCommand(
                                "invalid-game-id",
                                "i"
                        )
                );

        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    void processCommandGameAlreadyOver() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "Starting room.",
                        "hasBoss": false,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        // End the game first.
        gameService.processCommand(startResult.gameId(), "exit");

        String gameId = startResult.gameId();

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> gameService.processCommand(gameId, "i")
                );

        assertEquals("Game is already over.", exception.getMessage());

        assertEquals("Game is already over.", exception.getMessage());
    }

    @Test
    void processCommandBossLoss() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost the battle.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "Starting room.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "Boss Room"
                        }
                    },
                    {
                        "name": "Boss Room",
                        "itemDescription": "The boss is here.",
                        "hasBoss": true,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];
        Room bossRoom = theme.getRooms()[1];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom,
                bossRoom.getName(), bossRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        when(userService.getMatchingUserName("testUser"))
                .thenReturn(Optional.of(new User()));

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(
                        startResult.gameId(),
                        "go north"
                );

        assertTrue(result.gameOver());
        assertEquals(0, result.score());
        assertEquals(1, result.moves());

        assertTrue(result.message().contains("Alien"));
        assertTrue(result.message().contains("You lost the battle."));
        assertTrue(result.message().contains("Game over"));
    }

    @Test
    void processCommandBossWin() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost the battle.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "item": "Item 1",
                        "itemDescription": "First item.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "Item Room 2"
                        }
                    },
                    {
                        "name": "Item Room 2",
                        "item": "Item 2",
                        "itemDescription": "Second item.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "Item Room 3"
                        }
                    },
                    {
                        "name": "Item Room 3",
                        "item": "Item 3",
                        "itemDescription": "Third item.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "Item Room 4"
                        }
                    },
                    {
                        "name": "Item Room 4",
                        "item": "Item 4",
                        "itemDescription": "Fourth item.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "Item Room 5"
                        }
                    },
                    {
                        "name": "Item Room 5",
                        "item": "Item 5",
                        "itemDescription": "Fifth item.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "Item Room 6"
                        }
                    },
                    {
                        "name": "Item Room 6",
                        "item": "Item 6",
                        "itemDescription": "Sixth item.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "Boss Room"
                        }
                    },
                    {
                        "name": "Boss Room",
                        "itemDescription": "The boss is here.",
                        "hasBoss": true,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room[] testRooms = theme.getRooms();

        Map<String, Room> rooms = new java.util.HashMap<>();

        for (Room room : testRooms) {
            rooms.put(room.getName(), room);
        }

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        when(userService.getMatchingUserName("testUser"))
                .thenReturn(
                        java.util.Optional.of(
                                new User()
                        )
                );

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        String gameId = startResult.gameId();

        gameService.processCommand(gameId, "get item 1");
        gameService.processCommand(gameId, "go north");

        gameService.processCommand(gameId, "get item 2");
        gameService.processCommand(gameId, "go north");

        gameService.processCommand(gameId, "get item 3");
        gameService.processCommand(gameId, "go north");

        gameService.processCommand(gameId, "get item 4");
        gameService.processCommand(gameId, "go north");

        gameService.processCommand(gameId, "get item 5");
        gameService.processCommand(gameId, "go north");

        gameService.processCommand(gameId, "get item 6");
        GameResponse result =
                gameService.processCommand(gameId, "go north");

        assertTrue(result.gameOver());
        assertEquals(6, result.inventory().size());
        assertEquals(6, result.moves());
        assertTrue(result.score() > 0);

        assertTrue(result.message().contains("Congratulations!"));
        assertTrue(result.message().contains("You defeated Alien!"));
    }

    @Test
    void processCommandAppliesMovePenalty() {
        String json = """
        {
            "name": "Space Adventure",
            "boss": "Alien",
            "story": "Test story",
            "loseBattleMessage": "You lost.",
            "startingRoom": "Starting Room",
            "rooms": [
                {
                    "name": "Starting Room",
                    "itemDescription": "Starting room.",
                    "hasBoss": false,
                    "connectedRooms": {
                        "North": "North Room",
                        "East": "East Room"
                    }
                },
                {
                    "name": "North Room",
                    "itemDescription": "North room.",
                    "hasBoss": false,
                    "connectedRooms": {
                        "South": "Starting Room",
                        "West": "West Room"
                    }
                },
                {
                    "name": "East Room",
                    "itemDescription": "East room.",
                    "hasBoss": false,
                    "connectedRooms": {
                        "West": "Starting Room"
                    }
                },
                {
                    "name": "West Room",
                    "itemDescription": "West room.",
                    "hasBoss": false,
                    "connectedRooms": {
                        "East": "North Room"
                    }
                }
            ]
        }
        """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];
        Room northRoom = theme.getRooms()[1];
        Room eastRoom = theme.getRooms()[2];
        Room westRoom = theme.getRooms()[3];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom,
                northRoom.getName(), northRoom,
                eastRoom.getName(), eastRoom,
                westRoom.getName(), westRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        String gameId = startResult.gameId();

        // Test east and west movement.
        gameService.processCommand(gameId, "go east");
        gameService.processCommand(gameId, "go west");

        // Test north and south movement while reaching 10 moves.
        for (int i = 0; i < 4; i++) {
            gameService.processCommand(gameId, "go north");
            gameService.processCommand(gameId, "go south");
        }

        GameState gameState = gameService.getGame(gameId);

        assertEquals(10, gameState.getMoves());
        assertEquals(1000, gameState.getMoveScore());

        // The 11th move should apply the penalty.
        gameService.processCommand(gameId, "go north");

        assertEquals(11, gameState.getMoves());
        assertEquals(990, gameState.getMoveScore());
    }

    @Test
    void processCommandSavesScore() {
        String json = """
            {
                "name": "Space Adventure",
                "boss": "Alien",
                "story": "Test story",
                "loseBattleMessage": "You lost.",
                "startingRoom": "Starting Room",
                "rooms": [
                    {
                        "name": "Starting Room",
                        "itemDescription": "Starting room.",
                        "hasBoss": false,
                        "connectedRooms": {
                            "North": "Boss Room"
                        }
                    },
                    {
                        "name": "Boss Room",
                        "itemDescription": "The boss is here.",
                        "hasBoss": true,
                        "connectedRooms": {}
                    }
                ]
            }
            """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];
        Room bossRoom = theme.getRooms()[1];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom,
                bossRoom.getName(), bossRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        com.hoehn.game.entities.User user =
                new User();
        user.setUserName("testUser");

        when(userService.getMatchingUserName("testUser"))
                .thenReturn(java.util.Optional.of(user));

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        gameService.processCommand(startResult.gameId(), "go north");

        ArgumentCaptor<Score> scoreCaptor =
                ArgumentCaptor.forClass(Score.class);

        verify(scoreService)
                .createScore(scoreCaptor.capture());

        Score savedScore = scoreCaptor.getValue();

        assertEquals(user, savedScore.getUser());
        assertEquals("Space Adventure", savedScore.getTheme());
        assertEquals(1, savedScore.getMoves());
        assertEquals(0, savedScore.getScore());
    }

    @Test
    void processCommandGetWrongItem() {
        String json = """
        {
            "name": "Space Adventure",
            "boss": "Alien",
            "story": "Test story",
            "loseBattleMessage": "You lost.",
            "startingRoom": "Starting Room",
            "rooms": [
                {
                    "name": "Starting Room",
                    "item": "Key",
                    "itemDescription": "A key.",
                    "hasBoss": false,
                    "connectedRooms": {}
                }
            ]
        }
        """;

        Theme theme = new Gson().fromJson(json, Theme.class);

        Room startingRoom = theme.getRooms()[0];

        Map<String, Room> rooms = Map.of(
                startingRoom.getName(), startingRoom
        );

        when(gameWorldService.chooseTheme("space"))
                .thenReturn(theme);

        when(gameWorldService.createRooms(theme))
                .thenReturn(rooms);

        GameService gameService = createGameService();

        GameResponse startResult =
                gameService.startGame("testUser", "space");

        GameResponse result =
                gameService.processCommand(
                        startResult.gameId(),
                        "get sword"
                );

        assertEquals("That item is not here.", result.message());
        assertEquals(0, result.inventory().size());
        assertEquals("Key", startingRoom.getItem());
    }
}