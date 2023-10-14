package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.concurrent.ConcurrentHashMap;

public class Create_joinGame_page extends AppCompatActivity {

    private CheckBox locationCheckBox;
    private CheckBox gameModeCheckBox;
    private Button filterButton;
    private LinearLayout locationOptions;
    private LinearLayout gameModeOptions;
    private RecyclerView roomsRecyclerView;
    private RoomAdapter roomAdapter;
    private ConcurrentHashMap<String, RoomInformation> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_game_page);

        locationCheckBox = findViewById(R.id.locationCheckBox);
        gameModeCheckBox = findViewById(R.id.gameModeCheckBox);
        filterButton = findViewById(R.id.filterButton);
        locationOptions = findViewById(R.id.locationOptions);
        gameModeOptions = findViewById(R.id.gameModeOptions);
        roomsRecyclerView = findViewById(R.id.roomsRecyclerview);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        rooms = new ConcurrentHashMap<>();
        //TODO: get rooms
        roomAdapter = new RoomAdapter(rooms);
        roomsRecyclerView.setAdapter(roomAdapter);

        // Actions clicking on filter button
        filterButton.setOnClickListener(v -> {
            if (locationCheckBox.getVisibility() == View.GONE) {
                locationCheckBox.setVisibility(View.VISIBLE);
                gameModeCheckBox.setVisibility(View.VISIBLE);
            }else {
                locationCheckBox.setVisibility(View.GONE);
                gameModeCheckBox.setVisibility(View.GONE);
            }
        });

        // Actions clicking on checkBox
        locationCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                locationOptions.setVisibility(View.VISIBLE);
            } else {
                locationOptions.setVisibility(View.GONE);
            }
        });

        gameModeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gameModeOptions.setVisibility(View.VISIBLE);
            } else {
                gameModeOptions.setVisibility(View.GONE);
            }
        });



    }


}