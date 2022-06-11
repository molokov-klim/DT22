package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class JoystickActivity extends AppCompatActivity {

    BackgroundTask backgroundTask = new JoystickActivity.BackgroundTask();
    private String messageToServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
    }

    public void rotateUp(View view) {
        messageToServer = "rotate "+ClientActivity.token+" rotateUp";
        backgroundTask.execute(messageToServer);
    }

    public void rotateDown(View view) {
        messageToServer = "rotate "+ClientActivity.token+" rotateDown";
        backgroundTask.execute(messageToServer);
    }

    public void rotateLeft(View view) {
        messageToServer = "rotate "+ClientActivity.token+" rotateLeft";
        backgroundTask.execute(messageToServer);
    }

    public void rotateRight(View view) {
        messageToServer = "rotate "+ClientActivity.token+" rotateRight";
        backgroundTask.execute(messageToServer);
    }

    @SuppressLint("StaticFieldLeak")
    class BackgroundTask extends AsyncTask<String, Void, String>
    {
        Socket socket;
        PrintWriter printWriter;
        String direction;

        @Override
        protected String doInBackground(String... params) {
            direction = params[0];
            try {
                String ip = "188.127.239.39";
                int port = 9700;
                socket = new Socket(ip, port);
                printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.write(direction);
                printWriter.flush();
                printWriter.close();
                System.out.println("messageToServer: "+direction);
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}