package com.roger.joinme;

import android.content.Intent;
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

public class totalEvaluteActivity extends AppCompatActivity {

    public TextView starFive,starFour,starThree,starTwo,starOne;
    public TextView fiveText,fourText,threeText,twoText,oneText;
    public String starCount1,starCount2,starCount3,starCount4,starCount5;
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
        currentUserID = getIntent().getExtras().get("visit_user_id").toString();
        UserID = mAuth.getCurrentUser().getUid();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        totalEvaluateList = new ArrayList<>();

        initView();
        setListeners();

        fiveText.setText("(0)");
        fourText.setText("(0)");
        threeText.setText("(0)");
        twoText.setText("(0)");
        oneText.setText("(0)");

        fiveText.bringToFront();
        fourText.bringToFront();
        threeText.bringToFront();
        twoText.bringToFront();
        oneText.bringToFront();

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
                                                    int star5 = 0;
                                                    int star4 = 0;
                                                    int star3 = 0;
                                                    int star2 = 0;
                                                    int star1 = 0;

                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String activityName = document.getString("activityname");
                                                        String evaluateID = document.getString("evaluate_from");
                                                        String evaluateContent = document.getString("evaluate_content");
                                                        Double star = document.getDouble("star");
                                                        if(star == 5.0){
                                                            star5++;
                                                        }else if(star == 4.0){
                                                            star4++;
                                                        }else if(star == 3.0){
                                                            star3++;
                                                        }else if(star == 2.0){
                                                            star2++;
                                                        }else{
                                                            star1++;
                                                        }

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
                                                        starCount1 = String.valueOf(star1);
                                                        starCount2 = String.valueOf(star2);
                                                        starCount3 = String.valueOf(star3);
                                                        starCount4 = String.valueOf(star4);
                                                        starCount5 = String.valueOf(star5);

                                                        fiveText.setText("(" + starCount5 + ")");
                                                        fiveText.setVisibility(View.VISIBLE);
                                                        fourText.setText("(" + starCount4 + ")");
                                                        fourText.setVisibility(View.VISIBLE);
                                                        threeText.setText("(" + starCount3 + ")");
                                                        threeText.setVisibility(View.VISIBLE);
                                                        twoText.setText("(" + starCount2 + ")");
                                                        twoText.setVisibility(View.VISIBLE);
                                                        oneText.setText("(" + starCount1 + ")");
                                                        oneText.setVisibility(View.VISIBLE);
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
        starFive = (TextView) findViewById(R.id.starFive);
        starFour = (TextView) findViewById(R.id.starFour);
        starThree = (TextView) findViewById(R.id.starThree);
        starTwo = (TextView) findViewById(R.id.starTwo);
        starOne = (TextView) findViewById(R.id.starOne);
        fiveText = (TextView) findViewById(R.id.fiveText);
        fourText = (TextView) findViewById(R.id.fourText);
        threeText = (TextView) findViewById(R.id.threeText);
        twoText = (TextView) findViewById(R.id.twoTextt);
        oneText = (TextView) findViewById(R.id.oneText);

        //好友列表
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.evalutePage);
        LinearLayoutManager recycle = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recycle);
        totalEvaluateAdapter = new totalEvaluateAdapter(this, totalEvaluateList);
        recyclerView.setAdapter(totalEvaluateAdapter);
    }
    private void setListeners() {
        starFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("star_No", "5");
                myIntent.putExtras(bundle);
                myIntent.setClass(totalEvaluteActivity.this, separateEvaluateActivity.class);
                startActivity(myIntent);
            }
        });
        starFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("star_No", "4");
                myIntent.putExtras(bundle);
                myIntent.setClass(totalEvaluteActivity.this, separateEvaluateActivity.class);
                startActivity(myIntent);
            }
        });
        starThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("star_No", "3");
                myIntent.putExtras(bundle);
                myIntent.setClass(totalEvaluteActivity.this, separateEvaluateActivity.class);
                startActivity(myIntent);
            }
        });
        starTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("star_No", "2");
                myIntent.putExtras(bundle);
                myIntent.setClass(totalEvaluteActivity.this, separateEvaluateActivity.class);
                startActivity(myIntent);
            }
        });
        starOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("star_No", "1");
                myIntent.putExtras(bundle);
                myIntent.setClass(totalEvaluteActivity.this, separateEvaluateActivity.class);
                startActivity(myIntent);
            }
        });


    }
}