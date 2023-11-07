package com.example.g04_project;

import android.os.Handler;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient {

    private OkHttpClient client;
    private WebSocket webSocket;
    private OnMessageReceivedListener listener;
    private Handler handler = new Handler();
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
        locationData.put("user_id", "user123");
        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);
        locationData.put("timestamp", timestamp);

        sendMessage("user_location", locationData);
    }

    public void sendRoomInformation(String roomId, String userId, String locationName, String modeName, String duration, String password,
                                     int catNumber, int currCatNum, int ratNumber, int currRatNum,
                                     String startTime, boolean isPrivate,
                                     ConcurrentHashMap<String, Player> cat_list,
                                     ConcurrentHashMap<String, Player> rat_list,
                                     ConcurrentHashMap<String, String> ready_list) {
        // 获取位置信息（这里只是一个示例，你可能需要从GPS或其他位置服务获取实际的位置数据）

        Map<String, Object> roomInformation = new HashMap<>();
        roomInformation.put("user_id", userId);
        roomInformation.put("room_id", roomId);
        roomInformation.put("locationName", locationName);
        roomInformation.put("modeName", modeName);
        roomInformation.put("duration", duration);
        roomInformation.put("password", password);
        roomInformation.put("catNumber", catNumber);
        roomInformation.put("ratNumber", ratNumber);
        roomInformation.put("currCatNum", currCatNum);
        roomInformation.put("currRatNum", currRatNum);
        roomInformation.put("startTime", startTime);
        roomInformation.put("privacy", isPrivate);
        roomInformation.put("ready_list", ready_list);
        roomInformation.put("cat_list", cat_list);
        roomInformation.put("rat_list", rat_list);

        sendMessage("room_information", roomInformation);
    }

    public void sendRegistration(String userName, String email, String password) {

        Map<String, Object> userInformation = new HashMap<>();
        userInformation.put("username", userName);
        userInformation.put("email", email);
        userInformation.put("password", password);

        sendMessage("registration", userInformation);
    }

    public void sendValidation(String email, String password) {

        Map<String, Object> userInformation = new HashMap<>();
        userInformation.put("email", email);
        userInformation.put("password", password);

        sendMessage("validation", userInformation);
    }


    public void start() {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("ws://10.0.2.2:8080/ws/user123")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                // 当WebSocket连接打开时调用
                sendMessage("auth", Collections.singletonMap("token", "YOUR_SECRET_TOKEN"));
                listener.onMessageReceived("Trying to connect");
                handler.post(locationUpdateRunnable);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    Message message = new Gson().fromJson(text, Message.class);
                    if ("Connection established".equals(text)) {
                        if (listener != null) {
                            listener.onMessageReceived("Connection established");
                        }
                        return;
                    }
                    if ("Authentication failed.".equals(text)) {
                        // 处理身份验证失败的情况
                        // 例如，你可以关闭WebSocket连接或通知用户身份验证失败
                        listener.onMessageReceived("Connection failed_0");
                        webSocket.close(1000, "Authentication failed.");
                        return;
                    }

                    if ("Validation Successful".equals(text)) {
                        if (listener != null) {
                            listener.onMessageReceived("Validation Successful");
                        }
                        return;
                    }

                    if ("user_location".equals(message.getType())) {
                        // 解析并处理用户位置数据
                        UserLocation userLocation = new Gson().fromJson(new Gson().toJson(message.getData()), UserLocation.class);
                        listener.onMessageReceived(userLocation.toString());
                        // ... 处理userLocation数据 ...
                    } else if ("room_information".equals(message.getType())) {
                        RoomInformation roomInformation = new Gson().fromJson(new Gson().toJson(message.getData()), RoomInformation.class);
                        listener.onMessageReceived(roomInformation.toString());
                    } else if ("validation".equals(message.getType())) {
                        Validation validation = new Gson().fromJson(new Gson().toJson(message.getData()), Validation.class);
                        Boolean confirmation = validation.getValidationSuccess();
                        listener.onMessageReceived(confirmation.toString());
                    } else if ("registration".equals(message.getType())) {
                        Registration registration = new Gson().fromJson(new Gson().toJson(message.getData()), Registration.class);
                        Boolean confirmation = registration.getValidationSuccess();
                        listener.onMessageReceived(confirmation.toString());
                    } else {
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


}
