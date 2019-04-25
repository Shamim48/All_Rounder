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

public class aj_Set_Admin_Message extends AppCompatActivity {
    private static final int RQ_CODE = 101;
    EditText noti_title,noti_body;
    ImageView noti_image;
    Button noti_send_btn;
    FirebaseFirestore db;
    Uri noti_image_uri;
    String image_url;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj_activity_set__admin__message);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Notification");


        progressBar = (ProgressBar)findViewById(R.id.spin_kit_admin_message);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        noti_title=findViewById(R.id.notification_title);
        noti_body=findViewById(R.id.notification_message);
        noti_send_btn=findViewById(R.id.notification_send_button);
        db = FirebaseFirestore.getInstance();
        noti_image=findViewById(R.id.notification_image);



        noti_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        noti_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_send_user();
            }
        });
    }

    public  void notification_send_user(){
        String notification_title=noti_title.getText().toString();
        String notification_message=noti_body.getText().toString();

        if (!validate_notification(notification_title,notification_message)){
            Toast.makeText(aj_Set_Admin_Message.this,"Message Not valid",Toast.LENGTH_LONG).show();
        }
        else {
                progressBar.setVisibility(View.VISIBLE);
                uploadImage();
        }
    }




    private void uploadImage() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebase_email = firebaseUser.getEmail();
        final StorageReference storageReference=
                FirebaseStorage.getInstance().getReference("All_Company Message_img/"+firebase_email+".jpg");

        if (noti_image_uri!=null){
            storageReference.putFile(noti_image_uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        image_url = task.getResult().toString();

                        getCompanyName();

                        Log.d("downloadUrllllll", "onComplete: Url: "+ image_url);
                        Toast.makeText(aj_Set_Admin_Message.this, "Picture URL :"+image_url,Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(aj_Set_Admin_Message.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private boolean validate_notification(String notification_title, String notification_message) {

        boolean val=true;

        if (notification_title.isEmpty()){
            val=false;
            noti_title.setError("Title is Null");
        }else {
            noti_title.setError(null);
        }
        if (notification_message.isEmpty()){
            val=false;
            noti_body.setError("Body is null");
        }else {
            noti_body.setError(null);
        }
        if (noti_image_uri==null){
            val=false;
        }

        return  val;
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
                    Toast.makeText(aj_Set_Admin_Message.this,"Data Not Found",Toast.LENGTH_LONG).show();
                }else {
                    String company = doc.get("Company").toString();
                    firestoredata_save(company);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(aj_Set_Admin_Message.this,"Company Not Found",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void firestoredata_save(String collection){

        String message_title=noti_title.getText().toString();
        String message_body=noti_body.getText().toString();

        Map< String, Object > newContact = new HashMap< >();

        newContact.put("Message_Title", message_title);
        newContact.put("Message_Body", message_body);

        newContact.put("Message_image", image_url);

        db.collection("All_Company Message").document(collection).collection("Message").document().set(newContact)

                .addOnSuccessListener(new OnSuccessListener< Void >() {

                    @Override

                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(aj_Set_Admin_Message.this,"Data Uploaded Success",Toast.LENGTH_LONG).show();

                    }

                })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(aj_Set_Admin_Message.this, "Data Registered Failed" + e.toString(),

                                Toast.LENGTH_SHORT).show();

                        Log.d("TAG", e.toString());

                    }

                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RQ_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            noti_image_uri=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),noti_image_uri);
                Glide.with(aj_Set_Admin_Message.this).load(bitmap).into(noti_image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(aj_Set_Admin_Message.this,"Error in Select Image",Toast.LENGTH_LONG).show();
        }
    }

    public void selectImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),RQ_CODE);

    }

}
