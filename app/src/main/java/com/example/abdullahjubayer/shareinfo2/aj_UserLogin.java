package com.example.abdullahjubayer.shareinfo2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class aj_UserLogin extends AppCompatActivity {
    EditText email, password;
    Button login_btn;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView textView;
    Typeface typeface;
    ProgressBar progressBar ;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_user_login);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("User Login");


         progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);


        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);
        login_btn = findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        textView=findViewById(R.id.share_user_title);
        preferences=this.getSharedPreferences("com.example.abdullahjubayer.shareinfo2",Context.MODE_PRIVATE);

        typeface=Typeface.createFromAsset(getAssets(),"font/AlexBrush_Regular.ttf");
        textView.setTypeface(typeface);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });


    }


    private void userLogin() {
        if (!userLoginValid()){
            Toast.makeText(aj_UserLogin.this,"Please Fill All Data Correctly",Toast.LENGTH_SHORT).show();
        }
        else {

            String _email = email.getText().toString();
            String _pass = password.getText().toString();

            userValid(_email,_pass);
            progressBar.setVisibility(View.VISIBLE);

        }
    }

    private void userValid(final String email, final String pass) {

            DocumentReference user = db.collection("ALL_USER_ACCOUNT").document(email);
            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    progressBar.setVisibility(View.INVISIBLE);

                    DocumentSnapshot doc = task.getResult();

                    if (!doc.exists()){
                        Toast.makeText(aj_UserLogin.this,"Wrong Email and Password",Toast.LENGTH_LONG).show();
                    }else {
                        String name = doc.get("Name").toString();
                        String password = doc.get("Password").toString();
                        String image = doc.get("Picture").toString();

                        if (pass.equals(password)){
                            preferences.edit().clear();
                            preferences.edit().putBoolean("isLogin",true).apply();
                            Intent intent=new Intent(aj_UserLogin.this,aj_UserHomePage.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(aj_UserLogin.this,"Wrong Password",Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });

    }


    private boolean userLoginValid() {
        String ad_email=email.getText().toString().trim();
        String ad_pass=password.getText().toString().trim();


        boolean val=true;

        if (ad_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(ad_email).matches()) {
            email.setError("enter a valid admin email address");
            val = false;
        } else {
            email.setError(null);
        }

        if (ad_pass.isEmpty() || ad_pass.length() <6) {
            password.setError("Password Wrong");
            val = false;
        } else {
            password.setError(null);
        }

        return val;

    }

}
