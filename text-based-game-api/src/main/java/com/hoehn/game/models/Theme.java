package com.hoehn.game.models;

// This model holds the theme information

public class Theme {

    private String name;

    private String boss;

    private String story;

    private String loseBattleMessage;

    private String startingRoom;

    private Room[] rooms;

    public String getName() {
        return name;
    }

    public String getBoss() {
        return boss;
    }

    public String getStory() {
        return story;
    }

    public String getBattleLossMessage() {
        return loseBattleMessage;
    }

    public String getStartingRoom() {
        return startingRoom;
    }

    public Room[] getRooms() {
        return rooms;
    }
}
