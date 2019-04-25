package com.example.abdullahjubayer.shareinfo2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class aj_job_adapter extends BaseAdapter {
    Context context;
    ArrayList<String> job_title;
    ArrayList<String> job_body;
    ArrayList<String> job_image;
    LayoutInflater layoutInflater;
    String company,company_logo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public aj_job_adapter(Context context, ArrayList<String> job_title, ArrayList<String> job_body, ArrayList<String> job_image, String company, String company_logo) {
        this.context = context;
        this.job_title = job_title;
        this.job_body = job_body;
        this.job_image = job_image;
        this.company=company;
        this.company_logo=company_logo;
    }

    @Override
    public int getCount() {
        return job_title.size();
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
            convertView=layoutInflater.inflate(R.layout.aj_job_adapter,parent,false);
        }
        ImageView com_logo=convertView.findViewById(R.id.jod_adapter_logo);
        ImageView big_image=convertView.findViewById(R.id.jod_adapter_big_logo);
        TextView company_name=convertView.findViewById(R.id.jod_adapter_company_name);
        TextView service_title=convertView.findViewById(R.id.jod_adapter_com_title);
        TextView service_body=convertView.findViewById(R.id.jod_adapter_com_body);
        Button reply=convertView.findViewById(R.id.jod_adapter_reply_btn);
        final EditText editText=convertView.findViewById(R.id.jod_adapter_reply_et);

        company_name.setText(company);
        service_title.setText(job_title.get(position));
        service_body.setText(job_body.get(position));
        Glide.with(context).load(job_image.get(position)).into(big_image);
        Glide.with(context).load(company_logo).into(com_logo);

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String reply=editText.getText().toString();

                if (!reply.isEmpty()){

                    Map< String, Object > newContact = new HashMap< >();
                    newContact.clear();

                    newContact.put("Reply", reply);


                   DocumentReference documentReference= db.collection("ALL_USER_SEND_TO_ADMIN").document(company).collection("Reply").document();
                        documentReference.set(newContact)

                            .addOnSuccessListener(new OnSuccessListener< Void >() {

                                @Override

                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(context, "Reply Success",

                                            Toast.LENGTH_SHORT).show();

                                }

                            })

                            .addOnFailureListener(new OnFailureListener() {

                                @Override

                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(context, "Reply Failed" + e.toString(),

                                            Toast.LENGTH_SHORT).show();

                                    Log.d("TAG", e.toString());

                                }

                            });


                }else {
                    Toast.makeText(context,"Write Something in Edit Text",Toast.LENGTH_LONG).show();
                }

            }
        });

        return convertView;
    }


}
