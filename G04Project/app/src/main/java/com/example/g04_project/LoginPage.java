package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.g04_project.WebSocketClient;


public class LoginPage extends AppCompatActivity {

    private EditText passwordEditText;

    // Create an instance of WebSocketClient
    WebSocketClient webSocketClient = new WebSocketClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void openRegisterPage(View view) {
        startActivity(new Intent(this, RegisterPage.class));
    }

//    public void verifyInput(View view) {
//        passwordEditText = findViewById(R.id.password);
//        String password = passwordEditText.getText().toString();
//
//
//        if () {
//            // Passwords match, proceed with registration or the next step.
//            // You can add your registration logic here.
//            Intent intent = new Intent(view.getContext(), HomePage.class);
//            startActivity(intent);
//        } else {
//            // Passwords do not match, show an error message or take appropriate action.
//            Toast.makeText(getApplicationContext(), "Invalid user name or password", Toast.LENGTH_SHORT).show();
//            return;
//        }
//    }

}
