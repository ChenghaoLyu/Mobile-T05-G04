package com.example.g04_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginPage extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;

    private WebSocketClient client;

    // Create an instance of WebSocketClient
    WebSocketClient webSocketClient = new WebSocketClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        MyApp app = (MyApp) getApplication();
        client = app.getWebSocketClient();
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
            //Toast.makeText(getApplicationContext(), "Please enter all required fields!", Toast.LENGTH_SHORT).show();
            return;

            // Invalid email format, show an error message or take appropriate action.
        } else if (!isValidEmail(email)) {

            displayToast("Invalid email, please try again!");
            //Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;

        } else {
            displayToast("Validating");
            webSocketClient.sendValidation(email, password);
        }

        client.setOnMessageReceivedListener(new WebSocketClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                // Passwords match, proceed with registration or the next step.
                if (message.equals("Validation Successful")) {
                    displayToast("Login Successful");
                    Intent intent = new Intent(view.getContext(), HomePage.class);
                    startActivity(intent);

                // Incorrect validation, show an error message or take appropriate action.
                } else {

                    displayToast("Invalid email or password, please try again!");
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
