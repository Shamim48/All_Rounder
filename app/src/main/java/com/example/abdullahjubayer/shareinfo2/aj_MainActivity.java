package com.example.abdullahjubayer.shareinfo2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.abdullahjubayer.shareinfo2.Admin.Admin_Home_Avt;
import com.google.firebase.auth.FirebaseAuth;


public class aj_MainActivity extends AppCompatActivity {
    Button user,admin;
    TextView new_account,sh_title;
    Typeface typeface;
    FirebaseAuth auth;
    SharedPreferences preferences;

    @Override
    protected void onStart() {
        super.onStart();

        boolean isUser= preferences.getBoolean("isLogin",false);
        Log.i("User_Active",String.valueOf(isUser));
         auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=  null){
            Intent intent=new Intent(aj_MainActivity.this, Admin_Home_Avt.class);
            startActivity(intent);
            finish();
        }
       if (isUser){
            Intent intent=new Intent(aj_MainActivity.this,aj_UserHomePage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_main);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("");


        user=findViewById(R.id.user_id);
        admin=findViewById(R.id.admin_id);
        new_account=findViewById(R.id.new_account);
        sh_title=findViewById(R.id.share_info_title);
        preferences=this.getSharedPreferences("com.example.abdullahjubayer.shareinfo2",Context.MODE_PRIVATE);

        typeface=Typeface.createFromAsset(getAssets(),"font/AlexBrush_Regular.ttf");
        sh_title.setTypeface(typeface);

        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_account();
            }
        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(aj_MainActivity.this,aj_UserLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(aj_MainActivity.this,aj_AdminLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });
    }

    public void new_account(){
        Intent intent=new Intent(aj_MainActivity.this,aj_SignUpType.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


}
