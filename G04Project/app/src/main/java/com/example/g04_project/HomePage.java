package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void openJoinGamePage(View view) {
        startActivity(new Intent(this, Create_joinGame_page.class));
    }

    public void openCreateGamePage(View view) {
        startActivity(new Intent(this, Create_newgame_page.class));
    }


}
