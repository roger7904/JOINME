package com.roger.joinme;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class separateEvaluateActivity extends AppCompatActivity {

    public TextView starFive,starFour,starThree,starTwo,starOne;
    public TextView fiveText,fourText,threeText,twoText,oneText,textView3;
    public String starCount1,starCount2,starCount3,starCount4,starCount5;
    private String currentUserID, UserID;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileImagesRef, UserActImageRef;
    private List<totalEvaluate> personalEvaluateList;
    private totalEvaluateAdapter totalEvaluateAdapter;
    public String starNo = "";
    public boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separateevaluate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        starNo = getIntent().getExtras().get("star_No").toString();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserID = mAuth.getCurrentUser().getUid();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        personalEvaluateList = new ArrayList<>();

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
                                                        Double star = document.getDouble("star");
                                                        if((starNo+".0").equals(String.valueOf(star))){
                                                            flag = true;
                                                            String activityName = document.getString("activityname");
                                                            String evaluateID = document.getString("evaluate_from");
                                                            String evaluateContent = document.getString("evaluate_content");

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
                                                                                                personalEvaluateList.add(new totalEvaluate(uri, evaluateID, activityName, evaluateContent, userName, star));
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
                                                                                                personalEvaluateList.add(new totalEvaluate(uri, evaluateID, activityName, evaluateContent, userName, star));
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
                                                    if(flag == false){
                                                        textView3.setVisibility(View.VISIBLE);
                                                        textView3.setText("尚無評價內容");
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
        textView3 = (TextView) findViewById(R.id.textView3);

        //好友列表
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.separatePage);
        LinearLayoutManager recycle = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recycle);
        totalEvaluateAdapter = new totalEvaluateAdapter(this, personalEvaluateList);
        recyclerView.setAdapter(totalEvaluateAdapter);
    }
}
