package com.example.g04_project;

import java.time.LocalTime;

public class RoomInformation {
    private String userId, locationName, modeName, duration, password;
    private int playerNumber, catNumber,ratNumber;
    private LocalTime startTime;
    private boolean privacy;

    public RoomInformation(String userId, String locationName, String modeName, String duration, String password,
                           int playerNumber, int catNumber, int ratNumber,
                           LocalTime startTime, boolean privacy) {
        this.userId = userId;
        this.locationName = locationName;
        this.modeName = modeName;
        this.duration = duration;
        this.password = password;
        this.playerNumber = playerNumber;
        this.catNumber = catNumber;
        this.ratNumber = ratNumber;
        this.startTime = startTime;
        this.privacy = privacy;
    }

    // Getter and Setter methods for each attribute

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getCatNumber() {
        return catNumber;
    }

    public void setCatNumber(int catNumber) {
        this.catNumber = catNumber;
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

    public int getRatNumber() {
        return ratNumber;
    }

    public void setRatNumber(int ratNumber) {
        this.ratNumber = ratNumber;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "RoomInformation{" +
                "userId='" + userId + '\'' +
                ", locationName=" + locationName +
                ", modeName=" + modeName +
                ", duration=" + duration +
                ", password=" + password +
                ", playerNumber=" + playerNumber +
                ", catNumber=" + catNumber +
                ", ratNumber=" + ratNumber +
                ", startTime=" + startTime +
                ", privacy=" + privacy +
                '}';
    }
}
