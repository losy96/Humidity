package com.example.lihao.humidity;

public class Flowers {
    private String name;
    private int imageId;
    private String describe;

    public Flowers(String name ,int imageId,String describe){
        this.name = name;
        this.imageId = imageId;
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDescribe() {
        return describe;
    }
}
