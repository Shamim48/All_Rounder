package com.example.abdullahjubayer.shareinfo2.User;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abdullahjubayer.shareinfo2.R;
import com.example.abdullahjubayer.shareinfo2.aj_ShowHomePage;
import com.example.abdullahjubayer.shareinfo2.aj_Show_employee_user;
import com.example.abdullahjubayer.shareinfo2.aj_UserHomePage;
import com.example.abdullahjubayer.shareinfo2.aj_User_Photogallery;
import com.example.abdullahjubayer.shareinfo2.aj_User_home_page_Adapter;
import com.example.abdullahjubayer.shareinfo2.aj_User_reply_TO_Admin;
import com.example.abdullahjubayer.shareinfo2.aj_show_service_to_user;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_User_FMT extends Fragment {
    ImageView imageView;
    TextView titel_t,body_b;
    ImageButton home_page,employee_page,sevice_page,send_message_page,show_photo;
    String title,image,desc,company;
    ProgressBar progressBar ;
    FirebaseFirestore db;
    ArrayList<String> title_=new ArrayList<>();
    ArrayList<String>image_=new ArrayList<>();
    ArrayList<String>description=new ArrayList<>();
   // ArrayList<String>company=new ArrayList<>();
    ListView listView;


    public Home_User_FMT() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home__user__fm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView=view.findViewById(R.id.list_viewUHF);

       db=FirebaseFirestore.getInstance();

        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit_show_home_page_touserP);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.VISIBLE);

/*
        Intent intent=getActivity().getIntent();
        title=intent.getStringExtra("title");
        image=intent.getStringExtra("image");
        desc=intent.getStringExtra("Description");
        company=intent.getStringExtra("Company");

      //  Log.i("Company received",company);


        imageView=view.findViewById(R.id.user_show_image);
        titel_t=view.findViewById(R.id.user_show_title);
        body_b=view.findViewById(R.id.user_show_body);


        load_Data();





    }

    private void load_Data() {
        titel_t.setText(title);
        body_b.setText(desc);
        Glide.with(getActivity()).load(image).into(imageView);
        progressBar.setVisibility(View.INVISIBLE);

        */

loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference user = db.collection("All_Home_Page").document();
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    db.collection("All_Home_Page").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            title_.clear();
                            image_.clear();
                            description.clear();

                            for (DocumentSnapshot snapshot : documentSnapshots) {


                                String tit = snapshot.get("Title").toString();
                                String home_p = snapshot.get("Homepage_Img").toString();
                                String des = snapshot.get("Description").toString();
                                String comp = snapshot.get("Company").toString();

                                if (!tit.isEmpty() && !home_p.isEmpty() && !des.isEmpty() && !comp.isEmpty()) {
                                    title_.add(tit);
                                    image_.add(home_p);
                                    description.add(des);

                                } else {
                                    Toast.makeText(getActivity(), "Error in Home Page", Toast.LENGTH_LONG).show();
                                }

                            }

                            User_home_Fragment_Adapter adapter = new User_home_Fragment_Adapter(getActivity(), title_, image_, description);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Home Page not found", Toast.LENGTH_LONG).show();
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
