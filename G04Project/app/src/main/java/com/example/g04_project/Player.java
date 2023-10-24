package com.example.g04_project;

public class Player {
    private String playerId;
    private int avatar;
    private String roomId;
    private int team;
    private boolean isHost;
    private boolean isReady;
    private static final int TEAM_CAT = 1;
    private static final int TEAM_RAT = 2;

    public Player(String playerId, String roomId, int team) {
        this.playerId = playerId;
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
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
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
