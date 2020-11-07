package com.roger.joinme;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class totalEvaluteActivity extends AppCompatActivity {

    private String currentUserID, UserID;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileImagesRef, UserActImageRef;
    private List<totalEvaluate> totalEvaluateList;
    private totalEvaluateAdapter totalEvaluateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalevaluate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserID = mAuth.getCurrentUser().getUid();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        totalEvaluateList = new ArrayList<>();

        initView();

        //評價內容
        db.collection("user").document(currentUserID).collection("profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("user").document(currentUserID).collection("evaluate")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String activityName = document.getString("activityname");
                                                        String evaluateID = document.getString("evaluate_from");
                                                        String evaluateContent = document.getString("evaluate_content");
                                                        Double star = document.getDouble("star");
                                                        System.out.println(evaluateID +"2");
                                                        db.collection("user").document(evaluateID).collection("profile")
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                String userName = document.getString("name");
                                                                                if (document.contains("image")) {
                                                                                    UserProfileImagesRef.child(evaluateID + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                        @Override
                                                                                        public void onSuccess(Uri uri) {
                                                                                            // Got the download URL for 'users/me/profile.png'
                                                                                            totalEvaluateList.add(new totalEvaluate(uri, evaluateID, activityName, evaluateContent, userName, star));
                                                                                            totalEvaluateAdapter.notifyDataSetChanged();
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception exception) {
                                                                                            // Handle any errors
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                        @Override
                                                                                        public void onSuccess(Uri uri) {
                                                                                            // Got the download URL for 'users/me/profile.png'
                                                                                            totalEvaluateList.add(new totalEvaluate(uri, evaluateID, activityName, evaluateContent, userName, star));
                                                                                            totalEvaluateAdapter.notifyDataSetChanged();
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
                        }
                    }
                });
    }
    public void initView(){
        //好友列表
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.evalutePage);
        LinearLayoutManager recycle = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recycle);
        totalEvaluateAdapter = new totalEvaluateAdapter(this, totalEvaluateList);
        recyclerView.setAdapter(totalEvaluateAdapter);
    }
}