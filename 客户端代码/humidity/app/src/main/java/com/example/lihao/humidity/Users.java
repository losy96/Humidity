package com.example.lihao.humidity;

public class Users {
    private int userImageid;
    private String userText;

    public Users(String userText,int userImageid){
        this.userText = userText;
        this.userImageid = userImageid;
    }

    public int getUserImageid() {
        return userImageid;
    }

    public String getUserText() {
        return userText;
    }
}
