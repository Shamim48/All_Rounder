package com.example.abdullahjubayer.shareinfo2.User;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abdullahjubayer.shareinfo2.R;
import com.example.abdullahjubayer.shareinfo2.aj_User_Photogallery;
import com.example.abdullahjubayer.shareinfo2.aj_user_photogallery_adapter;
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

public class PhotosUser_FMT extends Fragment {


    FirebaseFirestore db;
    ArrayList<String> title=new ArrayList<>();
    ArrayList<String>image=new ArrayList<>();
    ListView listView;
    ProgressBar progressBar ;

    public PhotosUser_FMT() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos_user__fmt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit_user_photogalarryF);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        listView=view.findViewById(R.id.user_photogallery_listView);
        db = FirebaseFirestore.getInstance();

        Intent intent=getActivity().getIntent();
        String com=intent.getStringExtra("Company");
        Log.i("Company received emp",com);
        Toast.makeText(getActivity(),com,Toast.LENGTH_LONG).show();

        listview(com);
    }


    public  void listview(final String com){

        progressBar.setVisibility(View.VISIBLE);
        Log.i("Companyyyyyyy",com);
        DocumentReference user = db.collection("All_Photogallery").document(com);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    DocumentSnapshot doc = task.getResult();

                    db.collection("All_Photogallery").document(com).collection("Image").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            title.clear();
                            image.clear();

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                title.add(doc.get("Title").toString());
                                image.add(doc.get("Image").toString());

                            }
                            aj_user_photogallery_adapter adapter = new aj_user_photogallery_adapter(getContext(),title,image);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),"NO Picture Added",Toast.LENGTH_LONG).show();
            }
        });

    }

}
