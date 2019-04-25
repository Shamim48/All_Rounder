package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdullahjubayer.shareinfo2.Admin.Admin_Home_Avt;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class aj_AdminLogin extends AppCompatActivity {
    EditText admin_email, admin_pass;
    Button admin_login_button;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView textView;
    Typeface typeface;
    ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(aj_AdminLogin.this, aj_AdminHomePage.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_admin_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Admin Login");

        progressBar = (ProgressBar) findViewById(R.id.spin_kit_admin);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);


        admin_email = findViewById(R.id.admin_email_id);
        admin_pass = findViewById(R.id.admin_password_id);
        admin_login_button = findViewById(R.id.admin_login_btn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        textView = findViewById(R.id.share_admin_title);

        typeface = Typeface.createFromAsset(getAssets(), "font/AlexBrush_Regular.ttf");
        textView.setTypeface(typeface);


        admin_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminLogin();
            }
        });

    }

    private void adminLogin() {
        if (!admin_login_valid()) {
            Toast.makeText(aj_AdminLogin.this, "Wrong Email & Password ", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            String _email = admin_email.getText().toString();
            String _pass = admin_pass.getText().toString();

            mAuth.signInWithEmailAndPassword(_email, _pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(aj_AdminLogin.this, Admin_Home_Avt.class);
                                startActivity(intent);
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("adminle log state", "signInWithEmail:failure", task.getException());
                                Toast.makeText(aj_AdminLogin.this, "Authentication failed : Enter valid Email and pass",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(aj_AdminLogin.this, "Authentication failed : Enter valid Email and pass",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }


    }


    private boolean admin_login_valid() {
        String ad_email = admin_email.getText().toString().trim();
        String ad_pass = admin_pass.getText().toString().trim();


        boolean val = true;


        if (ad_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(ad_email).matches()) {
            admin_email.setError("enter a valid aj_admin email address");
            val = false;
        } else {
            admin_email.setError(null);
        }

        if (ad_pass.isEmpty()) {
            admin_pass.setError("Password Wrong");
            val = false;
        } else {
            admin_pass.setError(null);
        }

        return val;

    }
}