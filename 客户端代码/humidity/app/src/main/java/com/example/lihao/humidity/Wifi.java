package com.example.lihao.humidity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Wifi extends AppCompatActivity {
    private Socket socket = null;
    private OutputStream outputStream=null;//定义输出流
    private InputStream inputStream=null;//定义输入流
    private Button sendWifiMgs;
    private Button setWifi;
    private Button continueSet;
    private TextView wifiName;
    private TextView wifiPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        ConnectThread connectThread = new ConnectThread();
        connectThread.start();


        setWifi = (Button) findViewById(R.id.setWifi);
        sendWifiMgs = (Button) findViewById(R.id.send1);
        continueSet = (Button) findViewById(R.id.continue_set);
        wifiName = (TextView) findViewById(R.id.wifi_name_input);
        wifiPassword = (TextView) findViewById(R.id.wifi_password_input);
        setButtonStatus();
        sendWifiMgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendMessage("1");
                    startWifi();
                    finish();
                }
                catch (Exception e){
                    Log.i("发送错误",e.toString());
                }
            }
        });

        setWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendMessage(getWifiConfig());
                    startWifi();
                    finish();
                }
                catch (Exception e){
                    Log.i("发送错误",e.toString());
                }
            }
        });
        continueSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddDevice();
                finish();
            }
        });
    }

    public String getWifiConfig(){
        return wifiName.getText().toString()+"\n"+wifiPassword.getText().toString();
    }

    public void startWifi() {
        Intent intent = new Intent(Wifi.this, Wifi.class);
        startActivity(intent);
    }
    public void startAddDevice() {
        Intent intent = new Intent(Wifi.this, AddDevice.class);
        startActivity(intent);
    }

    //连接
    class ConnectThread extends Thread{
        @Override
        public void run(){
            try{
                if (socket == null){
                    InetAddress ipAddress = InetAddress.getByName("192.168.4.1");
                    int port = 80;
                    socket = new Socket(ipAddress,port);
                    ReceiveThread receiveThread = new ReceiveThread();
                    receiveThread.start();
                }
            }
            catch (Exception e){

            }
        }
    }
    //接收
    class ReceiveThread extends Thread{
        @Override
        public void run(){
            try{
                while (true){
                    final byte[] buffer = new byte[61440];
                    inputStream = socket.getInputStream();
                    final int len = inputStream.read(buffer);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (len>0){
                                String temp = new String(buffer,0,len);
                                String[] statusMac;
                                Log.i("长度",Integer.toString(temp.length()));
                                try {
                                    statusMac = temp.split(":");
                                    Log.i("状态",statusMac[0]);
                                    Log.i("MAC",statusMac[1]);
                                    Toast.makeText(getApplicationContext(),statusDispay(statusMac[0])+":"+statusMac[1],Toast.LENGTH_SHORT).show();
                                    SharedPreferences addDevice = getSharedPreferences("ADD_DEVICE",0);
                                    SharedPreferences.Editor editor = addDevice.edit();
                                    editor.putString("IS_CONNECT",statusMac[0]);
                                    editor.putString("MAC",statusMac[1]);
                                    editor.commit();
                                }
                                catch (Exception e){
                                    Log.i("分解出错",e.toString());
                                }
//                                Log.i("接受",new String(buffer,0,len));
                            }
                        }
                    });
                }
            }
            catch (Exception e){

            }
        }
    }
    //发送消息
    public void sendMessage(String msg) throws IOException {
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(msg.getBytes());
        }
        catch (Exception e){

        }

    }
    public String statusDispay(String i){
        switch (i){
            case "0":
                return "空闲";
            case "1":
                return "正在连接";
            case "2":
                return "密码错误";
            case "3":
                return "无此Wi-Fi";
            case "4":
                return "连接失败";
            case "5":
                return "正在获取IP";
        }
        return "未知错误";
    }
    private void setButtonStatus(){
        continueSet.setEnabled(false);
        SharedPreferences addDevice = getSharedPreferences("ADD_DEVICE",0);
        String isConnect = addDevice.getString("IS_CONNECT","");
        String mac = addDevice.getString("MAC","");
        if (isConnect.equals("5")&&(mac.length() == "18-FE-34-AC-55-DF".length())){
            continueSet.setEnabled(true);
        }
    }
}






























