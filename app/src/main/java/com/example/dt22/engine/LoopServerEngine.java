package com.example.dt22.engine;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class LoopServerEngine implements Runnable {

    private final float FPS = 60;
    private final float SECOND = 1000000000;
    private final float UPDATE_TIME = SECOND/FPS;

    private boolean runningServer = false;

    Thread serverSessionThread = null;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    @SuppressLint("NewApi")
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    //TEMP
    float updates = 0;
    float drawing = 0;
    long timer = 0;
    //TEMP


    @Override
    public void run() {
        System.out.println("LoopServerFW public void run()");
        float lastTime = System.nanoTime();
        float delta = 0;
        timer = System.currentTimeMillis();

        while(runningServer){
            float nowTime = System.nanoTime();
            float elapsedTime = nowTime-lastTime;
            lastTime = nowTime;
            delta += elapsedTime/UPDATE_TIME;
            if(delta>1){
                updateServerSession();
                drawingServerSession();
                delta--;
            }
            if(System.currentTimeMillis()-timer>1000){
                Date date = new Date();
                //System.out.println("UPDATES "+updates+" DRAWING "+drawing+" DATE "+date.toString());
                updates=0;
                drawing=0;
                timer+=1000;
            }
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

    private void updateServerSession(){
        updates++;
    }

    private void drawingServerSession(){
        drawing++;
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



}
