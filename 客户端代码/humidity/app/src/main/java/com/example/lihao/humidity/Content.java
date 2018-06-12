package com.example.lihao.humidity;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.lihao.humidity.R.drawable.wanshouju;
//import android.widget.TextView;



public class Content extends AppCompatActivity {

    private List<Humidity> humidityList = new ArrayList<Humidity>();

    private GetFlowerImageId flowerImageId = new GetFlowerImageId();

    private HttpGetHumidity httpHumidity = new HttpGetHumidity();

    private String humidityString;

    private SplitString splitRawString = new SplitString();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //set_data(home_data);
                    //TODO
                    new Thread(networkTask).start();
                    return true;
                case R.id.navigation_dashboard:
                    //set_data(flowers_data);
                    startListFlower();
                    finish();
                    //mTextMessage.setText("花草");

                    return true;
                case R.id.navigation_notifications:
                    //set_data(user_data);
                    startUser();
                    finish();
                    //mTextMessage.setText("用户");
                    return true;
            }
            return false;
        }
    };


//启动user界面
    public void startUser(){
        Intent intent = new Intent(Content.this,User.class);
        startActivity(intent);
    }
    public void startListFlower(){
        Intent intent = new Intent(Content.this,ListFlower.class);
        startActivity(intent);
    }
    public void startFLowerDetail(){
        Intent intent = new Intent(Content.this,FlowerDetail.class);
        startActivity(intent);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);



        new Thread(networkTask).start();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void set_data(){
//        initFlowers();
        HumidityAdapter adapter = new HumidityAdapter(Content.this,R.layout.humidity_item,humidityList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(),"数据已更新",Toast.LENGTH_SHORT).show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences userInfo = getSharedPreferences("FLOWER_DETAIL",0);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.clear();
                editor.commit();
                editor.putString("FLOWER_OTHER_NAME",humidityList.get(i).getFlowerName());
                editor.commit();
                startFLowerDetail();
                Log.i("item",Integer.toString(i));
//                Toast.makeText(getApplicationContext(),humidityList.get(i).getFlowerName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String setUrl(){
        SharedPreferences userInfo = getSharedPreferences("USER_INFO",0);
        String requestURL = "http://120.79.52.102/getData/?uid="+userInfo.getString("USER_ID","")+"&tkn="+userInfo.getString("TOKEN","");
        Log.i("URL",requestURL);
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
                // TODO
                String[] flowerItem =  splitRawString.splitString(val,";");
                String[] nameAndhumidity;
                String[] humidityAndTime;
                String[] flowerNames;
                Humidity temp;
                String renewTime = "最后同步时间:";
                humidityList.clear();
                for (int i = 0; i < flowerItem.length; i++){
                    nameAndhumidity = splitRawString.splitString(flowerItem[i],":");
                    flowerNames = nameAndhumidity[0].split(",");
                    humidityAndTime = nameAndhumidity[1].split(",");

                    try {
                        renewTime = "最后同步时间:";
                        renewTime = renewTime+humidityAndTime[3]+"-"+humidityAndTime[4]+"-"+humidityAndTime[5]+"  "+humidityAndTime[6]+":"+humidityAndTime[7];
                        //Log.i("time:",time);
                    }
                    catch (Exception e){
                        Log.i("timeException",e.toString());
                    }

                    //TODO 查表对应mac的花的别名 第一个形参
                    temp = new Humidity(flowerNames[1],flowerImageId.getFlowerImageId(flowerNames[0]),Integer.parseInt(humidityAndTime[0]),Integer.parseInt(humidityAndTime[1]),Integer.parseInt(humidityAndTime[2]),renewTime);
                    humidityList.add(temp);
                }
                set_data();//更新界面
                Log.i("更新界面:","成功");
                // UI界面的更新等相关操作
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
                //mTextMessage.setText("正在更新数据...");
                //Log.i("URL",setUrl());
                humidityString = httpHumidity.main(setUrl());
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", humidityString);
                msg.setData(data);
                handler.sendMessage(msg);
            }
            catch (Exception e){
                //mTextMessage.setText(e.toString());
                Log.i("run:",e.toString());
            }
            Log.i("run","结束");
        }
    };


}
