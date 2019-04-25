package com.example.abdullahjubayer.shareinfo2.User;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.abdullahjubayer.shareinfo2.R;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

public  class aj_show_employee_to_user_adapter extends BaseAdapter {


    Context context;
    ArrayList<String> name;
    public static final int CALL=112;
    ArrayList<String> email;
    ArrayList<String> phone;
    ArrayList<String> picture;
    ArrayList<String> qualification;
    LayoutInflater layoutInflater;

    public aj_show_employee_to_user_adapter(Context context, ArrayList<String> name, ArrayList<String> email, ArrayList<String> phone, ArrayList<String> picture, ArrayList<String> qualification) {
        this.context = context;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.picture = picture;
        this.qualification = qualification;
    }

    @Override
    public int getCount() {
        return name.size();
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
         final int REQUEST = 112;

        if (convertView==null){
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.aj_employee_show_design,parent,false);
        }


        ImageView image=convertView.findViewById(R.id.employee_show_image);
        TextView t_name=convertView.findViewById(R.id.employee_show_name);
        TextView t_email=convertView.findViewById(R.id.employee_show_email);
        TextView t_phone=convertView.findViewById(R.id.employee_show_phone);
        TextView t_qualification=convertView.findViewById(R.id.employee_show_qualification);
        Button callBtn=convertView.findViewById(R.id.callBtnId);
        Button messageBtn=convertView.findViewById(R.id.smsBtnId);
        Button emailBtn=convertView.findViewById(R.id.emailBtnId);

        Glide.with(context).load(picture).into(image);
        t_name.setText("Name: "+name.get(position));
        t_email.setText("Email: "+email.get(position));
        t_phone.setText("Phone: "+phone.get(position));
        t_qualification.setText("Designation: "+qualification.get(position));


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    String uri = "tel:" + phone.get(position).trim() ;
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse(uri));
                    context.startActivity(i);






            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phone.get(position).trim(); // The number on which you want to send SMS
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));

            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress=email.get(position).trim();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailAddress});
                email.putExtra(Intent.EXTRA_SUBJECT, "");
                email.putExtra(Intent.EXTRA_TEXT, "");

//need this to prompts email client only
                email.setType("message/rfc822");

                context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        return convertView;
    }
   /* public static void callPhone(Activity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Context.PermissionUtil.applyPermission(activity, Manifest.permission.CALL_PHONE, 100);
            return;
        }
        activity.startActivity(intent);
    }*/
}
