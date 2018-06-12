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
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private Button signUp;
    private Button signIn;
    private TextView userIdText;
    private TextView passwordText;
    private Button forgetPassword;
    private String userString;
    private String requestWay;
    private String requestKeyWord;
    private HttpGetHumidity getUserInfo = new HttpGetHumidity();


    //TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signUp = (Button) findViewById(R.id.sign_up) ;
        userIdText = (TextView)findViewById(R.id.user_id_input);
        passwordText = (TextView) findViewById(R.id.password_input);
        forgetPassword = (Button) findViewById(R.id.forgetPassword);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                requestWay = "sign_up";
//                requestKeyWord = "pwd";
//                Log.i("URL",getRequestURL(requestWay,requestKeyWord));
//                startRequest();
                startSignUp();
            }
        });
        signIn = (Button) findViewById(R.id.sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MD5
                requestWay = "sign_in";
                requestKeyWord = "pwd";
                Log.i("URL",getRequestURL(requestWay,requestKeyWord));
                startRequest();
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startForgetPassword();
            }
        });
        //Log.i("USER_ID:",userInfo.getString("USER_ID",""));


    }
    public void startContent(){
        Intent intent = new Intent(Login.this,Content.class);
        startActivity(intent);

    }
    public void startSignUp(){
        Intent intent = new Intent(Login.this,SignUp.class);
        startActivity(intent);

    }
    public void startForgetPassword(){
        Intent intent = new Intent(Login.this,ForgetPassword.class);
        startActivity(intent);

    }

    public String getRequestURL(String requestWay,String requestKeyWord) {
        return "http://120.79.52.102/"+requestWay+"/?uid="+userIdText.getText().toString()+"&"+requestKeyWord+"="+MD5.getMD5(passwordText.getText().toString());
    }

    public void saveIdToken(String id,String token){
        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("USER_ID",id);
        editor.putString("TOKEN",token);
        editor.commit();
        //Log.i("测试token",userInfo.getString("TOKEN",""));
    }

    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        if (m.matches() == true){
            Log.i("isMail","yes");
        }
        else {
            Toast.makeText(getApplicationContext(),"邮件格式错误",Toast.LENGTH_SHORT).show();
            Log.i("isMail","no");
        }
        return m.matches();
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
        if (isEmail(userIdText.getText().toString())&&isEnoughLen(passwordText.getText().toString())){
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
                    case "wrong":
                        Log.i("dealRRequestReturn1","wrong");
                        Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                        return false;
                    case "userIdExist":
                        Log.i("dealRRequestReturn2","exist");
                        Toast.makeText(getApplicationContext(),"账户已存在",Toast.LENGTH_SHORT).show();
                        return false;
                    case "noUserId":
                        Log.i("dealRRequestReturn3","noexist");
                        Toast.makeText(getApplicationContext(),"账户不存在",Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        //成功返回token
                        Log.i("dealRRequestReturn4",temp[1]);
                        saveIdToken(userIdText.getText().toString(),temp[1]);
                        startContent();
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
                userString = getUserInfo.main(getRequestURL(requestWay,requestKeyWord));
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
