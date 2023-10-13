package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Startpage extends AppCompatActivity {

    private Button goto_start_2_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        goto_start_2_page = findViewById(R.id.goto_start_2_page);

        goto_start_2_page.setOnClickListener(view ->{
            open_start_2_page(view);
        });

    }

    public void open_start_2_page(View view){
        startActivity(new Intent(this, Startpage_2.class));
    }
}