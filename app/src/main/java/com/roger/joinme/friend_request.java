package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class friend_request extends AppCompatActivity {

    private List<request> requestList;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private requestAdapter requestadapter;
    private StorageReference UserProfileImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        requestList = new ArrayList<>();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        initView();

        db.collection("add_friend_request").document(currentUserID).
                collection("UserID")
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
                                                                    requestList.add(new request(
                                                                            name, status, uri, id));
                                                                    requestadapter.notifyDataSetChanged();
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
                                                                    requestList.add(new request(
                                                                            name, status, uri, id));
                                                                    requestadapter.notifyDataSetChanged();
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

    //鎖手機的返回鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
                event.startTracking();
            }else{
                onBackPressed();
            }
        }
        return false;
    }

    @Override
    public  boolean onKeyUp(int keyCode, KeyEvent event){
        return super.onKeyUp(keyCode, event);
    }

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestadapter = new requestAdapter(this, requestList);
        recyclerView.setAdapter(requestadapter);
    }


}


