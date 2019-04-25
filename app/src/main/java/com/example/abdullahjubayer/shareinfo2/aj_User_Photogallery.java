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

 public class aj_User_Photogallery extends AppCompatActivity {


     FirebaseFirestore db;
     ArrayList<String> title=new ArrayList<>();
     ArrayList<String>image=new ArrayList<>();
    ListView listView;
     ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_user__photogallery);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Photo Gallery");

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_user_photogalarry);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        listView=findViewById(R.id.user_photogallery_listView);
        db = FirebaseFirestore.getInstance();

        Intent intent=getIntent();
        String com=intent.getStringExtra("Company");
        Log.i("Company received emp",com);
        Toast.makeText(aj_User_Photogallery.this,com,Toast.LENGTH_LONG).show();

        listview(com);
    }


     public  void listview(final String com){

        progressBar.setVisibility(View.VISIBLE);
         Log.i("Companyyyyyyy",com);
         DocumentReference user = db.collection("All_Photogallery").document(com);
         user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                 if (task.isSuccessful()){

                 DocumentSnapshot doc = task.getResult();

                     db.collection("All_Photogallery").document(com).collection("Image").addSnapshotListener(new EventListener<QuerySnapshot>() {
                         @Override
                         public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                             title.clear();
                             image.clear();

                             for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                 title.add(doc.get("Title").toString());
                                 image.add(doc.get("Image").toString());

                             }
                             aj_user_photogallery_adapter adapter = new aj_user_photogallery_adapter(aj_User_Photogallery.this,title,image);
                             adapter.notifyDataSetChanged();
                             listView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);

                         }
                     });
                 }
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 progressBar.setVisibility(View.INVISIBLE);
                     Toast.makeText(aj_User_Photogallery.this,"NO Picture Added",Toast.LENGTH_LONG).show();
             }
         });

     }

}
