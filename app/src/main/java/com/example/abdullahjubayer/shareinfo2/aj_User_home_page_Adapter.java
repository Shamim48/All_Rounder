package com.example.abdullahjubayer.shareinfo2;

import android.content.Context;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;

public class aj_User_home_page_Adapter extends BaseAdapter {


    Context context;
    ArrayList<String> title;
    ArrayList<String> image;
    ArrayList<String> description;
    ArrayList<String> company;
    private LayoutInflater layoutInflater;

    public aj_User_home_page_Adapter(Context context, ArrayList<String> title, ArrayList<String> image, ArrayList<String> description, ArrayList<String> company) {
        this.context = context;
        this.title = title;
        this.image = image;
        this.description = description;
        this.company=company;
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
            convertView=layoutInflater.inflate(R.layout.aj_list_view_layout,parent,false);
        }

        TextView textView=convertView.findViewById(R.id.list_view_title);
        ImageView imageView=convertView.findViewById(R.id.list_view_image);
        textView.setText(title.get(position));
        Glide.with(context).load(image.get(position)).into(imageView);
        Button button=convertView.findViewById(R.id.list_view_like_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String topic=title.get(position).toString();
                if (topic.isEmpty()){
                    Toast.makeText(context,"Wait Please",Toast.LENGTH_SHORT).show();
                }else {


                    String ttt=topic.replaceAll(" ",".");
                   // Toast.makeText(context,ttt,Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().subscribeToTopic(ttt)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(context,"Subscribe Failed",Toast.LENGTH_SHORT).show();
                                    }

                                    Toast.makeText(context,"Follower",Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, msg);
                                }

                            });
                }

            }
        });

        return convertView;
    }
}
