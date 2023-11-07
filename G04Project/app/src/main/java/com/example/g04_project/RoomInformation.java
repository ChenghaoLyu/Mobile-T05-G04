package com.example.g04_project;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

public class RoomInformation implements Serializable {
    private String roomID, hostId, locationName, modeName, duration, password;

    //TODO: playerNumber
    private int playerNumber, requiredCat, currentCat, requiredRat, currentRat;
    private ConcurrentHashMap<String, Player> catPlayers, ratPlayers, readyList;
    private String startTime;
    private boolean privacy;

    public RoomInformation(String roomID, String hostId, String locationName, String modeName, String duration, String password,
                           int requiredCat, int currentCat, int requiredRat, int currentRat,
                           String startTime, boolean privacy,
                           ConcurrentHashMap<String, Player> catPlayers,
                           ConcurrentHashMap<String, Player> ratPlayers,
                           ConcurrentHashMap<String, Player> readyList) {
        this.roomID = roomID;
        this.hostId = hostId;
        this.locationName = locationName;
        this.modeName = modeName;
        this.duration = duration;
        this.password = password;
        this.requiredCat = requiredCat;
        this.currentCat = currentCat;
        this.requiredRat = requiredRat;
        this.currentRat = currentRat;
        this.catPlayers = catPlayers;
        this.ratPlayers = ratPlayers;
        this.readyList = readyList;
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
    public ConcurrentHashMap<String, Player> getCatPlayers() { return catPlayers;}
    public ConcurrentHashMap<String, Player> getRatPlayers() { return ratPlayers;}
    public ConcurrentHashMap<String, Player> getReadyList() { return readyList;}


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void addReadyList(Player player) {
        readyList.put(player.getPlayerId(), player);
    }

    public void joinCatTeam(Player player) {
        catPlayers.put(player.getPlayerId(), player);
        if (ratPlayers.containsKey(player.getPlayerId())) {
            ratPlayers.remove(player.getPlayerId(), player);
        }
    }

    public void joinRatTeam(Player player) {
        ratPlayers.put(player.getPlayerId(), player);
        if (catPlayers.containsKey(player.getPlayerId())) {
            catPlayers.remove(player.getPlayerId(), player);
        }
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
                ", requiredCat=" + requiredCat +
                ", requiredRat=" + requiredRat +
                ", currentCat=" + currentCat +
                ", currentRat=" + currentRat +
                ", cat_list=" + catPlayers +
                ", rat_list=" + ratPlayers +
                ", ready_list=" + readyList +
                ", startTime=" + startTime +
                ", isPrivate=" + privacy +
                '}';
    }
}
