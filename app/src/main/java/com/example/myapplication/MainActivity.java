package com.example.myapplication;
import android.annotation.SuppressLint;
import android.os.Bundle;
//import android.support.v11.app.;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    Thread Thread1 = null;
    EditText etIP, etPort;
    TextView tvMessages;
    EditText etMessage;
    Button btnSend;
    String SERVER_IP;
    int SERVER_PORT  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etIP = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        tvMessages = findViewById(R.id.tvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        Button btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessages.setText("");
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
//                Log.d("test" , SERVER_IP);
//                Log.d("test" , "" + SERVER_PORT  );

                Thread1 = new Thread(new Thread1());
                Thread1.start();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    new Thread(new Thread3(message)).start();
                }
            }
        });
    }

    private PrintWriter output;
    private BufferedReader input;

    class Thread1 implements Runnable {
        Socket socket = null ;
        @Override
        public void run() {

            try {
                Log.d("test" , SERVER_IP);
                Log.d("test" , "" + SERVER_PORT  );
                Log.d("test" , "hi");
                socket = new Socket(SERVER_IP, SERVER_PORT);
                Log.d("test" , "h");
                output = new PrintWriter(socket.getOutputStream());
                Log.d("test" , "hi3");
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d("test" , "hi2");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMessages.setText("Connected\n");
                    }
                });
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMessages.append("server: " + message + "\n");
                            }
                        });
                    }else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Thread3 implements Runnable {
        private String message;

        Thread3(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            output.write(message);
            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMessages.append("client: " + message + "\n");
                    etMessage.setText("");
                }
            });
        }

        private void runOnUiThread(Runnable runnable) {
        }


    }
}

//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.os.StrictMode;
////import android.support.v11.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//
//public class MainActivity extends AppCompatActivity {
//
//
//    EditText edTxt;
//    myThread myThread ;
//   // Button btnSend;
//   //String SERVER_IP = "192.168.1.6";
//   //int SERVER_PORT = 5555;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        myThread = new myThread();
//        new Thread(myThread).start();
//        edTxt = findViewById(R.id.etMessage);
//    }
//
//    private class myThread implements Runnable {
//        private volatile String msg = "" ;
//        Socket socket ;
//        DataOutputStream dos ;
//        @Override
//
//        public void run() {
//            try {
//                socket = new Socket(" 192.168.1.6", 5677);
//                dos = new DataOutputStream(socket.getOutputStream());
//                dos.writeUTF(msg);
//                dos.close();
//                dos.flush();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        public void sendMsgParam(String msg){
//            this.msg = msg ;
//            run();
//        }
//
//    }
//    public void btnClickSnd (View v){
//       String msg = edTxt.getText().toString();
//       myThread.sendMsgParam(msg);
//    }
//}