package com.example.abdullahjubayer.shareinfo2;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class aj_Set_Job extends AppCompatActivity {


    FirebaseFirestore db;
    ArrayList<String> ad_title=new ArrayList<>();
    ListView listView;
    ProgressBar progressBar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_set__job);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Notification");

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_admin_set_job);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        listView=findViewById(R.id.admin_see_message_listview);
        db = FirebaseFirestore.getInstance();

        getCompanyName();
    }




    public void getCompanyName(){


        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebase_email = firebaseUser.getEmail();

        DocumentReference user = db.collection("Admin_Account").document(firebase_email);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();

                if (!doc.exists()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(aj_Set_Job.this,"Data Not Found",Toast.LENGTH_LONG).show();
                }else {
                    String company = doc.get("Company").toString();
                            getMessage(company);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_Set_Job.this,"Company Not Found",Toast.LENGTH_LONG).show();
            }
        });

    }


    public void getMessage(final String company){


        DocumentReference user = db.collection("ALL_USER_SEND_TO_ADMIN").document(company).collection("Reply").document();
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                db.collection("ALL_USER_SEND_TO_ADMIN").document(company).collection("Reply").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        ad_title.clear();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {

                            ad_title.add(doc.get("Reply").toString());

                        }
                        aj_admin_message_adapter adapter = new aj_admin_message_adapter(aj_Set_Job.this, ad_title);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_Set_Job.this,"Task Failed",Toast.LENGTH_LONG).show();
            }
        });



    }

}
