package com.example.dt22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.dt22.engine.BtAdapter;
import com.example.dt22.engine.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BtListActivity extends AppCompatActivity {
    private ListView listView;
    private BtAdapter adapter;
    private BluetoothAdapter btAdapter;
    private List<ListItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_list);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bt_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        } else if(item.getItemId() == R.id.id_search){
            ListItem itemTitle = new ListItem();
            itemTitle.setItemType(BtAdapter.TITLE_ITEM_TYPE);
            list.add(itemTitle);
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    private void init(){
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
    }

    @SuppressLint("MissingPermission")
    private void getPairedDevices(){
        @SuppressLint("MissingPermission")
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        System.out.println("LOG pairedDevices "+pairedDevices);

        if(pairedDevices.size()>0){
            System.out.println("LOG pairedDevices.size() "+pairedDevices.size());
            list.clear();
            for(BluetoothDevice device : pairedDevices){
                ListItem item = new ListItem();
                item.setBtName(device.getName());
                item.setBtMac(device.getAddress());//MAC
                System.out.println("LOG item "+item);
                System.out.println("LOG item name "+item.getBtName());
                System.out.println("LOG item name "+item.getBtMac());
                list.add(item);
            }
            adapter.notifyDataSetChanged();
        }else System.out.println("LOG pairedDevices.size() "+pairedDevices.size());
    }

}








































