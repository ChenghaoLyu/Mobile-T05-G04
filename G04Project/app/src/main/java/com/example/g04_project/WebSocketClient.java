package com.example.g04_project;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient {

    private OkHttpClient client;
    private WebSocket webSocket;

    public void start() {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("ws://10.0.2.2:8000/ws/user123")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                // 当WebSocket连接打开时调用
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    Message message = new Gson().fromJson(text, Message.class);
                    if ("user_location".equals(message.getType())) {
                        // 解析并处理用户位置数据
                        UserLocation userLocation = new Gson().fromJson(new Gson().toJson(message.getData()), UserLocation.class);
                        // ... 处理userLocation数据 ...
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
