package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;

public class ClientActivity extends AppCompatActivity {

    EditText token;
    String ip = "188.127.239.39";
    String messageToServer, messageFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        token = findViewById(R.id.et_token);
    }

    public void connectSession(View v) {
        BackgroundTask b = new BackgroundTask();
        messageToServer = "connectSession "+token.getText().toString();
        b.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class BackgroundTask extends AsyncTask<String, Void, String>
    {
        Socket socket;
        //DataOutputStream dataOutputStream;
        //DataInputStream dataInputStream;
        PrintWriter printWriter;


        @Override
        protected String doInBackground(String... params) {
            try {
                socket = new Socket(ip, 9700);
                //dataOutputStream = new DataOutputStream(socket.getOutputStream());
                //dataOutputStream.writeUTF(messageToServer);
                printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.write(messageToServer);
                printWriter.flush();
                printWriter.close();
                socket.close();
                System.out.println("messageToServer: "+messageToServer);
                //dataOutputStream.flush();
                //dataOutputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}


































