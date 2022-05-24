package com.example.dt22;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;

import com.example.dt22.engine.BluetoothAdapterEngine;
import com.example.dt22.engine.ListBluetoothItemEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private ListView listView;
    private BluetoothAdapterEngine bluetoothAdapterEngine;
    private BluetoothAdapter bluetoothAdapter;
    private List<ListBluetoothItemEngine> list;
    private final int ENABLE_REQUEST = 15;
    private boolean BluetoothStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        init();
    }

    private void init() {

        if (!bluetoothAdapter.isEnabled()) {
            enableBluetooth();
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        list = new ArrayList<>();
        listView = findViewById(R.id.bluetoothListView);
        bluetoothAdapterEngine = new BluetoothAdapterEngine(this, R.layout.bluetooth_list_item, list);
        listView.setAdapter(bluetoothAdapterEngine);

    }

    private void getPairedDevices() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            list.clear();
            for (BluetoothDevice device : pairedDevices) {
                ListBluetoothItemEngine item = new ListBluetoothItemEngine();
                item.setBluetoothName(device.getName());
                item.setBluetoothMac(device.getAddress());
                list.add(item);
            }
            bluetoothAdapterEngine.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENABLE_REQUEST) {
            if (resultCode == RESULT_OK) {
                BluetoothStatus = true;
            } else BluetoothStatus = false;
        }
    }


    private void enableBluetooth() {

        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivityForResult(i, ENABLE_REQUEST);

    }


}











































