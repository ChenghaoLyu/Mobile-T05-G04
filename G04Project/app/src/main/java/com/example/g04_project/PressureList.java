package com.example.g04_project;


import java.util.List;

public class PressureList {
    private List<String> userId;
    private List<Integer> pressure;

    public void setUserID(List<String> userId) {
        this.userId = userId;
    }

    public void setPosition(List<Integer> pressure) {
        this.pressure = pressure;
    }

    public List<String> getUserID() {
        return userId;
    }

    public List<Integer> getPressure() {
        return pressure;
    }

}
