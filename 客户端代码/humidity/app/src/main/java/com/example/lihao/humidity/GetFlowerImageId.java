package com.example.lihao.humidity;

public class GetFlowerImageId {
    public int getFlowerImageId(String flowerName) {
        switch (flowerName){
            case "万寿菊":
                return R.drawable.wanshouju;
            case "多肉":
                return R.drawable.duorou;
            case "紫罗兰":
                return R.drawable.ziluolan;
            case "香雪兰":
                return R.drawable.xiangxuelan;
        }
        return R.drawable.wanshouju;
    }
}
