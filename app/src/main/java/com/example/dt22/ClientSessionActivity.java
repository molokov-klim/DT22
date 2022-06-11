package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSessionActivity extends AppCompatActivity {

    private String messageToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_session);
        BackgroundTask b = new ClientSessionActivity.BackgroundTask();
        messageToServer = "connectSession "+ClientActivity.token;
        b.execute();
    }

    public void disconnectSession(View view) {
        Intent disconnectSession = new Intent (ClientSessionActivity.this, MainActivity.class);
        startActivity(disconnectSession);
    }

    public void hideClientActivity(View view) {
        Intent joystick = new Intent (ClientSessionActivity.this, JoystickActivity.class);
        startActivity(joystick);
    }

    @SuppressLint("StaticFieldLeak")
    class BackgroundTask extends AsyncTask<String, Void, String>
    {
        Socket socket;
        PrintWriter printWriter;

        @Override
        protected String doInBackground(String... params) {
            try {
                String ip = "188.127.239.39";
                int port = 9700;
                socket = new Socket(ip, port);
                printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.write(messageToServer);
                printWriter.flush();
                printWriter.close();
                System.out.println("messageToServer: "+messageToServer);
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}