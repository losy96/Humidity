package com.example.lihao.humidity;

import java.lang.ref.SoftReference;

public class Humidity {
    private String flowerName;
    private int flowerImageId;
    private int humidity;
    private String remindText;
    private int remindImageId;
    private int maxHumidity;
    private int minHumidity;
    private  String renewTime;

    public Humidity(String flowerName,int flowerImageId,int humidity,int maxHumidity,int minHumidity,String renewTime){
        this.flowerName = flowerName;
        this.flowerImageId = flowerImageId;
        this.humidity = humidity;
        this.maxHumidity = maxHumidity;
        this.minHumidity = minHumidity;
        this.renewTime = renewTime;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public int getFlowerImageId() {
        return flowerImageId;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getRemindText(){
        if (humidity>maxHumidity){
            remindText = "湿度过高";
        }
        else if (humidity<minHumidity){
            remindText = "湿度过低";
        }
        else {
            remindText = "湿度正常";
        }
        return remindText;
    }
    public int getRemindImageId(){
        if (humidity>maxHumidity){
            remindImageId = R.drawable.blue;
        }
        else if (humidity<minHumidity){
            remindImageId = R.drawable.red;
        }
        else {
            remindImageId = R.drawable.green;
        }
        return remindImageId;
    }
    public String getRenewTime(){
        return renewTime;
    }
}
