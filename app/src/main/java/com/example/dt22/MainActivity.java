package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dt22.engine.LoopServerEngine;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoopServerEngine loopServerEngine = new LoopServerEngine();
        loopServerEngine.stopServerSession();
    }

    public void createSession(View view) {
        System.out.println("MainActivity public void create_session(View view)");

        Intent serverSession = new Intent (MainActivity.this, ServerSessionActivity.class);
        startActivity(serverSession);
    }
}




