package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.FadingCircle;

public class aj_ShowHomePage extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    TextView titel_t,body_b;
    ImageButton home_page,employee_page,sevice_page,send_message_page,show_photo;
    String title,image,desc,company;
    ProgressBar progressBar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_show_home_page);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Home");


        progressBar = (ProgressBar)findViewById(R.id.spin_kit_show_home_page_touserPP);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.VISIBLE);


        Intent intent=getIntent();
          title=intent.getStringExtra("title");
          image=intent.getStringExtra("image");
          desc=intent.getStringExtra("Description");
          company=intent.getStringExtra("Company");

        Log.i("Company received",company);


        imageView=findViewById(R.id.user_show_image);
        titel_t=findViewById(R.id.user_show_title);
        body_b=findViewById(R.id.user_show_body);
        home_page=findViewById(R.id.user_show_homepage);
        employee_page=findViewById(R.id.user_show_employee);
        sevice_page=findViewById(R.id.user_show_services);
        send_message_page=findViewById(R.id.user_show_notification);
        show_photo=findViewById(R.id.user_show_photogalarry);


        home_page.setOnClickListener(this);
        employee_page.setOnClickListener(this);
        sevice_page.setOnClickListener(this);
        send_message_page.setOnClickListener(this);
        show_photo.setOnClickListener(this);

        load_Data();





    }

    private void load_Data() {
        titel_t.setText(title);
        body_b.setText(desc);
        Glide.with(aj_ShowHomePage.this).load(image).into(imageView);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.user_show_employee){

            Intent intent=new Intent(aj_ShowHomePage.this,aj_Show_employee_user.class);
            intent.putExtra("Company",company);
            startActivity(intent);
            finishActivity(0);

        }
        if (v.getId()==R.id.user_show_services){
            Intent intent=new Intent(aj_ShowHomePage.this,aj_show_service_to_user.class);
            intent.putExtra("Company",company);
            startActivity(intent);
            finishActivity(0);
        }
        if (v.getId()==R.id.user_show_notification){
            Intent intent=new Intent(aj_ShowHomePage.this,aj_User_reply_TO_Admin.class);
            intent.putExtra("Company",company);
            startActivity(intent);
            finishActivity(0);
        }
        if (v.getId()==R.id.user_show_photogalarry){
            Intent intent=new Intent(aj_ShowHomePage.this,aj_User_Photogallery.class);
            intent.putExtra("Company",company);
            startActivity(intent);
            finishActivity(0);
        }
    }
}
