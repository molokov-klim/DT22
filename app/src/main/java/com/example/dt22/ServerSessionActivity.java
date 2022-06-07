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
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dt22.engine.BtConnection;
import com.example.dt22.engine.BtConst;
import com.example.dt22.engine.LoopServerEngine;

public class ServerSessionActivity extends AppCompatActivity {

    public static String TOKEN;
    private MenuItem menuItem;
    private BluetoothAdapter btAdapter;
    private final int ENABLE_REQUEST = 15;
    private SharedPreferences pref;
    private BtConnection btConnection;
    private Button bA, bB;

    public String ipAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_session);
        bA = findViewById(R.id.buttonA);
        bB = findViewById(R.id.buttonB);
        init();
        bA.setOnClickListener(v -> {
            btConnection.sendMessage("A");
        });
        bB.setOnClickListener(v -> {
            btConnection.sendMessage("B");
        });

        //GET IP
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        System.out.println("LOG SERVER IP "+ipAddress);

        TextView serverIpTextView = findViewById(R.id.pt_server_ip);
        serverIpTextView.setText(ipAddress);

        //Создание сессии сервера
        LoopServerEngine loopServerEngine = new LoopServerEngine();
        loopServerEngine.startServerSession();

        //Генерация токена
        TOKEN = LoopServerEngine.generateNewToken();
        System.out.println("TOKEN "+TOKEN);
        TextView tokenTextView = findViewById(R.id.et_token);
        tokenTextView.setText(TOKEN);



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
            Intent i =new Intent(ServerSessionActivity.this, BtListActivity.class);
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
        LoopServerEngine loopServerEngine = new LoopServerEngine();
        loopServerEngine.stopServerSession();
        TOKEN = null;

        Intent mainActivity = new Intent (ServerSessionActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    // Копирование токена в буфер
    public void copyToken(View view) {
        ClipboardManager clipboard = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData clip = ClipData.newPlainText("TOKEN ", TOKEN);
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
            Intent i =new Intent(ServerSessionActivity.this, BtListActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Включите блютуз для перехода", Toast.LENGTH_SHORT).show();
        }
    }


}























