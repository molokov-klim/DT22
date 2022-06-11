package com.example.dt22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dt22.engine.BtAdapter;
import com.example.dt22.engine.BtListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BtListActivity extends AppCompatActivity {
    private final int BT_REQUEST_PERM = 111;
    private ListView listView;
    private BtAdapter adapter;
    private BluetoothAdapter btAdapter;
    private List<BtListItem> list;
    private boolean isBtPermissionGranted = false;
    private boolean isDiscovery = false;
    private ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_list);
        getBtPermission();
        init();
    }

    // Фильтр устройств для BroadcastReceiver (сообщений от ОС)
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter f1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter f2 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        IntentFilter f3 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bReceiver, f1);
        registerReceiver(bReceiver, f2);
        registerReceiver(bReceiver, f3);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bt_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Обработчик нажатия на кнопки в верхнем меню
    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(isDiscovery){
                btAdapter.cancelDiscovery();
                isDiscovery=false;
                getPairedDevices();
            } else {
                finish();
            }
        } else if(item.getItemId() == R.id.id_search){
            if(isDiscovery)return true;
            ab.setTitle(R.string.discovering);
            list.clear();
            BtListItem itemTitle = new BtListItem();
            itemTitle.setItemType(BtAdapter.TITLE_ITEM_TYPE);
            list.add(itemTitle);
            adapter.notifyDataSetChanged();
            btAdapter.startDiscovery();
            isDiscovery = true;
            System.out.println("LOG btAdapter.startDiscovery() "+btAdapter.startDiscovery());
            System.out.println("LOG isBtPermissionGranted "+isBtPermissionGranted);
        }
        return true;
    }

    // Инициализация
    private void init(){
        ab = getSupportActionBar();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println("LOG btAdapter "+btAdapter);
        list = new ArrayList<>();

        ActionBar ab = getSupportActionBar();
        if(ab==null)return;
        ab.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);
        adapter = new BtAdapter(this, R.layout.bt_list_item, list);
        listView.setAdapter(adapter);
        getPairedDevices();
        onItemClickListener();
    }

    // Слушатель нажатий на список устройств
    @SuppressLint("MissingPermission")
    private void onItemClickListener(){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            BtListItem item = (BtListItem)parent.getItemAtPosition(position);
            if(item.getItemType().equals(BtAdapter.DISCOVERY_ITEM_TYPE)){
                item.getBtDevice().createBond(); //запрос на сопряжение
            }
        });
    }

    // Получение списка сопряженных устройств
    @SuppressLint("MissingPermission")
    private void getPairedDevices(){
        @SuppressLint("MissingPermission")
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        System.out.println("LOG pairedDevices "+pairedDevices);

        if(pairedDevices.size()>0){
            System.out.println("LOG pairedDevices.size() "+pairedDevices.size());
            list.clear();
            for(BluetoothDevice device : pairedDevices){
                BtListItem item = new BtListItem();
                item.setBtDevice(device);
                System.out.println("LOG item "+item);
                System.out.println("LOG item name "+item.getBtDevice().getName());
                System.out.println("LOG item MAC "+item.getBtDevice().getAddress());
                list.add(item);
            }
            adapter.notifyDataSetChanged();
        }else System.out.println("LOG pairedDevices.size() "+pairedDevices.size());
    }

    // Системная функция проверки разрешений запускается с помощью ActivityCompat.requestPermissions и может работать с ОС
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("grantResults "+ Arrays.toString(grantResults));
        System.out.println("grantResults.length "+ grantResults.length);
        System.out.println("requestCode "+requestCode);
        System.out.println("permissions "+ Arrays.toString(permissions));
        System.out.println("Manifest.permission.ACCESS_FINE_LOCATION "+ Manifest.permission.ACCESS_FINE_LOCATION);
        System.out.println("PackageManager.PERMISSION_GRANTED "+ PackageManager.PERMISSION_GRANTED);

        if(requestCode == BT_REQUEST_PERM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                isBtPermissionGranted = true;
                Toast.makeText(this, "Разрешение получено", Toast.LENGTH_SHORT).show();
                System.out.println("LOG Разрешение получено");
            } else {
                Toast.makeText(this, "Ошибка. Нет разрешения на поиск устройств.", Toast.LENGTH_SHORT).show();
                System.out.println("LOG Разрешение НЕ получено");
            }
        } else {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // Проверка разрешений на обнаружение устройств
    private void getBtPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BT_REQUEST_PERM);
            System.out.println("LOG ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BT_REQUEST_PERM);");
        } else {
            isBtPermissionGranted = true;
        }
    }

    // Найденные устройства
    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("LOG public void onReceive ");
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BtListItem item = new BtListItem();
                item.setBtDevice(device);
                item.setItemType(BtAdapter.DISCOVERY_ITEM_TYPE);
                list.add(item);
                adapter.notifyDataSetChanged();
                System.out.println("LOG device.getName() "+device.getName());
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
                isDiscovery=false;
                getPairedDevices();
                ab.setTitle(R.string.app_name);
            } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                    getPairedDevices();
                }
            }


        }
    };
}
