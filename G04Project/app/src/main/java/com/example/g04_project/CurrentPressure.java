package com.example.g04_project;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class CurrentPressure {
    private String userId;
    private double pressure;

    public CurrentPressure(String userId, double pressure) {
        this.userId = userId;
        this.pressure = pressure;
    }

    // Getter and Setter methods for each attribute
    public String getUserId() { return userId; }
    public void setUserId (String userId){
        this.userId =  userId;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    @Override
    public String toString() {
        return "CurrentPressure{" +
                "userID='" + userId + '\'' +
                ", currentPressure=" + pressure +
                '}';
    }
}
