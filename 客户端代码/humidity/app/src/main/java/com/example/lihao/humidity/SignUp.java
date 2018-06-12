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

public class SignUp extends AppCompatActivity {
    private Button getCode;
    private Button signUp;
    private int requestType;
    private TextView userIdText;
    private TextView passwordText;
    private TextView codeText;
    private HttpGetHumidity signUpHttp = new HttpGetHumidity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getCode = (Button)findViewById(R.id.getCode);
        signUp = (Button)findViewById(R.id.signUp);
        userIdText = (TextView)findViewById(R.id.userIdText);
        passwordText = (TextView)findViewById(R.id.passwordText);
        codeText = (TextView)findViewById(R.id.codeText);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestType = 0;
                if (isEmail(userIdText.getText().toString())){
                    new Thread(networkTask).start();
                    Toast.makeText(getApplicationContext(),"获取验证码",Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(getApplicationContext(),"邮件格式错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestType = 1;
                if (isEmail(userIdText.getText().toString())&&isEnoughLen(passwordText.getText().toString())){
                    new Thread(networkTask).start();
                    Toast.makeText(getApplicationContext(),"正在注册",Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(getApplicationContext(),"邮件格式错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public boolean dealRequestReturn(String requestReturn){
        try {
            String[] temp = requestReturn.split(":");
            String q = temp[1].replace("\n","");
            if (temp[0].equals("userResult")){
                switch (q){
                    case "codeCreate":
                        Log.i("dealRRequestReturn1","UserIdError");
                        Toast.makeText(getApplicationContext(),"验证码已发送",Toast.LENGTH_SHORT).show();
                        getCode.setEnabled(false);
                        return false;
                    case "codeCreateFail":
                        Log.i("dealRRequestReturn2","outTime");
                        Toast.makeText(getApplicationContext(),"验证码发送失败",Toast.LENGTH_SHORT).show();
                        return false;
                    case "userIdExist":
                        Log.i("dealRRequestReturn3","userIdNoExist");
                        Toast.makeText(getApplicationContext(),"用户名存在",Toast.LENGTH_SHORT).show();
                        return false;
                    case "codeNoExist":
                        Log.i("dealRRequestReturn3","flowerNoExist");
                        Toast.makeText(getApplicationContext(),"验证码不存在，请获取验证码",Toast.LENGTH_SHORT).show();
                        getCode.setEnabled(true);
                        return false;
                    case "codeError":
                        Log.i("dealRRequestReturn3","flowerOtherNameExist");
                        Toast.makeText(getApplicationContext(),"验证码错误",Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        //成功返回token
                        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
                        saveIdToken(userIdText.getText().toString(),temp[1]);
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
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
    public void saveIdToken(String id,String token){
        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("USER_ID",id);
        editor.putString("TOKEN",token);
        editor.commit();
    }

    public String getRequestURL(int requestType){
        String tempURL = null;
        if (requestType == 0){
            tempURL = "http://120.79.52.102/sign_up_code/?uid="+userIdText.getText().toString();
        }
        else if (requestType == 1){
            String pwd = MD5.getMD5(passwordText.getText().toString());
            String cd = codeText.getText().toString();
            tempURL = "http://120.79.52.102/sign_up/?uid="+userIdText.getText().toString()+"&cd="+cd+"&pwd="+pwd;
            Log.i("URL",tempURL);
        }
        return tempURL;
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
                String userString = signUpHttp.main(getRequestURL(requestType));
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
