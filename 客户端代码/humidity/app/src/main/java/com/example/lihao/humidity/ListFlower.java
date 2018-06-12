package com.example.lihao.humidity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListFlower extends AppCompatActivity {

    private List<Flowers> flowersList = new ArrayList<Flowers>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    startContent();
                    finish();
                    return true;
                case R.id.navigation_dashboard:

                    //TODO
                    set_data();
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    startUser();
                    finish();
                    return true;
            }
            return false;
        }
    };

    public void startContent(){
        Intent intent = new Intent(ListFlower.this,Content.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
    }
    public void startUser(){
        Intent intent = new Intent(ListFlower.this,User.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
    }
    public void startWifi(){
        Intent intent = new Intent(ListFlower.this,Wifi.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_flower);


        set_data();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(navigation.getMenu().getItem(1).getItemId());
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    public void set_data(){
        initFlowers();

        FlowerAdapter adapter = new FlowerAdapter(ListFlower.this,R.layout.flower_item,flowersList);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView textView = (TextView) findViewById(i);
                SharedPreferences addDevice = getSharedPreferences("ADD_DEVICE",0);
                SharedPreferences.Editor editor = addDevice.edit();
                editor.clear();
                editor.commit();
                editor.putString("FLOWER_NAME",flowersList.get(i).getName());
                editor.commit();
                Toast.makeText(getApplicationContext(),"点击了"+flowersList.get(i).getName(),Toast.LENGTH_SHORT).show();
                startWifi();
            }
        });
    }


    //初始化花草列表
    private void initFlowers(){
        flowersList.clear();
        Flowers test1 = new Flowers("万寿菊",R.drawable.wanshouju,"为菊科万寿菊属一年生草本植物，茎直立，粗壮，具纵细条棱，分枝向上平展。");
        flowersList.add(test1);
        Flowers test2 = new Flowers("香雪兰",R.drawable.xiangxuelan,"属鸢尾科，香雪兰属多年生球根草本花卉。");
        flowersList.add(test2);
        Flowers test3 = new Flowers("多肉",R.drawable.duorou,"根、茎、叶三种营养器官中至少有一种是肥厚多汁并且具备储藏大量水分功能的植物。");
        flowersList.add(test3);
        Flowers test4 = new Flowers("紫罗兰",R.drawable.ziluolan,"全株密被灰白色具柄的分枝柔毛。茎直立，多分枝，基部稍木质化。");
        flowersList.add(test4);
    }

}
