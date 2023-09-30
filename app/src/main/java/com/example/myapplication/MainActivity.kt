package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.Request
class MainActivity : Activity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.0.2.2:5000/") // 注意: 10.0.2.2 是Android模拟器访问本地服务器的地址
                    .build()

                val response = client.newCall(request).execute()
                val result = response.body?.string()

                runOnUiThread {
                    textView.text = result
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}