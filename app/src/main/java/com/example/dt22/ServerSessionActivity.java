package com.example.dt22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dt22.engine.LoopServerEngine;

public class ServerSessionActivity extends AppCompatActivity {

    public static String TOKEN;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_session);

        //Создание сессии сервера
        LoopServerEngine loopServerEngine = new LoopServerEngine();
        loopServerEngine.startServerSession();

        //Генерация токена
        TOKEN = LoopServerEngine.generateNewToken();
        System.out.println("TOKEN "+TOKEN);
        TextView tokenTextView = findViewById(R.id.et_token);
        tokenTextView.setText(TOKEN);
    }

    public void closeSession(View view) {
        LoopServerEngine loopServerEngine = new LoopServerEngine();
        loopServerEngine.stopServerSession();
        TOKEN = null;

        Intent mainActivity = new Intent (ServerSessionActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void copyToken(View view) {
        ClipboardManager clipboard = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData clip = ClipData.newPlainText("TOKEN ", TOKEN);
        clipboard.setPrimaryClip(clip);

        System.out.println("clip "+clip);
    }

    public void gotoConnectBluetooth(View view) {
        Intent bluetoothActivity = new Intent (ServerSessionActivity.this, BluetoothActivity.class);
        startActivity(bluetoothActivity);
    }
}


