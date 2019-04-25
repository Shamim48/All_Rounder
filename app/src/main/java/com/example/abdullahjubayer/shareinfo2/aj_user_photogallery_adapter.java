package com.example.abdullahjubayer.shareinfo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class aj_user_photogallery_adapter extends BaseAdapter {
    Context context;
    ArrayList<String> title;
    ArrayList<String> image;
    LayoutInflater layoutInflater;

    public aj_user_photogallery_adapter(Context context, ArrayList<String> title, ArrayList<String> image) {
        this.context = context;
        this.title = title;
        this.image = image;
    }

    @Override
    public int getCount() {
        return title.size();
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
            convertView=layoutInflater.inflate(R.layout.aj_photogallery_design,parent,false);
        }
        TextView _title=convertView.findViewById(R.id.photogallery_title_design);
        ImageView _image=convertView.findViewById(R.id.photogallery_image_design);

        _title.setText(title.get(position));
        Glide.with(context).load(image.get(position)).into(_image);


        return convertView;
    }
}
