package com.example.g04_project;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StampStyle;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import java.util.UUID;


public class WebSocketClient {

    private OkHttpClient client;
    private WebSocket webSocket;
    private OnMessageReceivedListener listener;
    private Handler handler = new Handler();
    private String uniqueID = UUID.randomUUID().toString();
    public Account account = new Account();
    public RoomInformation room, updatedRoom;
    public AllRoomsInfo rooms = new AllRoomsInfo();
    public PositionList positionList = new PositionList();
    public PressureList pressureList = new PressureList();

    private Runnable locationUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            sendLocationUpdate();
            handler.postDelayed(this, 30000); // 30 seconds
        }
    };
    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
        this.listener = listener;
    }

    private void sendLocationUpdate() {
        // 获取位置信息（这里只是一个示例，你可能需要从GPS或其他位置服务获取实际的位置数据）
        double latitude = 40.7128;
        double longitude = -74.0060;
        long timestamp = System.currentTimeMillis();

        Map<String, Object> locationData = new HashMap<>();
        locationData.put("user_id", uniqueID);
        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);
        locationData.put("timestamp", timestamp);

        sendMessage("user_location", locationData);
    }

    public void sendValidation(String email, String password) {

        Map<String, Object> userInformation = new HashMap<>();
        userInformation.put("email", email);
        userInformation.put("password", password);

        sendMessage("validation", userInformation);
    }

    public void sendRoomInformation(String roomId, String userId, String locationName, String modeName, String duration, String password,
                                     int catNumber, int currCatNum, int ratNumber, int currRatNum,
                                     String startTime, boolean isPrivate, boolean isOngoing,
                                     ConcurrentHashMap<String, Player> cat_list,
                                     ConcurrentHashMap<String, Player> rat_list,
                                     ConcurrentHashMap<String, Player> ready_list) {
        // 获取位置信息（这里只是一个示例，你可能需要从GPS或其他位置服务获取实际的位置数据）

        Map<String, Object> roomInformation = new HashMap<>();
        roomInformation.put("roomId", roomId);
        roomInformation.put("hostId", userId);
        roomInformation.put("locationName", locationName);
        roomInformation.put("modeName", modeName);
        roomInformation.put("duration", duration);
        roomInformation.put("password", password);
        roomInformation.put("requiredCat", catNumber);
        roomInformation.put("requiredRat", ratNumber);
        roomInformation.put("currentCat", currCatNum);
        roomInformation.put("currentRat", currRatNum);
        roomInformation.put("catPlayers", cat_list);
        roomInformation.put("ratPlayers", rat_list);
        roomInformation.put("readyList", ready_list);
        roomInformation.put("startTime", startTime);
        roomInformation.put("isPrivate", isPrivate);
        roomInformation.put("isOnGoing", isOngoing);
        System.out.println("{{{{{{{}}}}}}}");
        System.out.println(roomInformation.get("startTime"));
        System.out.println(roomInformation.get("hostId"));
        sendMessage("room_information", roomInformation);
    }

    public void sendUpdateRoomInformation(String roomId, String userId, String locationName, String modeName, String duration, String password,
                                    int catNumber, int currCatNum, int ratNumber, int currRatNum,
                                    String startTime, boolean isPrivate, boolean isOngoing,
                                    ConcurrentHashMap<String, Player> cat_list,
                                    ConcurrentHashMap<String, Player> rat_list,
                                    ConcurrentHashMap<String, Player> ready_list) {
        // 获取位置信息（这里只是一个示例，你可能需要从GPS或其他位置服务获取实际的位置数据）

        Map<String, Object> roomInformation = new HashMap<>();
        roomInformation.put("roomId", roomId);
        roomInformation.put("hostId", userId);
        roomInformation.put("locationName", locationName);
        roomInformation.put("modeName", modeName);
        roomInformation.put("duration", duration);
        roomInformation.put("password", password);
        roomInformation.put("requiredCat", catNumber);
        roomInformation.put("requiredRat", ratNumber);
        roomInformation.put("currentCat", currCatNum);
        roomInformation.put("currentRat", currRatNum);
        roomInformation.put("catPlayers", cat_list);
        roomInformation.put("ratPlayers", rat_list);
        roomInformation.put("readyList", ready_list);
        roomInformation.put("startTime", startTime);
        roomInformation.put("isPrivate", isPrivate);
        roomInformation.put("isOnGoing", isOngoing);
        System.out.println("---------------");
        System.out.println(roomInformation.get("startTime"));
        System.out.println(roomInformation.get("hostId"));
        sendMessage("update_room_information", roomInformation);
    }

    public void sendGetRoomRequest(String roomId) {
        Map<String, Object> request = new HashMap<>();
        request.put("roomId", roomId);
        sendMessage("request_current_room_information", request);
    }

    public void sendGetRoomsRequest(String userId) {
        Map<String, Object> request = new HashMap<>();
        request.put("userId", userId);
        sendMessage("request_all_rooms_information", request);
    }

    public void sendStartNotification(String userId) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("userId", userId);
        sendMessage("notify_game_start", notification);
    }

    public void sendCurrentPosition(String userId, LatLng location){
        Map<String, Object> currentPosition = new HashMap<>();
        currentPosition.put("userId", userId);
        currentPosition.put("latitude", location.latitude);
        currentPosition.put("longitude", location.longitude);

        sendMessage("current_position", currentPosition);
    }
    public void sendCurrentPressure(String userId, double pressure){
        Map<String, Object> currentPressure = new HashMap<>();
        currentPressure.put("userId", userId);
        currentPressure.put("currentPressure", pressure);
        sendMessage("current_pressure", currentPressure);
    }

    public void sendRegistration(String userName, String email, String password) {

        Map<String, Object> userInformation = new HashMap<>();
        userInformation.put("username", userName);
        userInformation.put("email", email);
        userInformation.put("password", password);

        sendMessage("registration", userInformation);
    }

    public void start() {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("ws://13.55.228.219:8080/ws/" + uniqueID)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                sendMessage("auth", Collections.singletonMap("token", "YOUR_SECRET_TOKEN"));
                listener.onMessageReceived("Trying to connect");
                handler.post(locationUpdateRunnable);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    if ("Validation Fail".equals(text)) {

                        listener.onMessageReceived("Validation Fail");
                        return;
                    }

                    Message message = new Gson().fromJson(text, Message.class);
                    System.out.println("system received: " + message.getType() + message.getData());
//                    System.out.println(text);
                    if ("Connection established".equals(text)) {
                        if (listener != null) {
                            listener.onMessageReceived("Connection established");
                        }
                        return;
                    }
                    if ("Authentication failed.".equals(text)) {
                        listener.onMessageReceived("Connection failed_0");
                        webSocket.close(1000, "Authentication failed.");
                        return;
                    }

                    if ("user_location".equals(message.getType())) {
                        UserLocation userLocation = new Gson().fromJson(new Gson().toJson(message.getData()), UserLocation.class);
                        listener.onMessageReceived(userLocation.toString());

                    } else if ("room_information".equals(message.getType())) {
                        RoomInformation roomInformation = new Gson().fromJson(new Gson().toJson(message.getData()), RoomInformation.class);
                        listener.onMessageReceived(roomInformation.toString());

                    } else if ("current_position".equals(message.getType())) {
                        CurrentPosition currentPosition = new Gson().fromJson(new Gson().toJson(message.getData()), CurrentPosition.class);
                        listener.onMessageReceived(currentPosition.toString());

                    } else if ("account".equals(message.getType())) {
                        System.out.println("receive");
                        account = new Gson().fromJson(new Gson().toJson(message.getData()), Account.class);
                        listener.onMessageReceived("Validation Successful");

                    } else if ("successfully create room".equals(message.getType())) {
                        listener.onMessageReceived("Successfully create room");

                    } else if ("get all updated rooms".equals(message.getType())) {
                        System.out.println("get all rooms");// TODO
                        String jsonRooms = new Gson().toJson(message.getData());
                        Type type = new TypeToken<ConcurrentHashMap<String, RoomInformation>>() {
                        }.getType();
                        ConcurrentHashMap<String, RoomInformation> allRooms = new Gson().fromJson(jsonRooms, type);
                        rooms = new AllRoomsInfo();
                        rooms.setRooms(allRooms);
                        System.out.println(rooms.getRooms().size());
                        listener.onMessageReceived("All updated rooms received");

                    } else if ("game start".equals(message.getType())) {
                        System.out.println("shoudaole");
                        listener.onMessageReceived("game starts");

                    } else if ("updated positions".equals(message.getType())){
                        positionList = new Gson().fromJson(new Gson().toJson(message.getData()), PositionList.class);
                        listener.onMessageReceived("get updated positions");

                    }else if ("updated pressure".equals(message.getType())){
                        System.out.println("received pressure list");
                        pressureList = new Gson().fromJson(new Gson().toJson(message.getData()), PressureList.class);
                        listener.onMessageReceived("get updated pressures");

                    }else if ("successfully update room".equals(message.getType())){
                        System.out.println("received update room");
                        updatedRoom = new Gson().fromJson(new Gson().toJson(message.getData()), RoomInformation.class);
                        listener.onMessageReceived("get updated room");

                    }else {
                        listener.onMessageReceived("wrong_message_type");
                    }
                } catch (JsonSyntaxException e) {
                    // JSON格式不正确
                    e.printStackTrace();
                    // 你可以在这里添加其他的错误处理逻辑，例如日志记录或用户提示
                } catch (Exception e) {
                    // 其他可能的异常
                    e.printStackTrace();
                    // 你可以在这里添加其他的错误处理逻辑
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // 当从服务器接收到二进制消息时调用
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                // 当连接失败或发生错误时调用
                listener.onMessageReceived("Connection failed_1");
                handler.removeCallbacks(locationUpdateRunnable);
                t.printStackTrace();
            }
        });
    }

    public void stop() {
        if (webSocket != null) {
            webSocket.close(1000, "Goodbye!");
        }
    }

    public void sendMessage(String type, Map<String, Object> data) {
        if (webSocket != null) {
            Message message = new Message(type, data);
            String json = new Gson().toJson(message);
            webSocket.send(json);
        }
    }

    public Account getAccount() {
        return account;
    }

    public RoomInformation getRoomInformation() {
        return room;
    }
    public AllRoomsInfo getAllRooms() {
        return rooms;
    }
    public PositionList getPositionList(){
        return positionList;
    }
    public PressureList getPressureList(){
        return pressureList;
    }
    public RoomInformation getUpdatedRoom(){
        return updatedRoom;
    }
}
