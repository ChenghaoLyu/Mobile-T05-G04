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

            displayToast("Please enter all required fields!");
            //Toast.makeText(getApplicationContext(), "Please enter all required fields!", Toast.LENGTH_SHORT).show();
            return;

        // Invalid email format, show an error message or take appropriate action.
        } else if (!isValidEmail(email)) {

            displayToast("Invalid email");
            //Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;

        // Passwords do not match
        } else if (!confirmPassword.equals(password)) {

            displayToast("Passwords do not match");
            //Toast.makeText(getApplicationContext(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
            return;

        // Password too short
        } else if (password.length() < 8) {

            displayToast("Password must be at least 8 digits long");
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
                if (message.equals("Registration Successful")) {
                    Intent intent = new Intent(view.getContext(), HomePage.class);
                    startActivity(intent);

                // Incorrect validation, show an error message or take appropriate action.
                } else {

                    displayToast("Registration fails. Please try again!");
                    //Toast.makeText(getApplicationContext(), "Invalid user name or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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