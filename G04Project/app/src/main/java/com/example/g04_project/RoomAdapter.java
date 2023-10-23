package com.example.g04_project;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>{
    private List<RoomInformation> displayedRooms;
    private Map<String, Integer> modeSymbols;

    public RoomAdapter(List<RoomInformation> targetRooms) {
        setDisplayedRooms(targetRooms);
        modeSymbols = new HashMap<>();
        //TODO: upload mode images
        //modeSymbols.put("modeName1", R.drawable.modeImage1);
        //modeSymbols.put("modeName2", R.drawable.modeImage2);
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
        holder.gameModeImage.setImageResource(getModeSymbol(currRoom.getModeName()));
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
        ImageView gameModeImage;

        public RoomViewHolder(View itemView) {
            super(itemView);
            this.roomID = itemView.findViewById(R.id.roomId);
            this.catCount = itemView.findViewById(R.id.catCount);
            this.mouseCount = itemView.findViewById(R.id.mouseCount);
            this.location = itemView.findViewById(R.id.location);
            this.startTime = itemView.findViewById(R.id.startTime);
            this.duration = itemView.findViewById(R.id.duration);
            this.gameModeImage = itemView.findViewById(R.id.gameModeImage);
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
