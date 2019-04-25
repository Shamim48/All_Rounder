package com.example.abdullahjubayer.shareinfo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class aj_admin_message_adapter extends BaseAdapter {
    Context context;
    ArrayList<String> topic;
    LayoutInflater layoutInflater;

    public aj_admin_message_adapter(Context context, ArrayList<String> topic) {
        this.context = context;
        this.topic = topic;
    }

    @Override
    public int getCount() {
        return topic.size();
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
            convertView=layoutInflater.inflate(R.layout.aj_admin_message_adapter,parent,false);
        }

        TextView t_topic=convertView.findViewById(R.id.admin_see_message_topic);

        t_topic.setText(topic.get(position));


        return convertView;
    }
}
