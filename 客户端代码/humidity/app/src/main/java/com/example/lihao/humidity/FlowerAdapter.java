package com.example.lihao.humidity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


public class FlowerAdapter extends ArrayAdapter{

    private final int resourceId;

    public FlowerAdapter(Context context, int textViewResourceId, List<Flowers> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        //this.resourceId = resourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Flowers flowers = (Flowers) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView flowerImage = (ImageView) view.findViewById(R.id.flower_image);
        TextView flowerName = (TextView) view.findViewById(R.id.flower_name);
        TextView flowerDescribe = (TextView) view.findViewById(R.id.flower_describe);
        flowerImage.setImageResource(flowers.getImageId());
        flowerName.setText(flowers.getName());
        flowerDescribe.setText(flowers.getDescribe());
        return view;
    }
}
