package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class aj_Set_job_Result extends AppCompatActivity {
    private static final int RQ_CODE =101 ;
    EditText r_title,r_body;
    Button release_btn;
    String downloadUrl;
    Uri image_uri;
    FirebaseFirestore db;
    ProgressBar progressBar ;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_set_job__result);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Service");

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_release_result);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        r_title=findViewById(R.id.result_title);
        r_body=findViewById(R.id.result_body);
        release_btn=findViewById(R.id.release_button);
        imageView=findViewById(R.id.admin_message_image);
        db = FirebaseFirestore.getInstance();


        release_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImage();
                }
                else {
                    Toast.makeText(aj_Set_job_Result.this, "Error in Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    public void selectImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),RQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==RQ_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            image_uri=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),image_uri);
                Glide.with(aj_Set_job_Result.this).load(bitmap).into(imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(aj_Set_job_Result.this,"Error in Select Image",Toast.LENGTH_LONG).show();
        }
    }


    private void uploadImage() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebase_email = firebaseUser.getEmail();
        final StorageReference storageReference=
                FirebaseStorage.getInstance().getReference("All_Company service_image/"+firebase_email+".jpg");

        if (image_uri!=null){
            storageReference.putFile(image_uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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

                        getCompanyName();

                        Log.d("downloadUrllllll", "onComplete: Url: "+ downloadUrl);
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(aj_Set_job_Result.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    public boolean valid(){

        boolean val=true;
        String title=r_title.getText().toString();
        String body=r_body.getText().toString();
        if (title.isEmpty() || title.length() < 3) {
            r_title.setError("at least 3 characters");
            val = false;
        } else {
            r_title.setError(null);
        }
        if (body.isEmpty()) {
            r_body.setError("at least 10 characters");
            val = false;
        } else {
            r_body.setError(null);
        }
        if (image_uri==null){
            Toast.makeText(aj_Set_job_Result.this, "Select a Photo",Toast.LENGTH_SHORT).show();
        }

        return val;
    }

    private void UploadUserData(String company) {

        final String title= r_title.getText().toString();
        final String body=r_body.getText().toString();

        if (!(downloadUrl.isEmpty())){
            Map< String, Object > newContact = new HashMap< >();
            newContact.clear();

            newContact.put("TiTle", title);
            newContact.put("Message", body);
            newContact.put("Image", downloadUrl);

            db.collection("All_Services").document(company).collection("Service").document().set(newContact)

                    .addOnSuccessListener(new OnSuccessListener< Void >() {

                        @Override

                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(aj_Set_job_Result.this, "Data Uploaded Success",

                                    Toast.LENGTH_SHORT).show();

                        }

                    })

                    .addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(aj_Set_job_Result.this, "User Registered Failed" + e.toString(),

                                    Toast.LENGTH_SHORT).show();

                            Log.d("TAG", e.toString());

                        }

                    });

        }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(aj_Set_job_Result.this, "Download URL Null.",Toast.LENGTH_SHORT).show();
        }

    }

    public void getCompanyName(){



        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebase_email = firebaseUser.getEmail();

        DocumentReference user = db.collection("Admin_Account").document(firebase_email);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();

                if (!doc.exists()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(aj_Set_job_Result.this,"Data Not Found",Toast.LENGTH_LONG).show();
                }else {
                    String company = doc.get("Company").toString();
                    UploadUserData(company);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_Set_job_Result.this,"Company Not Found",Toast.LENGTH_LONG).show();
            }
        });

    }

}
