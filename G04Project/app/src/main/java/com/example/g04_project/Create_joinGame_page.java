package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Create_joinGame_page extends AppCompatActivity {

    private Button filterButton;
    private CheckBox locationCheckBox;
    private CheckBox gameModeCheckBox;
    private LinearLayout locationOptions;
    private LinearLayout gameModeOptions;
    private RadioButton location1RadioButton;
    private RadioButton location2RadioButton;
    private RadioButton location3RadioButton;
    private RadioButton mode1RadioButton;
    private RadioButton mode2RadioButton;
    private TextView emptyMessageTextView;
    private RecyclerView roomsRecyclerView;
    private RoomAdapter roomAdapter;
    private ConcurrentHashMap<String, RoomInformation> rooms;
    private List<RoomInformation> targetRooms;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_game_page);

        filterButton = findViewById(R.id.filterButton);
        locationCheckBox = findViewById(R.id.locationCheckBox);
        gameModeCheckBox = findViewById(R.id.gameModeCheckBox);
        locationOptions = findViewById(R.id.locationOptions);
        gameModeOptions = findViewById(R.id.gameModeOptions);
        location1RadioButton = findViewById(R.id.location1);
        location2RadioButton = findViewById(R.id.location2);
        location3RadioButton = findViewById(R.id.location3);
        mode1RadioButton = findViewById(R.id.mode1);
        mode2RadioButton = findViewById(R.id.mode2);
        emptyMessageTextView = findViewById(R.id.emptyMessageTextView);
        roomsRecyclerView = findViewById(R.id.roomsRecyclerview);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);

        rooms = new ConcurrentHashMap<>();
        targetRooms = new ArrayList<>();
        //TODO: get rooms

        if (rooms.isEmpty()) {
            return;
        }

        roomsRecyclerView.setVisibility(View.VISIBLE);
        emptyMessageTextView.setVisibility(View.GONE);

        roomAdapter = new RoomAdapter(rooms);
        roomsRecyclerView.setAdapter(roomAdapter);

        // Actions clicking on filter button
        filterButton.setOnClickListener(v -> {
            if (locationCheckBox.getVisibility() == View.GONE) {
                locationCheckBox.setVisibility(View.VISIBLE);
                gameModeCheckBox.setVisibility(View.VISIBLE);
            }else {
                locationCheckBox.setChecked(false);
                gameModeCheckBox.setChecked(false);
                locationCheckBox.setVisibility(View.GONE);
                gameModeCheckBox.setVisibility(View.GONE);
            }
        });

        // Actions clicking on checkBoxes
        locationCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                locationOptions.setVisibility(View.VISIBLE);
            } else {
                location1RadioButton.setChecked(false);
                location2RadioButton.setChecked(false);
                location3RadioButton.setChecked(false);
                locationOptions.setVisibility(View.GONE);
            }
        });

        gameModeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gameModeOptions.setVisibility(View.VISIBLE);
            } else {
                mode1RadioButton.setChecked(false);
                mode1RadioButton.setChecked(false);
                gameModeOptions.setVisibility(View.GONE);
            }
        });

        // Action clicking on option buttons
        CompoundButton.OnCheckedChangeListener filterListener = (buttonView, isChecked) -> {
            applyFilters();
        };
        location1RadioButton.setOnCheckedChangeListener(filterListener);
        location2RadioButton.setOnCheckedChangeListener(filterListener);
        location3RadioButton.setOnCheckedChangeListener(filterListener);
        mode1RadioButton.setOnCheckedChangeListener(filterListener);
        mode2RadioButton.setOnCheckedChangeListener(filterListener);

        // Actions entering and submitting id in searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String id) {
                searchRoom(id);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    // Apply filters
    private void applyFilters() {
        targetRooms.clear();

        // Check for each button
        boolean isLocation1Checked = location1RadioButton.isChecked();
        boolean isLocation2Checked = location2RadioButton.isChecked();
        boolean isLocation3Checked = location3RadioButton.isChecked();
        boolean isMode1Checked = mode1RadioButton.isChecked();
        boolean isMode2Checked = mode2RadioButton.isChecked();

        // Check if any option button is checked
        boolean isAnyLocationChecked = isLocation1Checked
                || isLocation2Checked
                || isLocation3Checked;
        boolean isAnyModeChecked = isMode1Checked || isMode2Checked;

        // Display all rooms if no option is checked
        if (!isAnyLocationChecked && !isAnyModeChecked) {
            targetRooms.addAll(rooms.values());
            roomAdapter.updateDisplayedRooms(targetRooms);
            return;
        }

        // Check if each room meets the conditions
        for (RoomInformation room : rooms.values()) {

            // Ignore the category if not checked
            boolean matchesLocation = !isAnyLocationChecked;
            boolean matchesMode = !isAnyModeChecked;

            // Check location
            if (isAnyLocationChecked) {
                matchesLocation = (room.getLocationName().equals("Location 1") && isLocation1Checked)
                        || (room.getLocationName().equals("Location 2") && isLocation2Checked)
                        || (room.getLocationName().equals("Location 3") && isLocation3Checked);
            }

            // Check game mode
            if (isAnyModeChecked) {
                matchesMode = (room.getModeName().equals("Mode 1") && isMode1Checked)
                        || (room.getModeName().equals("Mode 2") && isMode2Checked);
            }

            // Add the satisfied room to the displayed list
            if (matchesLocation && matchesMode) {
                targetRooms.add(room);
            }

        }
        roomAdapter.updateDisplayedRooms(targetRooms);
    }

    // Search and display the room
    private void searchRoom(String id) {
        targetRooms.clear();
        boolean roomFound = rooms.computeIfPresent(id, (key, room) -> {
            targetRooms.add(room);
            roomAdapter.updateDisplayedRooms(targetRooms);
            return room;
        }) != null;

        if (!roomFound) {
            Toast.makeText(this, "Room not found", Toast.LENGTH_SHORT).show();
        }
    }

}