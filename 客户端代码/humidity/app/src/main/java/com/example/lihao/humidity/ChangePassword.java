package com.example.lihao.humidity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {

    private TextView oldPasswordText;
    private TextView newPasswordText;
    private HttpGetHumidity getUserInfo = new HttpGetHumidity();
    private String userString;
    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPasswordText = (TextView) findViewById(R.id.old_password_input);
        newPasswordText = (TextView) findViewById(R.id.new_password_input);
        changePassword = (Button) findViewById(R.id.change_password) ;

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("URL",getRequestURL());
                startRequest();
            }
        });

    }


    public boolean isEnoughLen(String password){
        if (password.length() >= 6){
            Log.i("isPassword","yes");
        }
        else {
            Toast.makeText(getApplicationContext(),"密码长度不够",Toast.LENGTH_SHORT).show();
        }
        return (password.length() >=6);
    }


    public void startRequest(){
        if (isEnoughLen(newPasswordText.getText().toString())&&isEnoughLen(oldPasswordText.getText().toString())){
            new Thread(networkTask).start();
        }
        else {
//            Toast.makeText(getApplicationContext(),"邮件格式错误",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean dealRequestReturn(String requestReturn){
        try {
            String[] temp = requestReturn.split(":");
            String q = temp[1].replace("\n","");
            if (temp[0].equals("userResult")){
                switch (q){
                    case "userIdError":
                        Log.i("dealRRequestReturn1","userIdError");
                        Toast.makeText(getApplicationContext(),"用户名错误",Toast.LENGTH_SHORT).show();
                        return false;
                    case "outTime":
                        Log.i("dealRRequestReturn2","tokenOutTime");
                        Toast.makeText(getApplicationContext(),"token超时",Toast.LENGTH_SHORT).show();
                        return false;
                    case "tokenError":
                        Log.i("dealRRequestReturn3","tokenError");
                        Toast.makeText(getApplicationContext(),"token错误",Toast.LENGTH_SHORT).show();
                        return false;
                    case "passwordError":
                        Log.i("dealRRequestReturn3","passwordError");
                        Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        //成功返回token
                        Log.i("dealRRequestReturn4",temp[1]);
                        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
                        String userId = userInfo.getString("USER_ID","");
                        saveIdToken(userId.toString(),temp[1]);
                        Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
//                        startContent();
                        finish();
                        return true;
                }
            }
            else {
                Log.i("dealRequestReturn","不相等");
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Log.i("dealRequestReturn",e.toString());
        }
        Log.i("dealRequestReturn","end");
        return false;
    }

//保存请求回的信息
    public void saveIdToken(String id,String token){
        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("USER_ID",id);
        editor.putString("TOKEN",token);
        editor.commit();
    }

//获取请求URL
    public String getRequestURL(){
        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
        String userId = userInfo.getString("USER_ID","");
        String token = userInfo.getString("TOKEN","");
        String requestURL = "http://120.79.52.102/change_password/?uid="+userId+"&pwd="+MD5.getMD5(oldPasswordText.getText().toString())+"&npwd="+MD5.getMD5(newPasswordText.getText().toString())+"&tkn="+token;
        return requestURL;
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
                userString = getUserInfo.main(getRequestURL());
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
