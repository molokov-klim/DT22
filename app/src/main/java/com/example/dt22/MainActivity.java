package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createSession(View view) {
        Intent createSession = new Intent (MainActivity.this, HostSessionActivity.class);
        startActivity(createSession);
    }

    public void findSession(View view) {
        Intent findSession = new Intent (MainActivity.this, ClientActivity.class);
        startActivity(findSession);
    }
}

