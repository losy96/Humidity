package com.example.lihao.humidity;

import android.util.Log;

public class SplitString {
    public String[] splitString(String rawString,String splitSymbol){
        String[] newString;
        newString = rawString.split(splitSymbol);
        for (int i = 0; i < newString.length; i++){
            Log.i("mylog", "分解结果" + newString[i]);
        }
        return newString;
    }
}
