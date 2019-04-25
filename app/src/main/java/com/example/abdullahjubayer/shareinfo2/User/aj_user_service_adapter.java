package com.example.abdullahjubayer.shareinfo2.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.abdullahjubayer.shareinfo2.R;

import java.util.ArrayList;

public class aj_user_service_adapter extends BaseAdapter {

    ArrayList<String> sr_title;
    ArrayList<String> sr_body;
    ArrayList<String> sr_image;
    Context context;
    LayoutInflater layoutInflater;

    public aj_user_service_adapter(Context context, ArrayList<String> sr_title, ArrayList<String> sr_body, ArrayList<String> sr_image) {
        this.sr_title = sr_title;
        this.sr_body = sr_body;
        this.context = context;
        this.sr_image=sr_image;
    }

    @Override
    public int getCount() {
        return sr_title.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.aj_user_service_adapter,parent,false);
        }
        TextView s_title=convertView.findViewById(R.id.service_title_id);
        TextView s_body=convertView.findViewById(R.id.service_body_id);
        ImageView imageView=convertView.findViewById(R.id.service_body_image);

        s_title.setText(sr_title.get(position));
        s_body.setText(sr_body.get(position));
        Glide.with(context).load(sr_image.get(position)).into(imageView);


        return convertView;
    }
}
