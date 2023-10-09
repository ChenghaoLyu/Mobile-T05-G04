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
    private Button goto_newgame_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goto_newgame_page = findViewById(R.id.goto_newgame_page);

        goto_newgame_page.setOnClickListener(view ->{
            open_newgame_page(view);
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:5001/") // 注意: 10.0.2.2 是Android模拟器访问本地服务器的地址
                            .build();

                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    // 解析JSON响应以获取消息内容
                    JSONObject jsonResponse = new JSONObject(result);
                    final String message = jsonResponse.getString("message");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(message);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void open_newgame_page(View view){
        startActivity(new Intent(this, Create_newgame_page.class));
    }
}
