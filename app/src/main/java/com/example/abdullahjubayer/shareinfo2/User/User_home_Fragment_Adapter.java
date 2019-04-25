package com.example.abdullahjubayer.shareinfo2.User;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abdullahjubayer.shareinfo2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class User_home_Fragment_Adapter extends BaseAdapter {


    Context context;
    ArrayList<String> title;
    ArrayList<String> image;
    ArrayList<String> description;
   // ArrayList<String> company;
    private LayoutInflater layoutInflater;

    public User_home_Fragment_Adapter(Context context, ArrayList<String> title, ArrayList<String> image, ArrayList<String> description) {
        this.context = context;
        this.title = title;
        this.image = image;
        this.description = description;
      //  this.company=company;
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView==null){
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.userhome_fragment_row,parent,false);
        }

        TextView titleTv=convertView.findViewById(R.id.user_show_title_row);
        TextView descriptionTv=convertView.findViewById(R.id.user_show_body_row);
        descriptionTv.setText(description.get(position));
        ImageView imageView=convertView.findViewById(R.id.user_show_image_row);
        titleTv.setText(title.get(position));
        Glide.with(context).load(image.get(position)).into(imageView);

        return convertView;
    }
}
