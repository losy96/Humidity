package com.example.lihao.humidity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.example.lihao.humidity.Content;

public class MainActivity extends AppCompatActivity {

    private String isLogin;
    private HttpGetHumidity login = new HttpGetHumidity();

    public void startContent(){
        Intent intent = new Intent(MainActivity.this,Content.class);
        startActivity(intent);

    }

    public void startLogin(){
        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);

    }





    public boolean dealRequestReturn(String requestReturn) throws InterruptedException {
        try {
            String[] temp = requestReturn.split(":");
            String q = temp[1].replace("\n","");
            if (temp[0].equals("userResult")){
                switch (q){
                    case "outTime":
                        Log.i("dealRRequestReturn1","outTime");
                        Thread.currentThread().sleep(2000);
                        Toast.makeText(getApplicationContext(),"token已过期，请重新登陆",Toast.LENGTH_SHORT).show();
                        startLogin();
                        finish();
                        return false;
                    case "false":
                        Log.i("dealRRequestReturn2","falseToken");
                        Thread.currentThread().sleep(2000);
                        Toast.makeText(getApplicationContext(),"token错误",Toast.LENGTH_SHORT).show();
                        startLogin();
                        finish();
                        return false;
                    case "true":
                        Log.i("dealRRequestReturn2","trueToken");
                        Thread.currentThread().sleep(2000);
                        Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                        startContent();
                        finish();
                        return true;
                }
            }
            else {
                Log.i("dealRequestReturn","不相等");
                Thread.currentThread().sleep(2000);
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
                startLogin();
                finish();
                return false;
            }
        }
        catch (Exception e){
            Log.i("dealRequestReturn",e.toString());
        }
        Log.i("dealRequestReturn","end");
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startLogin();
        finish();
        return false;
    }

    public String getRequestURL() {
        SharedPreferences userInfo = getSharedPreferences("USER_INFO", 0);
        String userId = userInfo.getString("USER_ID", "");
        String token = userInfo.getString("TOKEN", "");
        String requestURL;
        if (userId.length() == 0) {
            return "";
        } else {
            requestURL = "http://120.79.52.102/compare_token/?uid=" + userId + "&tkn=" + token;
            Log.i("URL",requestURL);
        }
        return requestURL;
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (getRequestURL().length() == 0){
            Toast.makeText(getApplicationContext(),"无用户",Toast.LENGTH_SHORT).show();
            startLogin();
            finish();
        }
        else {
            new Thread(networkTask).start();
        }
        //TODO



        //判断是否登陆成功  如不成功跳转至登陆界面





        //setContentView(R.layout.activity_content);
    }
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            try {
                dealRequestReturn(val);
            }
            catch (Exception e){
                Log.i("线程",e.toString());
            }
            // TODO
            // UI界面的更新等相关操作
        }
    };

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {

            //TODO
            try {
                isLogin = login.main(getRequestURL());
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", isLogin);
                msg.setData(data);
                handler.sendMessage(msg);
            }
            catch (Exception e){
                Log.i("是否登陆：",e.toString());
            }

        }
    };
}
