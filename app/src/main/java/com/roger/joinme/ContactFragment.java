package com.roger.joinme;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private View contactFragmentView;
    private List<userprofile> userprofileList;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private userprofileAdapter userprofileadapter;
    private StorageReference UserProfileImagesRef;

    public ContactFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactFragmentView=inflater.inflate(R.layout.fragment_contact, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        userprofileList = new ArrayList<>();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        initView();
        RetrieveAndDisplayContact();

        // Inflate the layout for this fragment
        return contactFragmentView;
    }

    private void RetrieveAndDisplayContact() {
        db.collection("user").document(currentUserID).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("user").document(document.getId()).collection("profile")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentt : task.getResult()) {
                                                        if (documentt.contains("name") && documentt.contains("image")) {
                                                            String name = documentt.getString("name");
                                                            String status = documentt.getString("status");
                                                            String id = documentt.getString("currentUserID");
                                                            UserProfileImagesRef.child(id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    // Got the download URL for 'users/me/profile.png'
                                                                    userprofileList.add(new userprofile(
                                                                            name, status, uri, id,"friend"));
                                                                    userprofileadapter.notifyDataSetChanged();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception exception) {
                                                                    // Handle any errors
                                                                }
                                                            });
                                                        }
                                                        else if(documentt.contains("name")){
                                                            String name=documentt.getString("name");
                                                            String status=documentt.getString("status");
                                                            String id=documentt.getString("currentUserID");
                                                            UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    // Got the download URL for 'users/me/profile.png'
                                                                    userprofileList.add(new userprofile(
                                                                            name, status, uri, id,"friend"));
                                                                    userprofileadapter.notifyDataSetChanged();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception exception) {
                                                                    // Handle any errors
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) contactFragmentView.findViewById(R.id.contacts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(contactFragmentView.getContext()));
        userprofileadapter = new userprofileAdapter(contactFragmentView.getContext(), userprofileList);
        recyclerView.setAdapter(userprofileadapter);
    }
}