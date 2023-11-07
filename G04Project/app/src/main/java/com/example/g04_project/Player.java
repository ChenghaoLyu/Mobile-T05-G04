package com.example.g04_project;

import java.io.Serializable;

public class Player implements Serializable {
    private String userId;
    private int avatar;
    private String roomId;
    private int team;
    private boolean isHost;
    private boolean isReady;
    private static final int TEAM_CAT = 1;
    private static final int TEAM_RAT = 2;

    public Player(String userId, String roomId, int team) {
        this.userId = userId;
        this.roomId = roomId;
        this.team = team;
        this.isHost = false;
        this.isReady = false;

        if (team == TEAM_CAT) {
            setAvatar(R.drawable.cat_avatar);
        } else if (team == TEAM_RAT){
            setAvatar(R.drawable.rat_avatar);
        }
    }

    public String getPlayerId() {
        return userId;
    }

    public void setPlayerId(String playerId) {
        this.userId = playerId;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public void setRoomID(String id) {
        this.roomId = id;
    }

    public String getRoomId() {
        return roomId;
    }
    public void setHost() { this.isHost = true; }
    public boolean isHost() {return isHost;}

    public void setTeam(int team) {
        this.team = team;
    }

    public int getTeam() {
        return team;
    }

    public void switchReadyStatus() {
        this.isReady = !getIsReady();
    }

    public boolean getIsReady() {
        return isReady;
    }




}
