package com.example.g04_project;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>{
    private Context context;
    private Player player;
    private List<RoomInformation> displayedRooms;
    private Map<String, Integer> modeSymbols;

    public RoomAdapter(Context context, List<RoomInformation> targetRooms, Player player) {
        this.context = context;
        this.player = player;
        setDisplayedRooms(targetRooms);
        modeSymbols = new HashMap<>();
        //TODO: upload mode images
        modeSymbols.put("classic", R.drawable.mode_image1);
        modeSymbols.put("zombie", R.drawable.mode_image2);
    }

    @NonNull
    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent,
                false);
        return new RoomViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomInformation currRoom = displayedRooms.get(position);
        holder.roomID.setText("Room ID: " + currRoom.getRoomID());
        holder.catCount.setText(": " + currRoom.getCurrentCat() + "/" + currRoom.getRequiredCat() +
                "    ");
        holder.mouseCount.setText(": " + currRoom.getCurrentRat() + "/" + currRoom.getRequiredRat());
        holder.location.setText("Location: " + currRoom.getLocationName());
        holder.startTime.setText("Start Time: " + currRoom.getStartTime());
        holder.duration.setText("Duration: " + currRoom.getDuration());
        if (currRoom.isPrivate()) {
            holder.privacy.setText("Privacy: private");
        } else {
            holder.privacy.setText("Privacy: public");
        }
        holder.gameModeImage.setImageResource(getModeSymbol(currRoom.getModeName()));

        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currRoom.isFull()) {
                    Toast.makeText(context, "The room is full, please choose another one.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currRoom.isPrivate()) {
                    showPasswordInputDialog(currRoom);
                } else {
                    navigateToDisplayRoomPage(currRoom);
                }
            }

            private void showPasswordInputDialog(RoomInformation room) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter Room Password");

                // 设置密码输入框
                final EditText passwordInput = new EditText(context);
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(passwordInput);

                builder.setPositiveButton("Join", (dialog, which) -> {
                    String enteredPassword = passwordInput.getText().toString();
                    if (enteredPassword.equals(room.getPassword())) {
                        // Correct password
                        navigateToDisplayRoomPage(room);
                    } else {
                        // Incorrect password
                        Toast.makeText(context, "Password incorrect, cannot join.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                builder.show();
            }

            private void navigateToDisplayRoomPage(RoomInformation room) {
                RoomManager.getInstance().setRoom(room);
                player.setRoomID(room.getRoomID());
                PlayerManager.getInstance().setPlayer(player);

                Intent intent = new Intent(context,DisplayGameRoomPage.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return displayedRooms.size();
    }

    public int getModeSymbol(String modeName) {
        return modeSymbols.get(modeName);
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomID;
        TextView catCount;
        TextView mouseCount;
        TextView location;
        TextView startTime;
        TextView duration;
        TextView privacy;
        ImageView gameModeImage;
        Button joinButton;

        public RoomViewHolder(View itemView) {
            super(itemView);
            this.roomID = itemView.findViewById(R.id.roomId);
            this.catCount = itemView.findViewById(R.id.catCount);
            this.mouseCount = itemView.findViewById(R.id.mouseCount);
            this.location = itemView.findViewById(R.id.location);
            this.startTime = itemView.findViewById(R.id.startTime);
            this.duration = itemView.findViewById(R.id.duration);
            this.privacy = itemView.findViewById(R.id.privacy);
            this.gameModeImage = itemView.findViewById(R.id.gameModeImage);
            this.joinButton = itemView.findViewById(R.id.joinButton);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateDisplayedRooms(List<RoomInformation> rooms) {
        setDisplayedRooms(rooms);
        notifyDataSetChanged();
    }

    private void setDisplayedRooms(List<RoomInformation> rooms) {
        displayedRooms = rooms;
    }
}
