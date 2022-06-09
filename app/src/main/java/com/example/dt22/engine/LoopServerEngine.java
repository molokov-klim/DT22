package com.example.dt22.engine;

import android.annotation.SuppressLint;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.text.format.Formatter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dt22.R;
import com.example.dt22.ServerSessionActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class LoopServerEngine implements Runnable {

    ServerSocket serverSocket;
    Socket socketServerSide;
    DataInputStream dataInputStream;
    String messageFromClient;
    private boolean runningServer = false;
    Handler handler = new Handler();
    Thread serverSessionThread = null;


    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    @SuppressLint("NewApi")
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe


    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(9700);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("LOG waiting for client");
                }
            });

            while(runningServer){
                socketServerSide = serverSocket.accept();
                dataInputStream = new DataInputStream(socketServerSide.getInputStream());
                messageFromClient = dataInputStream.readUTF();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return base64Encoder.encodeToString(randomBytes);
        }
        return null;
    }


    public void startServerSession(){

        if (runningServer){return;}
        runningServer=true;
        serverSessionThread = new Thread(this);
        serverSessionThread.start();
    }

    public void stopServerSession(){
        if(!runningServer){return;}
        runningServer=false;

        try {
            serverSessionThread.join();
            serverSocket.close();
            socketServerSide.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }


    }



}
