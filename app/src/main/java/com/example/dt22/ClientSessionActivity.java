package com.example.dt22;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSessionActivity extends AppCompatActivity {

    private String messageToServer;
    Button buttonAddWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_session);
        BackgroundTask b = new ClientSessionActivity.BackgroundTask();
        messageToServer = "connectSession "+ClientActivity.token;
        b.execute();

        buttonAddWidget = (Button) findViewById(R.id.bt_hide);
        getPermisson();
        buttonAddWidget.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!Settings.canDrawOverlays(ClientSessionActivity.this)) {
                    getPermisson();
                } else {
                    Intent intent = new Intent(ClientSessionActivity.this, WidgetService.class);
                    startService(intent);
                    finish();
                }
            }
        });

    }

    // получение прав на поверх всего
    public void getPermisson() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(!Settings.canDrawOverlays(ClientSessionActivity.this)){
                Toast.makeText(this, "Разрешение отклонено пользователем", Toast.LENGTH_SHORT).show();
            }
        }
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