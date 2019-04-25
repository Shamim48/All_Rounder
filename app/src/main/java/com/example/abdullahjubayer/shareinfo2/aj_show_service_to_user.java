package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.abdullahjubayer.shareinfo2.User.aj_user_service_adapter;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class aj_show_service_to_user extends AppCompatActivity {


    FirebaseFirestore db;
    ArrayList<String> ser_title=new ArrayList<>();
    ArrayList<String>ser_body=new ArrayList<>();
    ArrayList<String>ser_image=new ArrayList<>();
    ListView listView;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_show_service_to_user);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Notice");

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_show_service);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        listView=findViewById(R.id.user_service_listview);
        db = FirebaseFirestore.getInstance();



        Intent intent=getIntent();
        final String com=intent.getStringExtra("Company");
        Log.i("received frm usr servie",com);

        loaddata(com);



    }

    private void loaddata(final String com) {

        progressBar.setVisibility(View.VISIBLE);
        DocumentReference user = db.collection("All_Company Message").document(com);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                DocumentSnapshot doc = task.getResult();

                    db.collection("All_Company Message").document(com).collection("Message").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ser_title.clear();
                            ser_body.clear();
                            ser_image.clear();

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {

                                ser_title.add(doc.get("Message_Title").toString());
                                ser_body.add(doc.get("Message_Body").toString());
                                ser_image.add(doc.get("Message_image").toString());

                            }
                            aj_user_service_adapter adapter = new aj_user_service_adapter(aj_show_service_to_user.this, ser_title,ser_body,ser_image);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(aj_show_service_to_user.this,"No Service Provided",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_show_service_to_user.this,"Task Failed",Toast.LENGTH_LONG).show();
            }
        });

    }
}
