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

    private TextView text_message;
    private WebSocketClient client;
    private Button goto_newgame_page, goto_gamestart_page, goto_start_page, goto_HomePage, goto_RegisterPage, goto_LoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goto_newgame_page = findViewById(R.id.goto_newgame_page);
        goto_gamestart_page = findViewById(R.id.goto_gamestart_page);
        goto_start_page = findViewById(R.id.goto_start_page);
        goto_HomePage = findViewById(R.id.goto_HomePage);
        goto_LoginPage = findViewById(R.id.goto_LoginPage);
        goto_RegisterPage = findViewById(R.id.goto_RegisterPage);
        goto_newgame_page.setOnClickListener(view ->{
            open_newgame_page(view);
        });
        goto_gamestart_page.setOnClickListener(view ->{
            open_gamestart_page(view);
        });
        goto_start_page.setOnClickListener(view ->{
            open_start_page(view);
        });
        goto_LoginPage.setOnClickListener(view ->{
            open_LoginPage(view);
        });
        goto_RegisterPage.setOnClickListener(view ->{
            open_RegisterPage(view);
        });
        goto_HomePage.setOnClickListener(view ->{
            open_HomePage(view);
        });


        client = new WebSocketClient();
        client.setOnMessageReceivedListener(new WebSocketClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_message.setText(message);
                    }
                });
            }
        });

        client.start(); // 启动WebSocket连接
    }
    public void open_newgame_page(View view){
        startActivity(new Intent(this, Create_newgame_page.class));
    }
    public void open_gamestart_page(View view){
        startActivity(new Intent(this, Create_gamestart_page.class));
    }
    public void open_start_page(View view){
        startActivity(new Intent(this, Startpage.class));
    }

    public void open_HomePage(View view){
        startActivity(new Intent(this, HomePage.class));
    }

    public void open_RegisterPage(View view){
        startActivity(new Intent(this, RegisterPage.class));
    }

    public void open_LoginPage(View view){
        startActivity(new Intent(this, LoginPage.class));
    }
}
