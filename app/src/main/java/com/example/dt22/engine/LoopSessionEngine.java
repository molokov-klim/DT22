package com.example.dt22.engine;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;

import com.example.dt22.SessionActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Base64;

public class LoopSessionEngine implements Runnable {

    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private boolean runningSession = false;
    private final Handler handler = new Handler();
    private Thread sessionThread = null;
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    @SuppressLint("NewApi")
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe


    @Override
    public void run() {
        try {
            String ip = "188.127.239.39";
            socket = new Socket(ip, 9700);
            output = new PrintWriter(socket.getOutputStream());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    output.write("startSession "+ SessionActivity.token);
                    output.flush();
                    output.close();
                }
            });
            while(runningSession){
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String messageFromServer = dataInputStream.readUTF();
                System.out.println("LOG messageFromServer: "+messageFromServer);
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


    public void startSession(){
        if (runningSession){return;}
        runningSession =true;
        sessionThread = new Thread(this);
        sessionThread.start();
    }

    public void stopSession(){
        if(!runningSession){return;}
        runningSession =false;
        try {
            sessionThread.join();
            socket.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
