package com.hoehn.game.models;

import java.util.Map;

/** This class creates Room objects. */
public class Room {

    /** The name of the room. */
    private String name;
    /** The name of the item in the room.*/
    private String item;
    /** The description of the item in the room. */
    private String itemDescription;
    /** The status of the boss residing in the room. */
    private boolean hasBoss;
    /** The room connections. */
    private Map<String, String> connectedRooms;

    /** Gets the name of the room.
     *
     * @return the name.
     * */
    public String getName() {
        return name;
    }
    /** Gets the item in the room.
     *
     * @return the item.
     * */
    public String getItem() {
        return item;
    }
    /** Gets the description of the item in the room.
     *
     * @return the item description.
     * */
    public String getItemDescription() {
        return itemDescription;
    }
    /** Gets the room's connected rooms
     *
     * @return a map of the connected rooms.
     * */
    public Map<String, String> getConnectedRooms() {
        return connectedRooms;
    }
    /** Gets room's boss status.
     *
     * @return the boss status.
     * */
    public boolean getBoss() {
        return hasBoss;
    }
    /** Sets the item for the room.
     *
     * @param item the item to be placed in the room.
     * */
    public void setItem(final String item) {
        this.item = item;
    }

    public void setItemDescription(final String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
