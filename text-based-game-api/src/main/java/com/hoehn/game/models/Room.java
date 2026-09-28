package com.hoehn.game.models;

import java.util.Map;

// Model that holds the room information

public class Room {

    private String name;

    private String item;

    private String itemDescription;

    private boolean hasBoss;

    private Map<String, String> connectedRooms;

    public String getName() {
        return name;
    }

    public String getItem() {
        return item;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Map<String, String> getConnectedRooms() {
        return connectedRooms;
    }

    public boolean getBoss() {
        return hasBoss;
    }

    public void setItem(final String item) {
        this.item = item;
    }

    public void setItemDescription(final String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
