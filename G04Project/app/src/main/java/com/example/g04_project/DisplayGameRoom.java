package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.flexbox.FlexboxLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.util.concurrent.ConcurrentHashMap;

public class DisplayGameRoom extends AppCompatActivity {

    //TODO: Client or Player
    private Button backButton;
    private TextView roomIdTextView;
    private Button readyButton;
    private FlexboxLayout catsContainer;
    private FlexboxLayout ratsContainer;
    private RoomInformation currentRoom;
    private ConcurrentHashMap<String, Player> catsPlayers;
    private ConcurrentHashMap<String, Player> ratPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_gameroom);

        backButton = findViewById(R.id.backButton);
        roomIdTextView = findViewById(R.id.roomIdTextView);
        readyButton = findViewById(R.id.readyButton);
        catsContainer = findViewById(R.id.catsContainer);
        ratsContainer = findViewById(R.id.ratsContainer);

        //TODO: get room
        catsPlayers = currentRoom.getCatsPlayers();
        ratPlayers = currentRoom.getRatPlayers();

        // Display the players based on their team
        displayPlayers(catsPlayers, catsContainer);
        displayPlayers(ratPlayers, ratsContainer);

        //TODO: Actions clicking ready button
//        readyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchReadyStatus();
//                refreshPlayerDisplay();
//            }
//        });
    }

    private void displayPlayers(ConcurrentHashMap<String, Player> players, FlexboxLayout container) {
        for (Player player : players.values()) {
            addPlayerToContainer(player, container);
        }
    }

    private void addPlayerToContainer(Player player, FlexboxLayout container) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View playerView = inflater.inflate(R.layout.item_player, null, false);

        // Setup player view based on the player details
        ImageView avatar = playerView.findViewById(R.id.playerAvatar);
        avatar.setImageResource(player.getAvatar());

        TextView playerName = playerView.findViewById(R.id.playerName);
        playerName.setText(player.getPlayerId());  // Assuming the player's ID is also their display name

        //TODO: create xml for color change of the avatar frame
//        if (player.getIsReady()) { // The background for ready state
//            playerView.setBackgroundResource(R.drawable.ready_status_background);
//        } else { // The background for not-ready state
//            playerView.setBackgroundResource(R.drawable.not_ready_status_background);
//        }

        // Add the player view to the container
        container.addView(playerView);
    }

    // Refresh the display of players based on their new ready status
    private void refreshPlayerDisplay() {
        catsContainer.removeAllViews();
        ratsContainer.removeAllViews();

        displayPlayers(catsPlayers, catsContainer);
        displayPlayers(ratPlayers, ratsContainer);
    }
}