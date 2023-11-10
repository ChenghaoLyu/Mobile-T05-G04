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

    private WebSocketClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        MyApp app = (MyApp) getApplication();
        client = app.getWebSocketClient();
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
            return;

        // Invalid email format, show an error message or take appropriate action.
        } else if (!isValidEmail(email)) {
            displayToast("Invalid email");
            return;

        // Passwords do not match
        } else if (!confirmPassword.equals(password)) {
            displayToast("Passwords do not match");
            return;

        // Password too short
        } else if (password.length() < 8) {
            displayToast("Password must be at least 8 digits long");
            return;
        }

        else {
            client.sendRegistration(username, email, password);
        }


        client.setOnMessageReceivedListener(new WebSocketClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                // Successful registration
                if (message.equals("Validation Successful")) {
                    Intent intent = new Intent(view.getContext(), HomePage.class);
                    startActivity(intent);

                // Incorrect validation, show an error message or take appropriate action.
                } else if (message.equals("Validation Fail")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayToast("Registration fails. Email already exists. Please try again!");
                        }
                    });
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