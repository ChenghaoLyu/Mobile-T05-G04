package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginPage extends AppCompatActivity {
    private EditText emailEditText;
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

    public void validate(View view) {
        emailEditText = findViewById(R.id.email);
        String email = emailEditText.getText().toString().trim();
        passwordEditText = findViewById(R.id.password);
        String password = passwordEditText.getText().toString();

        // Check empty input
        if (password.isEmpty() || email.isEmpty()) {
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
            customToastTextView.setText("Invalid email, please try again!");

            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
            customToast.setView(customToastView);

            customToast.show();

            //Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;

        } else {
            webSocketClient.sendVerification(email, password);
        }

//        MyApp app = (MyApp) getApplication();
//        client = app.getWebSocketClient();
        webSocketClient.setOnMessageReceivedListener(new WebSocketClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                // Passwords match, proceed with registration or the next step.
                if (message.equals("TRUE")) {
                    Intent intent = new Intent(view.getContext(), HomePage.class);
                    startActivity(intent);

                // Incorrect validation, show an error message or take appropriate action.
                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View customToastView = inflater.inflate(R.layout.item_toast, null);

                    TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
                    customToastTextView.setText("Invalid email or password, please try again!");

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
