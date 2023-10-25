package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;

public class RegisterPage extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }

    public void openLoginPage(View view) {
        startActivity(new Intent(this, LoginPage.class));
    }

    public void verifyInput(View view) {
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.password2);
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        emailEditText = findViewById(R.id.email);
        String email = emailEditText.getText().toString().trim();

        if (isValidEmail(email)) {
            // Valid email format, proceed with registration or the next step.
            // You can add your registration logic here.
        } else {
            // Invalid email format, show an error message or take appropriate action.
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.equals(confirmPassword)) {
            // Passwords match, proceed with registration or the next step.
            // You can add your registration logic here.

        } else {
            // Passwords do not match, show an error message or take appropriate action.
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(view.getContext(), HomePage.class);
        startActivity(intent);
    }

    private boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}