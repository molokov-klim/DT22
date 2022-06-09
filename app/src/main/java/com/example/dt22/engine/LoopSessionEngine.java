package com.example.dt22.engine;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;

import com.example.dt22.SessionActivity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.SecureRandom;
import java.util.Base64;

public class LoopSessionEngine implements Runnable {

    Socket socket;
    PrintWriter output;
    DataInputStream input;
    boolean runningSession = false;
    Handler handler = new Handler();
    Thread sessionThread = null;
    String messageFromServer;
    String messageToServer;
    String ip = "188.127.239.39";

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    @SuppressLint("NewApi")
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe



    @Override
    public void run() {
        try {
            socket = new Socket(ip, 9700);

            System.out.println("LOG sending message to server");
            messageToServer = "startSession "+SessionActivity.token;
            output = new PrintWriter(socket.getOutputStream());
            output.write(messageToServer);
            output.flush();
            output.close();


            System.out.println("LOG socket .isReachable "+socket.getInetAddress().isReachable(5000));


            System.out.println("LOG socket is "+socket);
            System.out.println("LOG socket.isConnected() is "+socket.isConnected());
            System.out.println("LOG socket.isBound() is "+socket.isBound());
            System.out.println("LOG socket.isClosed() is "+socket.isClosed());
            System.out.println("LOG socket.isInputShutdown() is "+socket.isInputShutdown());
            System.out.println("LOG socket.isOutputShutdown() is "+socket.isOutputShutdown());
            System.out.println("LOG socket.getInputStream() is "+socket.getInputStream());
            System.out.println("LOG socket.toString() is "+socket.toString());
            System.out.println("LOG socket.getInetAddress() is "+socket.getInetAddress());
            System.out.println("LOG socket.getChannel() is "+socket.getChannel());
            System.out.println("LOG socket.getRemoteSocketAddress() is "+socket.getRemoteSocketAddress());









            while(runningSession){
                input = new DataInputStream(socket.getInputStream());
                messageFromServer = input.readUTF();
                System.out.println("LOG messageFromServer: "+messageFromServer);
                if(messageFromServer.equals("startSessionOK")){
                    System.out.println("LOG startSessionOK");
                }
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
            socket.close();
            sessionThread.join();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
