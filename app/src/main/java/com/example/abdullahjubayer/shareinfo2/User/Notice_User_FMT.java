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

public class Notice_User_FMT extends Fragment {

    FirebaseFirestore db;
    ArrayList<String> ser_title=new ArrayList<>();
    ArrayList<String>ser_body=new ArrayList<>();
    ArrayList<String>ser_image=new ArrayList<>();
    ListView listView;
    ProgressBar progressBar ;

    public Notice_User_FMT() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice__user, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit_show_service);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        listView=view.findViewById(R.id.user_service_listview);
        db = FirebaseFirestore.getInstance();



        Intent intent=getActivity().getIntent();
        final String com=intent.getStringExtra("Company");
        //Log.i("received frm usr servie",com);

        loaddata(com);



    }

    private void loaddata(final String com) {

        progressBar.setVisibility(View.VISIBLE);
        DocumentReference user = db.collection("All_Company Message").document(com);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    DocumentSnapshot doc = task.getResult();

                    db.collection("All_Company Message").document(com).collection("Message").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            ser_title.clear();
                            ser_body.clear();
                            ser_image.clear();

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {

                                ser_title.add(doc.get("Message_Title").toString());
                                ser_body.add(doc.get("Message_Body").toString());
                                ser_image.add(doc.get("Message_image").toString());

                            }
                            aj_user_service_adapter adapter = new aj_user_service_adapter(getActivity(), ser_title,ser_body,ser_image);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),"No Service Provided",Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),"Task Failed",Toast.LENGTH_LONG).show();
            }
        });

    }
}
