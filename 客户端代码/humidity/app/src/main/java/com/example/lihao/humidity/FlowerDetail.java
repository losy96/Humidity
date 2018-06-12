package com.example.lihao.humidity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FlowerDetail extends AppCompatActivity {

    private Button removeButton;
    private String flowerOtherName;
    private HttpGetHumidity removeOldDevice = new HttpGetHumidity();
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_detail);
        removeButton = (Button) findViewById(R.id.remove_device);
        removeButton.setEnabled(true);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeButton.setEnabled(false);
                removeFlower();
            }
        });
//        Log.i("URL",getRequestURL());
    }

    public void  removeFlower(){
        SharedPreferences flowerDetail = getSharedPreferences("FLOWER_DETAIL",0);
        flowerOtherName = flowerDetail.getString("FLOWER_OTHER_NAME","");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除设备");
        builder.setMessage("确定删除"+flowerOtherName+"吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    new Thread(networkTask).start();
                }
                catch (Exception e){
                    Log.i("退出登录",e.toString());
                }
//                Toast.makeText(User.this, "你点击了确定", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(User.this,"你点击了取消",Toast.LENGTH_LONG).show();
                removeButton.setEnabled(true);
                dialog.dismiss();
            }
        });

        dialog = builder.create();

        dialog.show();
    }

    public String getRequestURL() {
        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
        String userId = userInfo.getString("USER_ID","");
        String token = userInfo.getString("TOKEN","");
//        SharedPreferences flowerDetail = getSharedPreferences("FLOWER_DETAIL",0);
//        flowerOtherName = flowerDetail.getString("FLOWER_OTHER_NAME","");
        Log.i("token",token);
        String URL = "http://120.79.52.102/device_remove/?uid="+userId+"&ofn="+flowerOtherName+"&tkn="+token;
        Log.i("URL",URL);
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
                        removeButton.setEnabled(true);
                        return false;
                    case "outTime":
                        Log.i("dealRRequestReturn2","outTime");
                        Toast.makeText(getApplicationContext(),"token超时",Toast.LENGTH_SHORT).show();
                        removeButton.setEnabled(true);
                        return false;
                    case "tokenError":
                        Log.i("dealRRequestReturn3","tokenError");
                        Toast.makeText(getApplicationContext(),"token错误",Toast.LENGTH_SHORT).show();
                        removeButton.setEnabled(true);
                        return false;
                    case "deviceNoExist":
                        Log.i("dealRRequestReturn3","deviceNoExist");
                        Toast.makeText(getApplicationContext(),"设备不存在",Toast.LENGTH_SHORT).show();
                        removeButton.setEnabled(true);
                        return false;
                    case "ok":
                        Log.i("dealRRequestReturn3","ok");
                        Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        removeButton.setEnabled(true);
                        finish();
                        return true;
                    default:
                        //成功返回token
                        Toast.makeText(getApplicationContext(),"未知错误",Toast.LENGTH_SHORT).show();
                        removeButton.setEnabled(true);
                        return false;
                }
            }
            else {
                Log.i("dealRequestReturn","不相等");
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
                removeButton.setEnabled(true);
            }
        }
        catch (Exception e){
            Log.i("dealRequestReturn",e.toString());
            removeButton.setEnabled(true);
        }
        Log.i("dealRequestReturn","end");
        removeButton.setEnabled(true);
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
                String userString = removeOldDevice.main(getRequestURL());
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
