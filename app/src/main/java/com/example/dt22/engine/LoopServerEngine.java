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
    Handler handler = new Handler();

    private boolean runningServer = false;

    Thread serverSessionThread = null;


    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    @SuppressLint("NewApi")
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe


    @Override
    public void run() {
        System.out.println("LoopServerEngine public void run()");
        System.out.println("LOG LoopServerEngine public void run()");

        try {
            serverSocket = new ServerSocket(9700);

            System.out.println("LOG serverSocket " +serverSocket);
            System.out.println("LOG serverSocket.getInetAddress() " +serverSocket.getInetAddress());
            System.out.println("LOG serverSocket.getLocalPort() " +serverSocket.getLocalPort());
            System.out.println("LOG serverSocket.getLocalSocketAddress() " +serverSocket.getLocalSocketAddress());


            handler.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("LOG waiting for client");
                }
            });

            System.out.println("LOG runningServer is "+runningServer);

            while(runningServer){
                socketServerSide = serverSocket.accept();
                dataInputStream = new DataInputStream(socketServerSide.getInputStream());

                messageFromClient = dataInputStream.readUTF();

                System.out.println("LOG dataInputStream "+dataInputStream);
                System.out.println("LOG messageFromClient "+messageFromClient);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(messageFromClient.equals("Open camera")){
                            System.out.println("LOG Opening camera");
                        } else if(messageFromClient.equals("Open gallery")){
                            System.out.println("LOG Opening gallery");
                        } else {
                            System.out.println("LOG message from client is"+messageFromClient);
                        }

                    }
                });

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
