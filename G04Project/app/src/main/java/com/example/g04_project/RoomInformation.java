package com.example.g04_project;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

public class RoomInformation {
    private String roomID, hostId, locationName, modeName, duration, password;

    //TODO: playerNumber
    private int playerNumber, requiredCat, currentCat, requiredRat, currentRat;
    private ConcurrentHashMap<String, Player> catsPlayers;
    private ConcurrentHashMap<String, Player> ratPlayers;
    private LocalTime startTime;
    private boolean privacy;

    public RoomInformation(String roomID, String hostId, String locationName, String modeName, String duration, String password,
                           int playerNumber, int requiredCat, int currentCat, int requiredRat, int currentRat,
                           LocalTime startTime, boolean privacy) {
        this.roomID = roomID;
        this.hostId = hostId;
        this.locationName = locationName;
        this.modeName = modeName;
        this.duration = duration;
        this.password = password;
        this.playerNumber = playerNumber;
        this.requiredCat = requiredCat;
        this.currentCat = currentCat;
        this.requiredRat = requiredRat;
        this.currentRat = currentRat;
        this.catsPlayers = new ConcurrentHashMap<>();
        this.ratPlayers = new ConcurrentHashMap<>();
        this.startTime = startTime;
        this.privacy = privacy;
    }

    // Getter and Setter methods for each attribute
    public String getRoomID() { return roomID; }
    public void setRoomID(String roomID) { this.roomID = roomID; }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public String getModeName(){
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getRequiredCat() { return requiredCat; }
    public void setRequiredCat(int requiredCat) { this.requiredCat = requiredCat; }

    public int getCurrentCat() {
        return currentCat;
    }

    public void setCurrentCat(int currCat) {
        this.currentCat = currCat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }
    public int getRequiredRat() { return requiredRat; }
    public void setRequiredRat(int requiredRat) { this.requiredRat = requiredRat; }

    public int getCurrentRat() {
        return currentRat;
    }

    public void setCurrentRat(int currentRat) {
        this.currentRat = currentRat;
    }
    public ConcurrentHashMap<String, Player> getCatsPlayers() { return catsPlayers;}
    public ConcurrentHashMap<String, Player> getRatPlayers() { return ratPlayers;}

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public boolean isFull() {
        if (currentRat + currentRat < requiredCat + requiredRat) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "RoomInformation{" +
                "roomID='" + roomID + '\'' +
                ", hostId='" + hostId + '\'' +
                ", locationName=" + locationName +
                ", modeName=" + modeName +
                ", duration=" + duration +
                ", password=" + password +
                ", playerNumber=" + playerNumber +
                ", requiredCat=" + requiredCat +
                ", requiredRat=" + requiredRat +
                ", currentCat=" + currentCat +
                ", currentRat=" + currentRat +
                ", startTime=" + startTime +
                ", privacy=" + privacy +
                '}';
    }
}
