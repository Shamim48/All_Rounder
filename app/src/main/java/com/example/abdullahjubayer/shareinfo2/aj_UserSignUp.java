package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class aj_UserSignUp extends AppCompatActivity  {
    private static final int RQ_CODE =101 ;
    EditText name_et,email_et,phone_et,passs_et,_reEnterPasswordText,work_at;
    Button signUP;
    TextView textView;
    CircleImageView imageView;
    Uri profileImage;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String downloadUrl;
    ProgressBar progressBar ;
    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_user_sign_up);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("User Signup");

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_user_signup);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);


        name_et=findViewById(R.id.input_name);
        email_et=findViewById(R.id.input_email);
        phone_et=findViewById(R.id.input_mobile);
        passs_et=findViewById(R.id.input_password);
        _reEnterPasswordText=findViewById(R.id.input_reEnterPassword);
        signUP=findViewById(R.id.btn_signup);
        textView=findViewById(R.id.user_login_back);
        imageView=findViewById(R.id.profile_image);
        work_at=findViewById(R.id.work_at);
         db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        preferences=this.getPreferences(MODE_PRIVATE);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten=new Intent(aj_UserSignUp.this,aj_UserLogin.class);
                inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inten);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()){
                    Toast.makeText(aj_UserSignUp.this,"Please Fill All Data Correctly",Toast.LENGTH_LONG).show();
                }
                else {

                    createUserr();

                }
            }
        });
    }

    private void createUserr() {


         String email = email_et.getText().toString();
        uploadImage(email);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void uploadImage(String firebase_email) {

        final StorageReference storageReference=
                FirebaseStorage.getInstance().getReference("ALL_USER_PHOTO/"+"User_img_"+firebase_email+".jpg");

        if (profileImage!=null){
            storageReference.putFile(profileImage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        downloadUrl = task.getResult().toString();
                        final String email = email_et.getText().toString();
                        userValid(email);
                    }
                    else {
                        Toast.makeText(aj_UserSignUp.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }else{
            Toast.makeText(aj_UserSignUp.this, "Profile Picture Not Selected",Toast.LENGTH_SHORT).show();
        }

    }



    private void UploadUserData() {

        String name= name_et.getText().toString();
        String work=work_at.getText().toString();
        String email = email_et.getText().toString();
        String mobile = phone_et.getText().toString();
        String password = passs_et.getText().toString();


        if (!(downloadUrl.isEmpty())){
            Map< String, Object > newContact = new HashMap< >();

            newContact.put("Name", name);
            newContact.put("Work_At", work);
            newContact.put("Email", email);
            newContact.put("Mobile", mobile);
            newContact.put("Password", password);
            newContact.put("Picture", downloadUrl);

            db.collection("ALL_USER_ACCOUNT").document(email).set(newContact)

                    .addOnSuccessListener(new OnSuccessListener < Void > () {

                        @Override

                        public void onSuccess(Void aVoid) {

                            Toast.makeText(aj_UserSignUp.this, "User Registered Success",

                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);


                        }

                    })

                    .addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(aj_UserSignUp.this, "User Registered Failed" + e.toString(),

                                    Toast.LENGTH_SHORT).show();

                            Log.d("TAG", e.toString());
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                    });

        }else {
            Toast.makeText(aj_UserSignUp.this, "Download URL Null.",Toast.LENGTH_SHORT).show();
        }

    }


    private void userValid(final String email) {


        DocumentReference user = db.collection("ALL_USER_ACCOUNT").document(email);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();

                if (!doc.exists()){
                    UploadUserData();
                }else {
                        signUP.setEnabled(false);
                        Toast.makeText(aj_UserSignUp.this,"Account Already Exists",Toast.LENGTH_LONG).show();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_UserSignUp.this,"Company Not Found",Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RQ_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            profileImage=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),profileImage);
                Glide.with(aj_UserSignUp.this).load(bitmap).into(imageView);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(aj_UserSignUp.this,"Error to Select Image",Toast.LENGTH_LONG).show();
        }
    }


    private void getImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),RQ_CODE);

    }

    public boolean validate() {
        boolean valid = true;

        String name= name_et.getText().toString();
        String work=work_at.getText().toString();
        String email = email_et.getText().toString();
        String mobile = phone_et.getText().toString();
        String password = passs_et.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            name_et.setError("at least 3 characters");
            valid = false;
        } else {
            name_et.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_et.setError("enter a valid email address");
            valid = false;
        } else {
            email_et.setError(null);
        }

        if (mobile.isEmpty() && Patterns.PHONE.matcher(mobile).matches()) {
            phone_et.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            phone_et.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            passs_et.setError("at last 6 digite");
            valid = false;
        } else {
            passs_et.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;

        } else {
            _reEnterPasswordText.setError(null);
        }
        if (work.isEmpty()){
            work_at.setError("Work As Required");
            valid=false;
        } else {
            work_at.setError(null);
        }


        return valid;
    }


}
