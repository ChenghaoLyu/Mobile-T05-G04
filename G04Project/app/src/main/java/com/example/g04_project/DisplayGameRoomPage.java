package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.flexbox.FlexboxLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.util.concurrent.ConcurrentHashMap;

public class DisplayGameRoomPage extends AppCompatActivity {

    private WebSocketClient client;
    private Player player;
    private Button backButton;
    private TextView roomIdTextView;
    private Button readyButton;
    private Button startButton;
    private FlexboxLayout catsContainer;
    private FlexboxLayout ratsContainer;
    private RoomInformation currentRoom;
    private ConcurrentHashMap<String, Player> catPlayers;
    private ConcurrentHashMap<String, Player> ratPlayers;
    private static final int TEAM_CAT = 1;
    private static final int TEAM_RAT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_gameroom);
        MyApp app = (MyApp) getApplication();
        client = app.getWebSocketClient();

        backButton = findViewById(R.id.backButton);
        roomIdTextView = findViewById(R.id.roomIdTextView);
        readyButton = findViewById(R.id.readyButton);
        startButton = findViewById(R.id.startButton);
        catsContainer = findViewById(R.id.catsContainer);
        ratsContainer = findViewById(R.id.ratsContainer);

        // Get the current player and room
        player = PlayerManager.getInstance().getPlayer();
        currentRoom = RoomManager.getInstance().getRoom();

        roomIdTextView.setText(currentRoom.getRoomID());
        catPlayers = currentRoom.getCatPlayers();
        ratPlayers = currentRoom.getRatPlayers();

        // Display the players based on their team
        displayPlayers(catPlayers, catsContainer);
        if (currentRoom.getCurrentCat() < currentRoom.getRequiredCat()) {
            displayJoinTeamBtn(catsContainer, TEAM_CAT);
        }

        displayPlayers(ratPlayers, ratsContainer);
        if (currentRoom.getCurrentRat() < currentRoom.getRequiredRat()) {
            displayJoinTeamBtn(ratsContainer, TEAM_RAT);
        }

        backButton.setOnClickListener(v -> {
            player.setRoomID(null);
            onBackPressed();
        });

        readyButton.setOnClickListener(v -> {
            switchReadyStatus();
            refreshPlayerDisplay();
        });

        startButton.setOnClickListener(v -> {
            if (player.isHost()) {
                if (currentRoom.isFull()) { // 人齐了
                    if (currentRoom.getReadyList().size() // 全员准备
                            == currentRoom.getRequiredCat() + currentRoom.getRequiredRat()) {

                        currentRoom.setOngoing(true);
                        client.sendRoomInformation(currentRoom.getRoomID(), currentRoom.getHostId(),
                                currentRoom.getLocationName(),
                                currentRoom.getModeName(),
                                currentRoom.getDuration(),
                                currentRoom.getPassword(),
                                currentRoom.getRequiredCat(),
                                currentRoom.getCurrentCat(),
                                currentRoom.getRequiredRat(),
                                currentRoom.getCurrentRat(),
                                currentRoom.getStartTime(),
                                currentRoom.isPrivate(),
                                currentRoom.isOngoing(),
                                currentRoom.getCatPlayers(),
                                currentRoom.getRatPlayers(),
                                currentRoom.getReadyList());

                        RoomManager.getInstance().setRoom(currentRoom);
                        PlayerManager.getInstance().setPlayer(player);

                        Intent intent = new Intent(this, Create_gamestart_page.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(DisplayGameRoomPage.this,
                                "Some players are not ready! ",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DisplayGameRoomPage.this,
                            "The number of player is not enough! ",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                if (currentRoom.isOngoing()) {
                    RoomManager.getInstance().setRoom(currentRoom);
                    PlayerManager.getInstance().setPlayer(player);

                    Intent intent = new Intent(this, Create_gamestart_page.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DisplayGameRoomPage.this, "Only host can start the game!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayPlayers(ConcurrentHashMap<String, Player> players, FlexboxLayout container) {
        if (players != null) {
            for (Player player : players.values()) {
                addPlayerToContainer(player, container);
            }
        }
    }

    private void addPlayerToContainer(Player player, FlexboxLayout container) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View playerView = inflater.inflate(R.layout.item_player, null, false);

        // Setup player view based on the player details
        ImageView avatar = playerView.findViewById(R.id.playerAvatar);
        avatar.setImageResource(player.getAvatar());

        TextView playerName = playerView.findViewById(R.id.playerName);
        playerName.setText(player.getPlayerName());

        ImageView crown = playerView.findViewById(R.id.crown);
        if (player.isHost()) {
            crown.setVisibility(View.VISIBLE);
        }

        FrameLayout avatarFrame = playerView.findViewById(R.id.avatarFrame);
        if (player.getIsReady()) { // The background for ready state
            avatarFrame.setBackgroundResource(R.drawable.ready_status_background);
        } else { // The background for not-ready state
            avatarFrame.setBackgroundResource(R.drawable.not_ready_status_background);
        }

        // Add the player view to the container
        container.addView(playerView);
    }

    private void displayJoinTeamBtn(FlexboxLayout container, int team) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View joinTeamButtonView = inflater.inflate(R.layout.button_join_team, null, false);

        // Adding click listener for joining the team
        joinTeamButtonView.setOnClickListener(v -> {

            if (currentRoom.isFull()) {
                Toast.makeText(DisplayGameRoomPage.this,
                        "The room is already full, please choose another one.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Let the player join the team
            player.setTeam(team);
            player.setAvatar(team);

            if (team == TEAM_CAT) {
                currentRoom.joinCatTeam(player);
            } else if (team == TEAM_RAT) {
                currentRoom.joinRatTeam(player);
            }
            addPlayerToContainer(player, container);

            // Refresh the player list to reflect the addition
            refreshPlayerDisplay();
        });
        container.addView(joinTeamButtonView);
    }

    private void switchReadyStatus() {
        player.switchReadyStatus();
        currentRoom.addReadyList(player);
    }

    // Refresh the display of players based on their new ready status
    private void refreshPlayerDisplay() {
        catsContainer.removeAllViews();
        ratsContainer.removeAllViews();

        displayPlayers(catPlayers, catsContainer);
        if (currentRoom.getCurrentCat() < currentRoom.getRequiredCat()) {
            displayJoinTeamBtn(catsContainer, TEAM_CAT);
        }
        displayPlayers(ratPlayers, ratsContainer);
        if (currentRoom.getCurrentRat() < currentRoom.getRequiredRat()) {
            displayJoinTeamBtn(ratsContainer, TEAM_RAT);
        }

        client.sendRoomInformation(currentRoom.getRoomID(), currentRoom.getHostId(),
                currentRoom.getLocationName(),
                currentRoom.getModeName(),
                currentRoom.getDuration(),
                currentRoom.getPassword(),
                currentRoom.getRequiredCat(),
                currentRoom.getCurrentCat(),
                currentRoom.getRequiredRat(),
                currentRoom.getCurrentRat(),
                currentRoom.getStartTime(),
                currentRoom.isPrivate(),
                currentRoom.isOngoing(),
                currentRoom.getCatPlayers(),
                currentRoom.getRatPlayers(),
                currentRoom.getReadyList());
    }
}