package com.example.g04_project;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;
public class GameFinishPage extends AppCompatActivity {
    private WebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);

        MyApp app = (MyApp) getApplication();
        client = app.getWebSocketClient();
    }

    public void goToHome(View view) {
        startActivity(new Intent(this, HomePage.class));
    }

    public void openCreateGamePage(View view) {
        startActivity(new Intent(this, Create_newgame_page.class));
    }

}
