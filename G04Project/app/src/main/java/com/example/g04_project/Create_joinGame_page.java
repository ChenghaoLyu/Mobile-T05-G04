package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Create_joinGame_page extends AppCompatActivity {
    private WebSocketClient client;
    private Player player;
    private Button backButton;
    private Button filterButton;
    private Button refreshButton;
    private CheckBox locationCheckBox;
    private CheckBox gameModeCheckBox;
    private LinearLayout locationOptions;
    private LinearLayout gameModeOptions;
    private CheckBox location1CheckBox;
    private CheckBox location2CheckBox;
    private CheckBox location3CheckBox;
    private CheckBox mode1CheckBox;
    private CheckBox mode2CheckBox;
    private TextView emptyMessageTextView;
    private RecyclerView roomsRecyclerView;
    private RoomAdapter roomAdapter;
    private ConcurrentHashMap<String, RoomInformation> rooms = new ConcurrentHashMap<>();
    private List<RoomInformation> targetRooms;
    private SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_joingame_page);
        MyApp app = (MyApp) getApplication();
        client = app.getWebSocketClient();
        player = new Player(client.getAccount().getUserID(), client.getAccount().getUserName());

        backButton = findViewById(R.id.backButton);
        filterButton = findViewById(R.id.filterButton);
        refreshButton = findViewById(R.id.refreshButton);

        locationCheckBox = findViewById(R.id.locationCheckBox);
        gameModeCheckBox = findViewById(R.id.gameModeCheckBox);
        locationOptions = findViewById(R.id.locationOptions);
        gameModeOptions = findViewById(R.id.gameModeOptions);
        location1CheckBox = findViewById(R.id.location1);
        location2CheckBox = findViewById(R.id.location2);
        location3CheckBox = findViewById(R.id.location3);
        mode1CheckBox = findViewById(R.id.mode1);
        mode2CheckBox = findViewById(R.id.mode2);
        emptyMessageTextView = findViewById(R.id.emptyMessageTextView);
        roomsRecyclerView = findViewById(R.id.roomsRecyclerview);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);

        client.sendGetRoomsRequest(client.getAccount().getUserID());
        client.setOnMessageReceivedListener(message -> {
            // Successful registration
            System.out.println(message);
            if (message.equals("All updated rooms received")) {
                System.out.println(message + "-----------");

                //rooms = client.getAllRooms().getRooms();
                for (String id : client.getAllRooms().getRooms().keySet()) {
                    rooms.put(id, client.getAllRooms().getRooms().get(id));
                    System.out.println(id);
                    System.out.println("currCat: " + rooms.get(id).getCurrentCat());
                }
                System.out.println("error3");

                System.out.println("-------");

//            }
                ConcurrentHashMap<String, Player> catPlayers1 = new ConcurrentHashMap<>();
                ConcurrentHashMap<String, Player> ratPlayers1 = new ConcurrentHashMap<>();
                ConcurrentHashMap<String, Player> readyList1 = new ConcurrentHashMap<>();

                Player player1Room1 = new Player("User1", "Alice");
                //player = new Player("User2", "Bob");

                Player player1Room2 = new Player("User3", "Charlie");
                Player player2Room2 = new Player("User4", "Danielle");

                player1Room1.setHost();
                player1Room1.setTeam(2);
                player1Room1.setAvatar(2);
                ratPlayers1.put("User1", player1Room1);

                RoomInformation room1 = new RoomInformation(
                        "000003",
                        "HostUser1",
                        "Unimelb",
                        "Classic",
                        "30",
                        "123456",
                        1,
                        0,
                        2,
                        1,
                        "2023-11-09T20:00:00",
                        true,
                        false,
                        catPlayers1,
                        ratPlayers1,
                        readyList1
                );
                ConcurrentHashMap<String, Player> catPlayers2 = new ConcurrentHashMap<>();
                ConcurrentHashMap<String, Player> ratPlayers2 = new ConcurrentHashMap<>();
                ConcurrentHashMap<String, Player> readyList2 = new ConcurrentHashMap<>();

                player1Room2.setHost();
                player1Room2.switchReadyStatus();
                player1Room2.setTeam(1);
                player1Room2.setAvatar(1);
                player2Room2.setTeam(2);
                player2Room2.setAvatar(2);
                catPlayers2.put("User3", player1Room2);
                ratPlayers2.put("User4", player2Room2);
                RoomInformation room2 = new RoomInformation(
                        "000002",
                        "HostUser2",
                        "Monash",
                        "Zombie",
                        "45",
                        "",
                        2,
                        1,
                        3,
                        1,
                        "2023-11-09T21:00:00",
                        false,
                        false,
                        catPlayers2,
                        ratPlayers2,
                        readyList2
                );

                rooms.put("000003", room1);
                rooms.put("000002", room2);
                System.out.println(rooms.size());
                System.out.println(rooms.get("777777").getRoomId());

                //});
                System.out.println("error1");

                //rooms = new ConcurrentHashMap<>();
                //rooms = client.getAllRooms().getRooms();
                //TODO: get rooms

                targetRooms = new ArrayList<>(rooms.values());
                System.out.println("aaaa");
                System.out.println(targetRooms.size());
                System.out.println("aaaaaa");

                if (!rooms.isEmpty()) {
                    System.out.println("woyouroom");
                    roomsRecyclerView.setVisibility(View.VISIBLE);
                    emptyMessageTextView.setVisibility(View.GONE);
                }

                roomAdapter = new RoomAdapter(this, targetRooms, player);
                roomsRecyclerView.setAdapter(roomAdapter);

                // Actions clicking on back button
                backButton.setOnClickListener(v -> finish());

                // Actions clicking on refresh button
                refreshButton.setOnClickListener(v -> {
                    //rooms = new ConcurrentHashMap<>();
                    //TODO: get rooms
                    //rooms = client.getAllRooms().getRooms();

                    System.out.println("get rooms successfully");

                    targetRooms = new ArrayList<>(rooms.values());
                    roomAdapter.updateDisplayedRooms(targetRooms);
                });

                ConstraintLayout.LayoutParams layoutParams =
                        (ConstraintLayout.LayoutParams) roomsRecyclerView.getLayoutParams();

                // Actions clicking on filter button
                filterButton.setOnClickListener(v -> {
                    if (locationCheckBox.getVisibility() == View.GONE) {
                        locationCheckBox.setVisibility(View.VISIBLE);
                        gameModeCheckBox.setVisibility(View.VISIBLE);
                        layoutParams.topToBottom = R.id.locationCheckBox;
                    } else {
                        locationCheckBox.setChecked(false);
                        gameModeCheckBox.setChecked(false);
                        locationCheckBox.setVisibility(View.GONE);
                        gameModeCheckBox.setVisibility(View.GONE);
                        layoutParams.topToBottom = R.id.filterButton;
                    }
                });

                // Actions clicking on checkBoxes
                locationCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        locationOptions.setVisibility(View.VISIBLE);
                        layoutParams.topToBottom = R.id.locationOptions;
                    } else {
                        location1CheckBox.setChecked(false);
                        location2CheckBox.setChecked(false);
                        location3CheckBox.setChecked(false);
                        locationOptions.setVisibility(View.GONE);
                        layoutParams.topToBottom = R.id.locationCheckBox;
                    }
                });

                gameModeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        gameModeOptions.setVisibility(View.VISIBLE);
                        layoutParams.topToBottom = R.id.gameModeOptions;
                    } else {
                        mode1CheckBox.setChecked(false);
                        mode2CheckBox.setChecked(false);
                        gameModeOptions.setVisibility(View.GONE);
                        layoutParams.topToBottom = R.id.gameModeOptions;
                    }
                });

                // Action clicking on option buttons
                CompoundButton.OnCheckedChangeListener filterListener = (buttonView, isChecked) -> {
                    applyFilters();
                };
                location1CheckBox.setOnCheckedChangeListener(filterListener);
                location2CheckBox.setOnCheckedChangeListener(filterListener);
                location3CheckBox.setOnCheckedChangeListener(filterListener);
                mode1CheckBox.setOnCheckedChangeListener(filterListener);
                mode2CheckBox.setOnCheckedChangeListener(filterListener);

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
        });
        System.out.println("finish");
    }

    // Apply filters
    private void applyFilters() {
        targetRooms.clear();

        // Check for each button
        boolean isLocation1Checked = location1CheckBox.isChecked();
        boolean isLocation2Checked = location2CheckBox.isChecked();
        boolean isLocation3Checked = location3CheckBox.isChecked();
        boolean isMode1Checked = mode1CheckBox.isChecked();
        boolean isMode2Checked = mode2CheckBox.isChecked();

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
                matchesLocation = (room.getLocationName().equals("Unimelb") && isLocation1Checked)
                        || (room.getLocationName().equals("Monash") && isLocation2Checked)
                        || (room.getLocationName().equals("Central") && isLocation3Checked);
            }

            // Check game mode
            if (isAnyModeChecked) {
                matchesMode = (room.getModeName().equals("Classic") && isMode1Checked)
                        || (room.getModeName().equals("Zombie") && isMode2Checked);
            }

            // Add the satisfied room to the displayed list
            if (matchesLocation && matchesMode) {
                targetRooms.add(room);
            }

        }
        if (!targetRooms.isEmpty()) {
            roomAdapter.updateDisplayedRooms(targetRooms);
        } else {
            displayToast("Room not found");
            //Toast.makeText(this, "Room not found", Toast.LENGTH_SHORT).show();
        }
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
            displayToast("Room not found");
            //Toast.makeText(this, "Room not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayToast(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View customToastView = inflater.inflate(R.layout.item_toast, null);

        TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
        customToastTextView.setText(msg);

        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
        customToast.setView(customToastView);

        customToast.setGravity(Gravity.CENTER, 0, 700);
        customToast.show();
    }

}