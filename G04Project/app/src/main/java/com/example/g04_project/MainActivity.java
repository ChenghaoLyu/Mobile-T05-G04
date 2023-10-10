package com.example.g04_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private WebSocketClient client;
    private Button goto_newgame_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goto_newgame_page = findViewById(R.id.goto_newgame_page);

        goto_newgame_page.setOnClickListener(view ->{
            open_newgame_page(view);
        });
        client = new WebSocketClient();
        client.setOnMessageReceivedListener(new WebSocketClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(message);
                    }
                });
            }
        });

        client.start(); // 启动WebSocket连接
    }
    public void open_newgame_page(View view){
        startActivity(new Intent(this, Create_newgame_page.class));
    }
}
