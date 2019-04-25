package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abdullahjubayer.shareinfo2.Admin.Admin_Home_Avt;
import com.example.abdullahjubayer.shareinfo2.User.Home_User;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class aj_AdminHomePage extends AppCompatActivity implements View.OnClickListener {
    ImageButton home_page,empioyee,message_btn,job,result_btn,photogalarry;
    FirebaseFirestore db;
    LinearLayout linearLayout;
    TextView tit,bod;
    ImageView imageView;
    FirebaseAuth mAuth;
    ProgressBar progressBar ;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_admin_home_page);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Admin Home");


        progressBar = (ProgressBar)findViewById(R.id.spin_kit_admin_home);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);



        home_page=findViewById(R.id.set_admin_home_page);
        empioyee=findViewById(R.id.set_employee);
        message_btn=findViewById(R.id.set_notification);
        job=findViewById(R.id.set_job_circular);
        result_btn=findViewById(R.id.set_job_result);
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        linearLayout=findViewById(R.id.layout_id);
        tit=findViewById(R.id.text_title);
        bod=findViewById(R.id.text_body);
        imageView=findViewById(R.id.image_id_com);
        photogalarry=findViewById(R.id.set_job_photogalary);






        home_page.setOnClickListener(this);
        empioyee.setOnClickListener(this);
        message_btn.setOnClickListener(this);
        job.setOnClickListener(this);
        result_btn.setOnClickListener(this);
        photogalarry.setOnClickListener(this);

        getCompanyName();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adminmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        switch (id){

            case R.id.adminLogOut:
                auth.signOut();
                Intent i=new Intent(getApplicationContext(), aj_AdminLogin.class);
                startActivity(i);
                finish();


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.set_admin_home_page){
            Intent intent=new Intent(aj_AdminHomePage.this,aj_Set_Admin_HomePage.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.set_employee){
            Intent intent=new Intent(aj_AdminHomePage.this,aj_Set_Employee.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.set_notification){
            Intent intent=new Intent(aj_AdminHomePage.this,aj_Set_Admin_Message.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.set_job_circular){
            Intent intent=new Intent(aj_AdminHomePage.this,aj_Set_Job.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.set_job_result){
            Intent intent=new Intent(aj_AdminHomePage.this,aj_Set_job_Result.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.set_job_photogalary){
            Intent intent=new Intent(aj_AdminHomePage.this,aj_Set_photogalarry.class);
            startActivity(intent);
        }
    }

    public void firestoreCalled(String name) {


        DocumentReference user = db.collection("All_Home_Page").document("HomePage Of_"+name);
        user.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {

            @Override

            public void onComplete(@NonNull Task < DocumentSnapshot > task) {


                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();

                    if (!doc.exists()){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(aj_AdminHomePage.this,"Home Page Not Found",Toast.LENGTH_LONG).show();
                    }else {

                        String title=doc.get("Title").toString();
                        String description=doc.get("Description").toString();
                        String image=doc.get("Homepage_Img").toString();

                        Log.i("Nammmmmmmmmmmm",title+"   "+description+"  "+image);
                        progressBar.setVisibility(View.INVISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                        tit.setText(title);
                        bod.setText(description);
                        Glide.with(aj_AdminHomePage.this).load(image).into(imageView);

                    }

                }else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(aj_AdminHomePage.this,"Home Page Not Found",Toast.LENGTH_LONG).show();
                }

            }

        })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(aj_AdminHomePage.this,"Home Page Not Found",Toast.LENGTH_LONG).show();

                    }

                });




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
                    Toast.makeText(aj_AdminHomePage.this,"NO Home Page Found",Toast.LENGTH_LONG).show();
                }else {
                    String company = doc.get("Company").toString();
                    firestoreCalled(company);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_AdminHomePage.this,"Company Not Found",Toast.LENGTH_LONG).show();
            }
        });

    }

}
