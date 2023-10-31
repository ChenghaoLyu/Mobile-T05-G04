package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;

public class RegisterPage extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;

    WebSocketClient webSocketClient = new WebSocketClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }

    public void openLoginPage(View view) {
        startActivity(new Intent(this, LoginPage.class));
    }

    public void verifyInput(View view) {
        usernameEditText = findViewById(R.id.username);
        String username = usernameEditText.getText().toString();

        passwordEditText = findViewById(R.id.password);
        String password = passwordEditText.getText().toString();

        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        String confirmPassword = confirmPasswordEditText.getText().toString();

        emailEditText = findViewById(R.id.email);
        String email = emailEditText.getText().toString().trim();

        // Check empty input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.item_toast, null);

            TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
            customToastTextView.setText("Please enter all required fields!");

            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
            customToast.setView(customToastView);

            customToast.show();
            //Toast.makeText(getApplicationContext(), "Please enter all required fields!", Toast.LENGTH_SHORT).show();
            return;

        // Invalid email format, show an error message or take appropriate action.
        } else if (!isValidEmail(email)) {

            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.item_toast, null);

            TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
            customToastTextView.setText("Invalid email");

            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
            customToast.setView(customToastView);

            customToast.show();

            //Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;

        // Passwords do not match
        } else if (!confirmPassword.equals(password)) {
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.item_toast, null);

            TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
            customToastTextView.setText("New password and confirm password do not match");

            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
            customToast.setView(customToastView);

            customToast.show();

            //Toast.makeText(getApplicationContext(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
            return;

        // Password too short
        } else if (password.length() < 8) {
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.item_toast, null);

            TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
            customToastTextView.setText("Password must be at least 8 digits long");

            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
            customToast.setView(customToastView);

            customToast.show();

            //Toast.makeText(getApplicationContext(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            webSocketClient.sendRegistration(username, email, password);
        }


//        MyApp app = (MyApp) getApplication();
//        client = app.getWebSocketClient();
        webSocketClient.setOnMessageReceivedListener(new WebSocketClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                // Successful registration
                if (message.equals("TRUE")) {
                    Intent intent = new Intent(view.getContext(), HomePage.class);
                    startActivity(intent);

                // Incorrect validation, show an error message or take appropriate action.
                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View customToastView = inflater.inflate(R.layout.item_toast, null);

                    TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
                    customToastTextView.setText("Registration fails. Please try again!");

                    Toast customToast = new Toast(getApplicationContext());
                    customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
                    customToast.setView(customToastView);

                    customToast.show();
                    //Toast.makeText(getApplicationContext(), "Invalid user name or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}