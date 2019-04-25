package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class aj_SignUpType extends AppCompatActivity {
    Button user_s_up,admin_s_up;
    TextView textView;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_sign_up_type);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("");

        user_s_up=findViewById(R.id.user_SignUP);
        admin_s_up=findViewById(R.id.admin_signUP);
        textView=findViewById(R.id.sign_type_share_title);

        typeface=Typeface.createFromAsset(getAssets(),"font/AlexBrush_Regular.ttf");
        textView.setTypeface(typeface);


        user_s_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(aj_SignUpType.this,aj_UserSignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        admin_s_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(aj_SignUpType.this,aj_admin_signUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
}
