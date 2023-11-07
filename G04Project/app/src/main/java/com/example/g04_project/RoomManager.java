package com.example.g04_project;

public class RoomManager {
    private static RoomManager instance;
    private RoomInformation room;

    private RoomManager() {
    }

    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public RoomInformation getRoom() {
        return room;
    }

    public void setRoom(RoomInformation room) {
        this.room = room;
    }
}
