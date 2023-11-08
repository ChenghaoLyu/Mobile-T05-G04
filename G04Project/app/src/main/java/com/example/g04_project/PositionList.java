package com.example.g04_project;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PositionList {
    private List<String> userId;
    private List<List<Double>> position;

    public void setUserID(List<String> userId) {
        this.userId = userId;
    }

    public void setPosition(List<List<Double>> position) {
        this.position = position;
    }

    public List<String> getUserID() {
        return userId;
    }

    public List<List<Double>> getPosition() {
        return position;
    }

}
