package com.example.g04_project;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

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
}
