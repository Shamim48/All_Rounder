package com.example.abdullahjubayer.shareinfo2.Admin;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abdullahjubayer.shareinfo2.R;
import com.example.abdullahjubayer.shareinfo2.aj_Set_job_Result;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SerVices_Admin_FMT extends Fragment {
    private static final int RQ_CODE =101 ;
    EditText r_title,r_body;
    Button release_btn;
    String downloadUrl;
    Uri image_uri;
    FirebaseFirestore db;
    ProgressBar progressBar ;
    ImageView imageView;


    public SerVices_Admin_FMT() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ser_vices__admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit_release_result);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        r_title=view.findViewById(R.id.result_title);
        r_body=view.findViewById(R.id.result_body);
        release_btn=view.findViewById(R.id.release_button);
        imageView=view.findViewById(R.id.admin_message_image);
        db = FirebaseFirestore.getInstance();


        release_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImage();
                }
                else {
                    Toast.makeText(getActivity(), "Error in Data", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==RQ_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            image_uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),image_uri);
                Glide.with(getActivity()).load(bitmap).into(imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getActivity(),"Error in Select Image",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "Picture Upload failed.",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(),"Select a Photo",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), "Data Uploaded Success",

                                    Toast.LENGTH_SHORT).show();

                        }

                    })

                    .addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "User Registered Failed" + e.toString(),

                                    Toast.LENGTH_SHORT).show();

                            Log.d("TAG", e.toString());

                        }

                    });

        }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Download URL Null.",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(),"Data Not Found",Toast.LENGTH_LONG).show();
                }else {
                    String company = doc.get("Company").toString();
                    UploadUserData(company);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),"Company Not Found",Toast.LENGTH_LONG).show();
            }
        });

    }

}
