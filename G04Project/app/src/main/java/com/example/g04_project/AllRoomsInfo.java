package com.example.g04_project;

import java.util.concurrent.ConcurrentHashMap;

public class AllRoomsInfo {
    private ConcurrentHashMap<String, RoomInformation> rooms;
    public ConcurrentHashMap<String, RoomInformation> getRooms() { return rooms;}
}
