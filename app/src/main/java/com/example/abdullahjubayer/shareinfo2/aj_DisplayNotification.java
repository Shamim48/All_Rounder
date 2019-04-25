package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class aj_DisplayNotification extends AppCompatActivity {
    TextView textView_title,textView_body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_display_notification);


        textView_title=findViewById(R.id.notification_title);
        textView_body=findViewById(R.id.notification_body);

        Intent intent=getIntent();
        String title=intent.getStringExtra("Notification_title");
        String body=intent.getStringExtra("Notification_body");
        textView_title.setText(title);
        textView_body.setText(body);
    }
}
