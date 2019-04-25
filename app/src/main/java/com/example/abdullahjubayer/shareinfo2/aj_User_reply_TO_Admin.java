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

public class aj_User_reply_TO_Admin extends AppCompatActivity {

    FirebaseFirestore db;
    String company;
    ArrayList<String> job_title=new ArrayList<>();
    ArrayList<String>job_body=new ArrayList<>();
    ArrayList<String>job_image=new ArrayList<>();
    ListView listView;
    ProgressBar progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_user_reply__to__admin);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Services");

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_user_reply);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        db = FirebaseFirestore.getInstance();
        listView=findViewById(R.id.user_show_jod);

        Intent intent=getIntent();
        company=intent.getStringExtra("Company");
        Log.i("Company User Reply",company);

        getcompany_logo(company);
    }




    public  void load_job_circular(final String company_logo){


        DocumentReference user = db.collection("All_Services").document(company);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    db.collection("All_Services").document(company).collection("Service").addSnapshotListener(new EventListener<QuerySnapshot>() {


                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                            job_title.clear();
                            job_body.clear();
                            job_image.clear();

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {

                                job_title.add(doc.get("TiTle").toString());
                                job_body.add(doc.get("Message").toString());
                                job_image.add(doc.get("Image").toString());

                                Log.i("user_reply title",doc.get("TiTle").toString());
                                Log.i("user_reply aj_image",doc.get("Message").toString());

                            }
                            aj_job_adapter aj_job_adapter = new aj_job_adapter(getApplicationContext(), job_title,job_body,job_image,company,company_logo);
                            aj_job_adapter.notifyDataSetChanged();
                            listView.setAdapter(aj_job_adapter);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });


                }else {
                    Toast.makeText(aj_User_reply_TO_Admin.this,"No Service Added",Toast.LENGTH_LONG).show();

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(aj_User_reply_TO_Admin.this,"No Service Added",Toast.LENGTH_LONG).show();
            }
        });
                }



    public  void getcompany_logo(String com){
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference user = db.collection("All_Home_Page").document("HomePage Of_"+com);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    DocumentSnapshot doc = task.getResult();

                     String company_logo = doc.get("Company_logo").toString();
                    load_job_circular(company_logo);

                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_User_reply_TO_Admin.this,"Company Not Found",Toast.LENGTH_LONG).show();
            }
        });

    }
}

