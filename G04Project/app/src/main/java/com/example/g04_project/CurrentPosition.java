package com.example.g04_project;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class CurrentPosition {
    private String userId;
    private LatLng location;

    public CurrentPosition(String userId, LatLng location) {
        this.userId = userId;
        this.location = location;
    }

    // Getter and Setter methods for each attribute
    public String getUserId() { return userId; }
    public void setUserId (String userId){
        this.userId =  userId;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
    @Override
    public String toString() {
        return "CurrentPosition{" +
                "userID='" + userId + '\'' +
                ", location=" + location +
                '}';
    }
}
