package com.example.dt22;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dt22.engine.BtConnection;
import com.example.dt22.engine.BtConst;
import com.example.dt22.engine.LoopSessionEngine;

public class SessionActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter;
    private BtConnection btConnection;

    public static String token;
    private MenuItem menuItem;
    private final int ENABLE_REQUEST = 15;
    private SharedPreferences pref;



    //TEMP
    private Button bA, bB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        //Запрос генерации и вывод токена
        token = LoopSessionEngine.generateNewToken();
        TextView tokenTextView = findViewById(R.id.et_token);
        tokenTextView.setText(token);

        //Создание сессии сервера
        LoopSessionEngine loopSessionEngine = new LoopSessionEngine();
        loopSessionEngine.startSession();


        //TEMP
        bA = findViewById(R.id.buttonA);
        bB = findViewById(R.id.buttonB);
        init();
        bA.setOnClickListener(v -> {
            btConnection.sendMessage("A");
        });
        bB.setOnClickListener(v -> {
            btConnection.sendMessage("B");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItem = menu.findItem(R.id.id_bt_button);
        setBtIcon();
        return super.onCreateOptionsMenu(menu);
    }

    // Слушатель нажатий на кнопки меню
    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.id_bt_button){
            if(!btAdapter.isEnabled()){
                enableBt();
            } else {
                btAdapter.disable();
                menuItem.setIcon(R.drawable.ic_bt_enable);
            }
        } else if(item.getItemId() == R.id.id_menu){
            if(btAdapter.isEnabled()){
            Intent i =new Intent(SessionActivity.this, BtListActivity.class);
            startActivity(i);
            } else {
                Toast.makeText(this, "Включите блютуз для перехода", Toast.LENGTH_SHORT).show();
            }
        } else if(item.getItemId() == R.id.id_connect){
            btConnection.connect();
        }
        return super.onOptionsItemSelected(item);
    }

    // Отслеживатель сообщений от активити
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ENABLE_REQUEST){
            if(resultCode == RESULT_OK){
                setBtIcon();

            }
        }
    }

    // Переключатель иконки
    private void setBtIcon(){
        if(btAdapter.isEnabled()){
            menuItem.setIcon(R.drawable.ic_bt_disable);
        } else {
            menuItem.setIcon(R.drawable.ic_bt_enable);
        };
    }

    // Закрытие сессии сервера
    public void closeSession(View view) {
        LoopSessionEngine loopSessionEngine = new LoopSessionEngine();
        loopSessionEngine.stopSession();
        token = null;

        Intent mainActivity = new Intent (SessionActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    // Копирование токена в буфер
    public void copyToken(View view) {
        ClipboardManager clipboard = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData clip = ClipData.newPlainText("TOKEN ", token);
        clipboard.setPrimaryClip(clip);

        System.out.println("clip "+clip);
    }

    // Инициализация
    private void init(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pref = getSharedPreferences(BtConst.MY_PREF, Context.MODE_PRIVATE);
        Log.d("MyLog", "BtMac "+pref.getString(BtConst.MAC_KEY, "no bt selected"));
        btConnection = new BtConnection(this);
    };

    // Включение bluetooth
    @SuppressLint("MissingPermission")
    private void enableBt(){
        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(i, ENABLE_REQUEST);
    };

    // Нажатие на кнопку "CONNECT BLUETOOTH"
    public void connectBluetooth(View view) {
        if(btAdapter.isEnabled()){
            Intent i =new Intent(SessionActivity.this, BtListActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Включите блютуз для перехода", Toast.LENGTH_SHORT).show();
        }
    }


}























