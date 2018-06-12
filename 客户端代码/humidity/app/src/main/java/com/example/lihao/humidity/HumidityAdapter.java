package com.example.lihao.humidity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class HumidityAdapter extends ArrayAdapter {

    private final int resourceId;

    public HumidityAdapter(Context context, int textViewResourceId, List<Humidity> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        //this.resourceId = resourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Humidity flowers = (Humidity) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

        ImageView flowerImage = (ImageView) view.findViewById(R.id.flower_image);
        TextView flowerName = (TextView) view.findViewById(R.id.flower_name);
        ImageView remindHumidityImage = (ImageView) view.findViewById(R.id.humidity_remind_image);
        TextView remindHumidityText = (TextView) view.findViewById(R.id.humidity_remind_text);
        TextView renewTime = (TextView) view.findViewById(R.id.flower_renew_time);


        flowerImage.setImageResource(flowers.getFlowerImageId());
        flowerName.setText(flowers.getFlowerName());
        remindHumidityImage.setImageResource(flowers.getRemindImageId());
        remindHumidityText.setText(flowers.getRemindText());
        renewTime.setText(flowers.getRenewTime());
        return view;
    }
}
