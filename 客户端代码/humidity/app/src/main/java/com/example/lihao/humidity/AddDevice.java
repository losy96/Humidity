package com.example.lihao.humidity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddDevice extends AppCompatActivity {
    private Button addDeviceButton;
    private TextView flowerOtherName;
    private HttpGetHumidity addNewDevice = new HttpGetHumidity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        flowerOtherName = (TextView) findViewById(R.id.other_name_input);
        addDeviceButton = (Button) findViewById(R.id.add_device);
        addDeviceButton.setEnabled(true);
        addDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("新增设备",getRequestURL(flowerOtherName.getText().toString()));
                new Thread(networkTask).start();
                addDeviceButton.setEnabled(false);
            }
        });
    }

    public String getRequestURL(String flowerOtherName) {
        SharedPreferences addDevice = getSharedPreferences("ADD_DEVICE",0);
        String flowerName = addDevice.getString("FLOWER_NAME","");
        String mac =  addDevice.getString("MAC","");
        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
        String userId = userInfo.getString("USER_ID","");
        String token = userInfo.getString("TOKEN","");
        Log.i("token",token);
        String URL = "http://120.79.52.102/device_add/?uid="+userId+"&mac="+mac+"&fn="+flowerName+"&fon="+flowerOtherName+"&tkn="+token;
        return URL;
    }

    public boolean dealRequestReturn(String requestReturn){
        try {
            String[] temp = requestReturn.split(":");
            String q = temp[1].replace("\n","");
            if (temp[0].equals("userResult")){
                switch (q){
                    case "userIdError":
                        Log.i("dealRRequestReturn1","UserIdError");
                        Toast.makeText(getApplicationContext(),"用户名错误",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        return false;
                    case "outTime":
                        Log.i("dealRRequestReturn2","outTime");
                        Toast.makeText(getApplicationContext(),"token超时",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        return false;
                    case "tokenError":
                        Log.i("dealRRequestReturn3","tokenError");
                        Toast.makeText(getApplicationContext(),"token错误",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        return false;
                    case "flowerNoExist":
                        Log.i("dealRRequestReturn3","flowerNoExist");
                        Toast.makeText(getApplicationContext(),"花卉不存在",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        return false;
                    case "flowerOtherNameExist":
                        Log.i("dealRRequestReturn3","flowerOtherNameExist");
                        Toast.makeText(getApplicationContext(),"别名已存在",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        return false;
                    case "flowerMacExist":
                        Log.i("dealRRequestReturn3","flowerMacExist");
                        Toast.makeText(getApplicationContext(),"flowerMacExist",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        return false;
                    case "deviceExist":
                        Log.i("dealRRequestReturn3","deviceExist");
                        Toast.makeText(getApplicationContext(),"设备已存在",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        return false;
                    case "ok":
                        Log.i("dealRRequestReturn3","ok");
                        Toast.makeText(getApplicationContext(),"新增成功",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        finish();
                        return true;
                    default:
                        //成功返回token
                        Toast.makeText(getApplicationContext(),"未知错误",Toast.LENGTH_SHORT).show();
                        addDeviceButton.setEnabled(true);
                        return false;
                }
            }
            else {
                Log.i("dealRequestReturn","不相等");
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
                addDeviceButton.setEnabled(true);
            }
        }
        catch (Exception e){
            Log.i("dealRequestReturn",e.toString());
            addDeviceButton.setEnabled(true);
        }
        Log.i("dealRequestReturn","end");
        addDeviceButton.setEnabled(true);
        return false;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            try {

                dealRequestReturn(val);

                //startContent();
                // TODO
            }
            catch (Exception e){
                Log.i("更新界面",e.toString());
            }

        }
    };

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {

            //TODO
            try {
                String userString = addNewDevice.main(getRequestURL(flowerOtherName.getText().toString()));
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", userString);
                msg.setData(data);
                handler.sendMessage(msg);

            }
            catch (Exception e){
                Log.i("run:",e.toString());
            }
            Log.i("run","结束");
        }
    };
}
