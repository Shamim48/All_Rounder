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

import com.example.abdullahjubayer.shareinfo2.User.aj_show_employee_to_user_adapter;
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

public class aj_Show_employee_user extends AppCompatActivity {

    FirebaseFirestore db;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String>email=new ArrayList<>();
    ArrayList<String>phone=new ArrayList<>();
    ArrayList<String>picture=new ArrayList<>();
    ArrayList<String>qualification=new ArrayList<>();
    ListView listView;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_show_employee_user);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Employee");

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_show_employee);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);



        db = FirebaseFirestore.getInstance();

        listView=findViewById(R.id.show_employee_to_user_listview);


        Intent intent=getIntent();
        String com=intent.getStringExtra("Company");
        Log.i("Company received emp",com);




        listview(com);

    }

    public  void listview(final String com){


        progressBar.setVisibility(View.VISIBLE);
        Log.i("Companyyyyyyy",com);
        DocumentReference user = db.collection("All_Employee_Data").document(com);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();

                        db.collection("All_Employee_Data").document(com).collection("Information").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                name.clear();
                                email.clear();
                                phone.clear();
                                picture.clear();
                                qualification.clear();

                                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                    name.add(doc.get("Name").toString());
                                    email.add(doc.get("Email").toString());
                                    phone.add(doc.get("Phone").toString());
                                    picture.add(doc.get("Picture").toString());
                                    qualification.add(doc.get("Qualification").toString());


                                }
                                aj_show_employee_to_user_adapter adapter = new aj_show_employee_to_user_adapter(aj_Show_employee_user.this, name, email, phone, picture, qualification);
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });

                    }
                    else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(aj_Show_employee_user.this,"No Employee Added",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_Show_employee_user.this,"No Employee Added",Toast.LENGTH_LONG).show();
            }
        });




    }
}
