//package com.example.dt22;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class TestActivity extends AppCompatActivity {
//
//    EditText e1,e2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        // Client
//        e1 = (EditText) findViewById(R.id.et_IP);
//        e2 = (EditText) findViewById(R.id.et_message);
//
//
//        // Server
//        Thread myThread = new Thread(new MyServer());
//        myThread.start();
//
//    }
//
//    // CLIENT
//
//    public void ButtonClick(View v) {
//        BackgroundTask b = new BackgroundTask();
//        b.execute(e1.getText().toString(), e2.getText().toString());
//    }
//
//    class BackgroundTask extends AsyncTask<String, Void, String>
//    {
//
//        Socket socketClientSide;
//        DataOutputStream dataOutputStream;
//        String serverIP, messageToServer;
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            serverIP = params[0];
//            messageToServer = params [1];
//
//
//            try {
//                socketClientSide = new Socket(serverIP, 9700);
//                dataOutputStream = new DataOutputStream(socketClientSide.getOutputStream());
//                dataOutputStream.writeUTF(messageToServer);
//
//                dataOutputStream.close();
//                socketClientSide.close();
//
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }
//
//    // SERVER
//
//    class MyServer implements Runnable {
//
//        ServerSocket serverSocket;
//        Socket socket;
//        DataInputStream dataInputStream;
//
//        String messageFromClient;
//        Handler handler = new Handler();
//
//
//        @Override
//        public void run() {
//
//
//            try {
//                serverSocket = new ServerSocket(9700);
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "waiting for client: ", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//
//                while(true){
//                    socket = serverSocket.accept();
//                    dataInputStream = new DataInputStream(socket.getInputStream());
//
//                    messageFromClient = dataInputStream.readUTF();
//
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(messageFromClient.equals("Open camera")){
//                                Toast.makeText(getApplicationContext(), "Opening camera", Toast.LENGTH_SHORT).show();
//                            } else if(messageFromClient.equals("Open gallery")){
//                                Toast.makeText(getApplicationContext(), "Opening gallery", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "message from client: "+ messageFromClient, Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//
//                }
//
//
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
