package com.example.dt22.engine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BtConnection {
    private Context context;
    private SharedPreferences pref;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice device;
    private ConnectThread connectThread;

    // Конструктор
    public BtConnection(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(BtConst.MY_PREF, Context.MODE_PRIVATE);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    // Подключение устройства на второстепенном потоке
    public void connect(){
        String mac = pref.getString(BtConst.MAC_KEY, "");
        if(!btAdapter.isEnabled() || mac.isEmpty()){
            Toast.makeText(context, "Включите bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        device = btAdapter.getRemoteDevice(mac);
        if(device == null) return;
        connectThread = new ConnectThread(btAdapter, device);
        connectThread.start();
    }
}














































