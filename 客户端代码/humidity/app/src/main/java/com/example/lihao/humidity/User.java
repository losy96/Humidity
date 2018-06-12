package com.example.lihao.humidity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class User extends AppCompatActivity {

    //private TextView mTextMessage;
    private HttpGetHumidity test = new HttpGetHumidity();
    private List<Users> userList = new ArrayList<Users>();
    private String userId = "";
    private AlertDialog dialog;
    private String testText;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    startContent();
                    finish();
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    startListFlower();
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    //TODO
                    try {
                        set_data();
//                        new Thread(networkTask).start();
                    }catch (Exception e){
                        Log.i("user按钮:",e.toString());
                    }

                    return true;
            }
            return false;
        }
    };


    public void startContent(){
        Intent intent = new Intent(User.this,Content.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
    }
    public void startListFlower(){
        Intent intent = new Intent(User.this,ListFlower.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
    }
    public void startLogin(){
        Intent intent = new Intent(User.this,Login.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
    }

    public void startChangePassword(){
        Intent intent = new Intent(User.this,ChangePassword.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        set_data();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(navigation.getMenu().getItem(2).getItemId());
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void set_data(){
        initUsers();
        UsersAdapter adapter = new UsersAdapter(User.this,R.layout.user_item,userList);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("item",Integer.toString(i));
                switch (i){
                    case 0:
                        //用户名
                        Toast.makeText(getApplicationContext(),"用户名",Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        //修改密码
                        startChangePassword();
//                        Toast.makeText(getApplicationContext(),"修改密码",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        exitUser();
                        //退出登录
                        break;
                }


            }
        });
    }

    public void  exitUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("退出");
        builder.setMessage("确定退出登陆吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
                    SharedPreferences.Editor editor = userInfo.edit();
                    editor.clear();
                    editor.commit();
                    startLogin();
                    finish();
                    Toast.makeText(getApplicationContext(),"退出登陆成功",Toast.LENGTH_SHORT).show();
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
                dialog.dismiss();
            }
        });

        dialog = builder.create();

        dialog.show();
    }

    private void initUsers() {
        userList.clear();
        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
        userId = userInfo.getString("USER_ID","");
        Users test1 = new Users(userId, R.drawable.user);
        userList.add(test1);
        Users test2 = new Users("修改密码", R.drawable.key);
        userList.add(test2);
        Users test3 = new Users("退出登陆", R.drawable.exit);
        userList.add(test3);
    }
}
