package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {

    EditText token;
    String ip = "188.127.239.39";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        token = findViewById(R.id.et_token);
    }

    public void connectSession(View v) {
        BackgroundTask b = new BackgroundTask();
        b.execute(ip, "connectSession "+token.getText().toString());
    }

    static class BackgroundTask extends AsyncTask<String, Void, String>
    {
        Socket socket;
        DataOutputStream dataOutputStream;
        DataInputStream dataInputStream;
        String serverIP, messageToServer, messageFromServer;

        @Override
        protected String doInBackground(String... params) {
            serverIP = params[0];
            messageToServer = params [1];
            try {
                socket = new Socket(serverIP, 9700);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(messageToServer);
                System.out.println("messageToServer: "+messageToServer);
                dataOutputStream.close();
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}


































