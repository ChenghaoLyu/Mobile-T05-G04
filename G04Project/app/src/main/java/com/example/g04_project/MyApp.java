package com.example.g04_project;

import android.app.Application;

public class MyApp extends Application {
    private WebSocketClient webSocketClient;

    @Override
    public void onCreate() {
        super.onCreate();
        webSocketClient = new WebSocketClient();
    }

    public WebSocketClient getWebSocketClient() {
        return webSocketClient;
    }
}

