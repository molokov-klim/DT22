package com.example.dt22.engine;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;

import com.example.dt22.HostSessionActivity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Base64;

public class LoopSession implements Runnable {

    Socket socket;
    PrintWriter output;
    DataInputStream input;
    boolean runningSession = false;
    Handler handler = new Handler();
    Thread sessionThread = null;
    String messageFromServer;
    String messageToServerStartSession;
    String messageToServerStopSession;
    String ip = "188.127.239.39";
    int port = 9700;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    @SuppressLint("NewApi")
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe



    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);

            System.out.println("LOG sending message to server");
            messageToServerStartSession = "startSession "+ HostSessionActivity.token;
            output = new PrintWriter(socket.getOutputStream());
            output.write(messageToServerStartSession);
            output.flush();
            socket.shutdownOutput();


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
            socket = new Socket(ip, port);
            messageToServerStopSession = "stopSession "+ HostSessionActivity.token;
            output = new PrintWriter(socket.getOutputStream());
            output.write(messageToServerStopSession);
            output.flush();
            output.close();
            socket.close();
            sessionThread.join();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
