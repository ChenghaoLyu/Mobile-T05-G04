package com.example.g04_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Create_gamestart_page extends AppCompatActivity implements OnMapReadyCallback {
    private TextView timerTextView;
    static float zoomLevel = 15.5f;
    private GoogleMap mymap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gamestart_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.game_start_map);
        mapFragment.getMapAsync(this);

        timerTextView = findViewById(R.id.timerTextView);

// 创建一个倒计时，例如10秒
        CountDownTimer countDownTimer = new CountDownTimer(1800000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int totalSeconds = (int) millisUntilFinished / 1000;
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;

                // 使用String.format来确保时间总是以两位数字的形式显示
                String timeFormat = String.format("%02d:%02d", minutes, seconds);
                timerTextView.setText(timeFormat);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00");
            }
        };

        countDownTimer.start();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mymap = googleMap;
        LatLng chosen_location = new LatLng(-37.7963, 144.9614);
        mymap.addMarker(new MarkerOptions().position(chosen_location).title("unimelb"));
        mymap.moveCamera(CameraUpdateFactory.newLatLng(chosen_location));
        mymap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
    }
}