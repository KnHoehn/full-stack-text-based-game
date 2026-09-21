package com.hoehn.game.models;

/**  This class creates a theme object. */
public class Theme {


    /** The name of the theme. */
    private String name;
    /** The theme's boss. */
    private String boss;
    /** The theme's story. */
    private String story;
    /** The theme's message to the user if they lose the battle against the boss. */
    private String loseBattleMessage;
    /** The room the player starts in for the theme. */
    private String startingRoom;
    /** The theme's rooms. */
    private Room[] rooms;

    /** Gets the name of the theme.
     *
     * @return the name of the theme.
     * */
    public String getName() {
        return name;
    }
    /** Gets the name of the boss for the theme.
     *
     * @return the name of the boss.
     * */
    public String getBoss() {
        return boss;
    }
    /** Get the theme's story.
     *
     * @return the story.
     * */
    public String getStory() {
        return story;
    }
    /** Gets the theme's message to the user if they lose the battle against the boss.
     *
     * @return the losing message.
     * */
    public String getBattleLossMessage() {
        return loseBattleMessage;
    }
    /** Gets the theme's starting room.
     *
     * @return the starting room.
     * */
    public String getStartingRoom() {
        return startingRoom;
    }
    /** Gets the theme's rooms.
     *
     * @return the rooms.
     * */
    public Room[] getRooms() {
        return rooms;
    }
}
