package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dt22.engine.LoopSessionEngine;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoopSessionEngine loopSessionEngine = new LoopSessionEngine();
        loopSessionEngine.stopSession();
    }

    public void createSession(View view) {
        System.out.println("MainActivity public void create_session(View view)");

        Intent serverSession = new Intent (MainActivity.this, SessionActivity.class);
        startActivity(serverSession);
    }

    public void findSession(View view) {
        System.out.println("MainActivity public void findSession(View view)");

        Intent findSession = new Intent (MainActivity.this, ClientActivity.class);
        startActivity(findSession);
    }
}




