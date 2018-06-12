//package com.example.lihao.humidity;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//public class UserKey extends AppCompatActivity{
//    private static Context context;
//    public static void saveUserKey(String userId,String token){
//        SharedPreferences userInfo = context.getSharedPreferences("USER_KEY",0);
//        SharedPreferences.Editor editor = userInfo.edit();
//        editor.putString("userId",userId);
//        editor.putString("token",token);
//        editor.commit();
//        Log.i("保存用户信息：","成功");
//    }
//    public static String getUserId(){
//        SharedPreferences userInfo = context.getSharedPreferences("USER_KEY",0);
//        String userId = userInfo.getString("userId","");
//        Log.i("userId",userId);
//        return userId;
//    }
//    public static String getToken(){
//        SharedPreferences userInfo = context.getSharedPreferences("USER_KEY",0);
//        String token = userInfo.getString("token","");
//        Log.i("token",token);
//        return token;
//    }


//}
