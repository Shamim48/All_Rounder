package com.example.abdullahjubayer.shareinfo2.Admin;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abdullahjubayer.shareinfo2.R;
import com.example.abdullahjubayer.shareinfo2.aj_Set_Employee;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Employee_Admin_FMT extends Fragment {
    private static final int RQ_CODE = 101;
    EditText e_name,e_phone,e_email,e_qualification;
    Button emp_s_btn;
    Uri emp_image;
    CircleImageView emp_imageView;
    String downloadUrl;
    FirebaseFirestore db;
    ProgressBar progressBar;

    public Employee_Admin_FMT() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee__admin__fmt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit_admin_set_employee);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        e_name=view.findViewById(R.id.emp_name);
        e_phone=view.findViewById(R.id.emp_phone);
        e_email=view.findViewById(R.id.emp_email);
        e_qualification=view.findViewById(R.id.emp_descrip);
        emp_s_btn=view.findViewById(R.id.emp_save_button);
        emp_imageView=view.findViewById(R.id.employee_img_view);
        db = FirebaseFirestore.getInstance();

        emp_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        emp_s_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    progressBar.setVisibility(View.VISIBLE);
                    getCompanyName();
                }
                else {
                    Toast.makeText(getActivity(),"Pleade Fill Up All data correctly",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public   void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RQ_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            emp_image=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),emp_image);
                Glide.with(getActivity()).load(bitmap).into(emp_imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getActivity(),"Error in Select Image",Toast.LENGTH_LONG).show();
        }
    }

    public void selectImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),RQ_CODE);
    }


    public boolean validate() {
        boolean valid = true;

        String name= e_name.getText().toString();
        String email = e_email.getText().toString();
        String mobile = e_phone.getText().toString();
        String qualification = e_qualification.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            e_name.setError("at least 3 characters");
            valid = false;
        } else {
            e_name.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e_email.setError("enter a valid email address");
            valid = false;
        } else {
            e_email.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=11) {
            e_phone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            e_phone.setError(null);
        }
        if (qualification.isEmpty()){
            e_qualification.setError("Company/Institute Required");
            valid=false;
        } else {
            e_qualification.setError(null);
        }

        return valid;
    }

    private void uploadImage(final String Company_name) {
        String email = e_email.getText().toString();

        final StorageReference storageReference=
                FirebaseStorage.getInstance().getReference("Company_User_Image/"+Company_name+"/"+email+".jpg");

        if (emp_image!=null){
            storageReference.putFile(emp_image).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        UploadUserData(Company_name);
                        Log.i("downloadUrllllll", "onComplete: Url: "+ downloadUrl);
                        Toast.makeText(getContext(), "Picture Upload Success.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }

    }

    private void UploadUserData(String company_name) {

        String name= e_name.getText().toString();
        String email = e_email.getText().toString();
        String mobile = e_phone.getText().toString();
        String qualification = e_qualification.getText().toString();

        if (!(downloadUrl.isEmpty())){
            Map< String, Object > newContact = new HashMap< >();
            newContact.clear();

            newContact.put("Name", name);
            newContact.put("Email", email);
            newContact.put("Phone", mobile);
            newContact.put("Qualification", qualification);
            newContact.put("Picture", downloadUrl);

            db.collection("All_Employee_Data").document(company_name).collection("Information").document().set(newContact)

                    .addOnSuccessListener(new OnSuccessListener< Void >() {

                        @Override

                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "User Registered Success",

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
                    uploadImage(company);

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
