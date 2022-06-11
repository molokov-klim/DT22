package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ClientActivity extends AppCompatActivity {

    public static String token;
    EditText editTextToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        editTextToken = findViewById(R.id.et_token);
    }

    public void connectSession(View v) {
        token = editTextToken.getText().toString();
        Intent connectSession = new Intent (ClientActivity.this, ClientSessionActivity.class);
        startActivity(connectSession);
    }
}