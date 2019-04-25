package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.Timer;

public class aj_Splash_activity extends AppCompatActivity {

    GifView gifView1;
    TextView textView;
    Typeface typeface;
    ProgressBar progressBar_counter;
    int pog_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_splash_activity);

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.hide();

        gifView1 = (GifView) findViewById(R.id.gif2);
        gifView1.setVisibility(View.VISIBLE);
        progressBar_counter=findViewById(R.id.progress_count);

        textView=findViewById(R.id.welcome_txt);

        typeface=Typeface.createFromAsset(getAssets(),"font/FFF_Tusj.ttf");
        textView.setTypeface(typeface);
        gifView1.play();


        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                progress_mathed();
                new_intent();
            }
        });
        thread.start();

    }

    private void progress_mathed() {

        for (pog_value=1;pog_value<=60;pog_value=pog_value+1) {
            try {
                        Thread.sleep(30);
                        progressBar_counter.setProgress(pog_value);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    private void new_intent() {
        Intent intent=new Intent(aj_Splash_activity.this,aj_MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();

    }

    }

