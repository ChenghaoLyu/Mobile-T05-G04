package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Startpage_2 extends AppCompatActivity {

    private Button goto_home_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage2);
        goto_home_page = findViewById(R.id.goto_home_page);

        goto_home_page.setOnClickListener(view ->{
            open_home_page(view);
        });
    }

    public void open_home_page(View view){
        startActivity(new Intent(this, LoginPage.class));
    }
}