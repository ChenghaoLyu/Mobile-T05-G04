package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

public class LoginPage extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 123;
    private EditText emailEditText;
    private EditText passwordEditText;

    private WebSocketClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        MyApp app = (MyApp) getApplication();
        client = app.getWebSocketClient();

        // Check if the app has permission to access the device's location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted; you can proceed with location-related tasks.
        }
    }

    public void openRegisterPage(View view) {
        startActivity(new Intent(this, RegisterPage.class));
    }

    public void validate(View view) {
        emailEditText = findViewById(R.id.email);
        String email = emailEditText.getText().toString().trim();
        passwordEditText = findViewById(R.id.password);
        String password = passwordEditText.getText().toString();

        // Check empty input
        if (password.isEmpty() || email.isEmpty()) {

            displayToast("Please enter all required fields!");
            return;

            // Invalid email format, show an error message or take appropriate action.
        } else if (!isValidEmail(email)) {

            displayToast("Invalid email, please try again!");
            return;

        } else {
            client.sendValidation(email, password);
        }

        client.setOnMessageReceivedListener(new WebSocketClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                // Passwords match, proceed with registration or the next step.
                if (message.equals("Validation Successful")) {

                    Intent intent = new Intent(view.getContext(), HomePage.class);
                    startActivity(intent);


                // Incorrect validation, show an error message or take appropriate action.
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayToast("Invalid email or password, please try again!");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted; you can now access the location.
            } else {
                // Permission denied; handle the situation (e.g., show a message to the user).
                showPermissionDeniedMessage();
            }
        }
    }

    private void showPermissionDeniedMessage() {
        // Display a message explaining why the permission is necessary.
        displayToast("Location permission is required to use this app. Please grant the permission in app settings.");
    }
}
