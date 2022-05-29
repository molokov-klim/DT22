package com.example.dt22.engine;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

public class BtConnectThread extends Thread {
    private BluetoothAdapter btAdapter;
    private BluetoothSocket mSocket;
    private BtReceiveThread rThread;
    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB"; // Идентификатор соединения (подходит для Arduino)

    // Конструктор потока подключения
    @SuppressLint("MissingPermission")
    public BtConnectThread(BluetoothAdapter btAdapter, BluetoothDevice device){
        this.btAdapter = btAdapter;
        try{
                mSocket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
                } catch (IOException e){

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        btAdapter.cancelDiscovery();
        try{
            mSocket.connect();
           rThread = new BtReceiveThread(mSocket);
           rThread.start();
            System.out.println("LOG Device connected");
            Log.d("MyLog", "Connected");
        } catch (IOException e){
            System.out.println("LOG Device not connected");
            Log.d("MyLog", "Not connected");
            closeConnection();
        }
    }

    public void closeConnection(){
        try{
            mSocket.close();
        } catch (IOException y){

        }
    }

    public BtReceiveThread getRThread() {
        return rThread;
    }
}





















































