package com.example.lihao.humidity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UsersAdapter extends ArrayAdapter {
    private final int resourceId;

    public UsersAdapter(Context context, int textViewResourceId, List<Users> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        //this.resourceId = resourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Users users = (Users) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView userImageId = (ImageView) view.findViewById(R.id.user_image);
        TextView usertext = (TextView) view.findViewById(R.id.user_text);


        userImageId.setImageResource(users.getUserImageid());
        usertext.setText(users.getUserText());
        return view;
    }
}
