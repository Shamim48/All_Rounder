package com.example.abdullahjubayer.shareinfo2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class aj_Set_photogalarry extends AppCompatActivity {
    private static final int RQ_CODE =101 ;
    EditText galary_title;
    ImageView galary_img;
    Uri photogalarry_image;
    Button button;
    String photo_downloadUrl;
    FirebaseFirestore db;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_set_photogalarry);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Picture");



        progressBar = (ProgressBar)findViewById(R.id.spin_kit_admin_home_photogallery);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);



        galary_title=findViewById(R.id.photogalarry_title);
        galary_img=findViewById(R.id.photogalarry_image);
        button=findViewById(R.id.photogalarry_upload_btn);
        db = FirebaseFirestore.getInstance();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()){
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImage();
                }
                else {
                    Toast.makeText(aj_Set_photogalarry.this, "Error in Data",Toast.LENGTH_SHORT).show();
                }

            }
        });


        galary_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }




    private void uploadImage() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebase_email = firebaseUser.getEmail();
        final StorageReference storageReference=
                FirebaseStorage.getInstance().getReference("All_Photogalarry_image/"+"Homepage_img"+firebase_email+".jpg");

        if (photogalarry_image!=null){
            storageReference.putFile(photogalarry_image).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        photo_downloadUrl = task.getResult().toString();

                        getCompanyName();

                        Log.d("downloadUrllllll", "onComplete: Url: "+ photo_downloadUrl);
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(aj_Set_photogalarry.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(aj_Set_photogalarry.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
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
                    Toast.makeText(aj_Set_photogalarry.this,"Data Not Found",Toast.LENGTH_LONG).show();
                }else {
                    String company = doc.get("Company").toString();
                    firestoredata_save(company);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_Set_photogalarry.this,"Company Not Found",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void firestoredata_save(String collection){

        String title=galary_title.getText().toString();
        Map< String, Object > newContact = new HashMap< >();

        newContact.put("Title", title);
        newContact.put("Image", photo_downloadUrl);
        newContact.put("Company", collection);

        db.collection("All_Photogallery").document(collection).collection("Image").document().set(newContact)

                .addOnSuccessListener(new OnSuccessListener< Void >() {

                    @Override

                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(aj_Set_photogalarry.this,"Data Uploaded Success",Toast.LENGTH_LONG).show();

                    }

                })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(aj_Set_photogalarry.this, "Data Uploaded Failed" + e.toString(),

                                Toast.LENGTH_SHORT).show();

                        Log.d("TAG", e.toString());

                    }

                });
    }



    public boolean validation(){



        boolean val=true;
        String title=galary_title.getText().toString();

        if (title.isEmpty() || title.length() < 3) {
            galary_title.setError("at least 3 characters");
            val = false;
        } else {
            galary_title.setError(null);
        }
        if (photogalarry_image==null) {
            val = false;
        }

        return val;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RQ_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            photogalarry_image=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),photogalarry_image);
                Glide.with(aj_Set_photogalarry.this).load(bitmap).into(galary_img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(aj_Set_photogalarry.this,"Error in Select Image",Toast.LENGTH_LONG).show();
        }
    }

    public void selectImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),RQ_CODE);

    }
}
