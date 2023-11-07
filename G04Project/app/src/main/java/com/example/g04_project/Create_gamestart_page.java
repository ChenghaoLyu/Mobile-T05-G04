package com.example.g04_project;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Create_gamestart_page extends AppCompatActivity implements OnMapReadyCallback {
    private TextView timerTextView;
    private WebSocketClient client;
    static float zoomLevel = 15.5f;
    private GoogleMap mymap;
    public List<LatLng> locations = new ArrayList<>();
    public ArrayList<Marker> markerList = new ArrayList<>();
//    public ConcurrentHashMap<String, Marker> marker_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gamestart_page);
        MyApp app = (MyApp) getApplication();
        client = app.getWebSocketClient();

//        marker_list = new ConcurrentHashMap<>();

        locations.add(new LatLng(-37.7990, 144.9594));
        locations.add(new LatLng(-37.7962, 144.9594));
        locations.add(new LatLng(-37.7963, 144.9614));

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
        try {
            // 使用 raw resource JSON 文件
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        LatLng chosen_location = new LatLng(-37.7963, 144.9614);
        List<LatLng> melbUniCorners = Arrays.asList(
                new LatLng(-37.7990, 144.9594),
                new LatLng(-37.7990, 144.9639),
                new LatLng(-37.7962, 144.9639),
                new LatLng(-37.7962, 144.9594)
        );

        // 在地图上绘制多边形
        mymap.addPolygon(new PolygonOptions()
                .addAll(melbUniCorners)
                .strokeColor(Color.RED)  // 边框颜色
                .fillColor(Color.argb(50, 255, 0, 0)));  // 填充颜色（半透明红色）

//        for (LatLng location : locations) {
//            Integer count = 0;
//            Marker marker = mymap.addMarker(new MarkerOptions().position(location).title("user" + String.valueOf(count)));
//            markerList.add(marker);
//        }
        mymap.addMarker(new MarkerOptions().position(chosen_location).title("unimelb"));
        mymap.moveCamera(CameraUpdateFactory.newLatLng(chosen_location));
        mymap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
    }
}