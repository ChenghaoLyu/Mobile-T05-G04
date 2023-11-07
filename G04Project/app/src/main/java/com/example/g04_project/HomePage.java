package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        displayToast("Login Successful");
    }

    public void openJoinGamePage(View view) {
        startActivity(new Intent(this, Create_joinGame_page.class));
    }

    public void openCreateGamePage(View view) {
        startActivity(new Intent(this, Create_newgame_page.class));
    }

    private void displayToast(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View customToastView = inflater.inflate(R.layout.item_toast, null);

        TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
        customToastTextView.setText(msg);

        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
        customToast.setView(customToastView);

        customToast.setGravity(Gravity.CENTER, 0, 0);
        customToast.show();
    }
}
