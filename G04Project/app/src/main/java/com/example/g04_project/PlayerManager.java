package com.example.g04_project;

public class PlayerManager {
    private static PlayerManager instance;
    private Player player;

    private PlayerManager() {
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
