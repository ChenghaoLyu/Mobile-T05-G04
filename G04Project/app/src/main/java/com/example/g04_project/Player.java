package com.example.g04_project;

import java.io.Serializable;

public class Player implements Serializable {
    private String userId;
    private String userName;
    private int avatar;
    private String roomId;
    private int team;
    private boolean isHost;
    private boolean isReady;
    private static final int TEAM_CAT = 1;
    private static final int TEAM_RAT = 2;

    public Player(){

    }

    public Player(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.isHost = false;
        this.isReady = false;
    }

    public String getPlayerId() {
        return userId;
    }

    public void setPlayerId(String playerId) {
        this.userId = playerId;
    }
    public String getPlayerName() {return userName;}
    public void setPlayerName(String userName) { this.userName = userName; }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int team) {
        if (team == TEAM_CAT) {
            this.avatar = R.drawable.cat_avatar_pixel;
        } else if (team == TEAM_RAT){
            this.avatar = R.drawable.rat_avatar_pixel;
        }
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
