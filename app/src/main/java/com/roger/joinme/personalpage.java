package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class personalpage extends AppCompatActivity {
    private Button chatView;
    private Button selfView;
    public ImageView activityPhoto;
    public TextView title,evaluation;
    public TextView first,second,third,forth,fifth;
    public int Count = 0;
    public double Score = 0;
    private CircleImageView userProfileImage;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileImagesRef,UserActImageRef;
    private String currentUserID;
    private TextView userProfileName, userAge, userSex;
    private personholdactAdapter personholdactadapter;
    private personalactRecAdapter personalactRecadapter;
    private personalFriAdapter personalFriAdapter;
    private List<personal> personalList,personalRecList,personalFriList;
//    String activityLocation;

    public personalpage()
    {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalpage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        personalList = new ArrayList<>();
        personalRecList = new ArrayList<>();
        personalFriList = new ArrayList<>();

        initViews();
        initData();
        setListeners();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserActImageRef = FirebaseStorage.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        //個人列表上方user資訊

        final DocumentReference docRef = db.collection("user").document(currentUserID).collection("profile")
                .document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists() && snapshot.contains("name") && snapshot.contains("image")) {
                        String userName = snapshot.getString("name");
                        String userage = snapshot.getString("age");
                        String usersex = snapshot.getString("gender");

                        UserProfileImagesRef.child(currentUserID + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Glide.with(personalpage.this)
                                        .load(uri)
                                        .circleCrop()
                                        .into(userProfileImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                        userProfileName.setText(userName);
                        userAge.setText(userage);
                        userSex.setText(usersex);
                    } else if (snapshot != null && snapshot.exists() && snapshot.contains("name")) {
                        String userName = snapshot.getString("name");
                        String userage = snapshot.getString("age");
                        String usersex = snapshot.getString("gender");

                        Glide.with(personalpage.this)
                                .load(R.drawable.head)
                                .circleCrop()
                                .into(userProfileImage);
                        userProfileName.setText(userName);
                        userAge.setText(userage);
                        userSex.setText(usersex);
                    }
                }
            }
        });

        db.collection("user").document(currentUserID)
                .collection("evaluate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double score = 0;
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 score = score + document.getDouble("star");
                                 count++;
                                 Score = score;
                                 Count = count;
                            }
                        }
                    }
                });
        double finalescore = Score / Count;
        evaluation.setText(String.valueOf(finalescore));
        if(finalescore >= 4.5){
            first.setBackground(getResources().getDrawable(R.drawable.brightstar));
            second.setBackground(getResources().getDrawable(R.drawable.brightstar));
            third.setBackground(getResources().getDrawable(R.drawable.brightstar));
            forth.setBackground(getResources().getDrawable(R.drawable.brightstar));
            fifth.setBackground(getResources().getDrawable(R.drawable.brightstar));
        }else if(finalescore >= 3.5){
            first.setBackground(getResources().getDrawable(R.drawable.brightstar));
            second.setBackground(getResources().getDrawable(R.drawable.brightstar));
            third.setBackground(getResources().getDrawable(R.drawable.brightstar));
            forth.setBackground(getResources().getDrawable(R.drawable.brightstar));
            fifth.setBackground(getResources().getDrawable(R.drawable.darkstar));
        }else if(finalescore >= 2.5){
            first.setBackground(getResources().getDrawable(R.drawable.brightstar));
            second.setBackground(getResources().getDrawable(R.drawable.brightstar));
            third.setBackground(getResources().getDrawable(R.drawable.brightstar));
            forth.setBackground(getResources().getDrawable(R.drawable.darkstar));
            fifth.setBackground(getResources().getDrawable(R.drawable.darkstar));
        }else if(finalescore >= 1.5){
            first.setBackground(getResources().getDrawable(R.drawable.brightstar));
            second.setBackground(getResources().getDrawable(R.drawable.brightstar));
            third.setBackground(getResources().getDrawable(R.drawable.darkstar));
            forth.setBackground(getResources().getDrawable(R.drawable.darkstar));
            fifth.setBackground(getResources().getDrawable(R.drawable.darkstar));
        }else if(finalescore >= 0.5){
            first.setBackground(getResources().getDrawable(R.drawable.brightstar));
            second.setBackground(getResources().getDrawable(R.drawable.darkstar));
            third.setBackground(getResources().getDrawable(R.drawable.darkstar));
            forth.setBackground(getResources().getDrawable(R.drawable.darkstar));
            fifth.setBackground(getResources().getDrawable(R.drawable.darkstar));
        }else{
            evaluation.setText("尚無評價");
        }

        //主辦的活動
        db.collection("user").document(currentUserID).collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String activityName = document.getString("activityname");

                                final DocumentReference docRef = db.collection("activity").document(activityName);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot snapshot = task.getResult();
                                                    if (snapshot != null && snapshot.exists()) {
                                                        if(currentUserID.equals(snapshot.getString("organizerID"))){
                                                            String activityLocation = snapshot.getString("location");

                                                            final DocumentReference docReff = db.collection("activity").document(activityName);
                                                            docReff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot snapshot = task.getResult();
                                                                        if (snapshot != null && snapshot.exists() && snapshot.contains("img")){
                                                                            if(!snapshot.getBoolean("img").equals(false)){
                                                                                UserActImageRef.child(activityName + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                    @Override
                                                                                    public void onSuccess(Uri uri) {
                                                                                        // Got the download URL for 'users/me/profile.png'
                                                                                        personalList.add(new personal(uri, currentUserID, activityName, activityLocation,""));
                                                                                        personholdactadapter.notifyDataSetChanged();
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception exception) {
                                                                                        // Handle any errors
                                                                                    }
                                                                                });
                                                                            }else{
                                                                                String actType = "";
                                                                                if(snapshot.getString("activityType").equals("運動")) {
                                                                                    actType = "球類";
                                                                                }else if(snapshot.getString("activityType").equals("KTV")){
                                                                                    actType = "KTV";
                                                                                }else if(snapshot.getString("activityType").equals("商家優惠")){
                                                                                    actType = "商家優惠";
                                                                                }else if(snapshot.getString("activityType").equals("限時")){
                                                                                    actType = "限時";
                                                                                }else{
                                                                                    actType = "其他";
                                                                                }
                                                                                UserActImageRef.child(actType+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                    @Override
                                                                                    public void onSuccess(Uri uri) {
                                                                                        // Got the download URL for 'users/me/profile.png'
                                                                                        personalList.add(new personal(uri, currentUserID, activityName, activityLocation,""));
                                                                                        personholdactadapter.notifyDataSetChanged();
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
                                            }
                                });
                            }
                        }else{
                        }
                    }
                });

        //活動紀錄
        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String actTitle = document.getString("title");
                                String activityLocation = document.getString("location");
                                String actType = document.getString("activityType");
                                Boolean isImage = document.getBoolean("img");
                                String actTypeRes = actType.equals("運動") ? "球類" : actType;

                                db.collection("activity").document(actTitle).collection("participant")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if(document.getString("UserID").equals(currentUserID)){
                                                            if(isImage){
                                                                UserActImageRef.child(actTitle + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        // Got the download URL for 'users/me/profile.png'
                                                                        personalRecList.add(new personal(uri, currentUserID, actTitle, activityLocation,""));
                                                                        personalactRecadapter.notifyDataSetChanged();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception exception) {
                                                                        // Handle any errors
                                                                    }
                                                                });
                                                            }else {
                                                                UserActImageRef.child(actTypeRes + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        // Got the download URL for 'users/me/profile.png'
                                                                        personalRecList.add(new personal(uri, currentUserID, actTitle, activityLocation,""));
                                                                        personalactRecadapter.notifyDataSetChanged();
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
                                            }
                                        });
                            }
                        }
                    }
                });

        //好友列表
        db.collection("user").document(currentUserID).collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String friends = document.getString("UserID");
                                db.collection("user").document(friends).collection("profile")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String userName = document.getString("name");
                                                    if (document.contains("image")) {
                                                        UserProfileImagesRef.child(friends + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                // Got the download URL for 'users/me/profile.png'
                                                                personalFriList.add(new personal(uri, currentUserID,"","",userName));
                                                                personalFriAdapter.notifyDataSetChanged();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                // Handle any errors
                                                            }
                                                        });
                                                    }else{
                                                        UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                // Got the download URL for 'users/me/profile.png'
                                                                personalFriList.add(new personal(uri, currentUserID,"","",userName));
                                                                personalFriAdapter.notifyDataSetChanged();
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

    private void initData() {
    }

    private void initViews() {
        chatView = (Button) findViewById(R.id.chatView);
        title = (TextView) findViewById(R.id.title);
        activityPhoto = (ImageView) findViewById(R.id.activityphoto);
        userProfileImage = (CircleImageView)findViewById(R.id.users_profile_image);
        userProfileName = (TextView) findViewById(R.id.userName);
        userAge = (TextView) findViewById(R.id.userAge);
        userSex = (TextView) findViewById(R.id.userSex);
        first = (TextView) findViewById(R.id.first);
        second = (TextView) findViewById(R.id.second);
        third = (TextView) findViewById(R.id.third);
        forth = (TextView) findViewById(R.id.forth);
        fifth = (TextView) findViewById(R.id.fifth);
        evaluation = (TextView) findViewById(R.id.score);


        //主辦的活動
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.actHold);
        LinearLayoutManager recycle = new LinearLayoutManager(this);
        recycle.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(recycle);
        personholdactadapter = new personholdactAdapter(this, personalList);
        recyclerView.setAdapter(personholdactadapter);

        //活動紀錄
        RecyclerView recyclerViewRec = (RecyclerView) findViewById(R.id.actRec);
        LinearLayoutManager recycleRec = new LinearLayoutManager(this);
        recycleRec.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewRec.setLayoutManager(recycleRec);
        personalactRecadapter = new personalactRecAdapter(this, personalRecList);
        recyclerViewRec.setAdapter(personalactRecadapter);

        //好友列表
        RecyclerView recyclerViewFri = (RecyclerView) findViewById(R.id.friendList);
        LinearLayoutManager recycleFri = new LinearLayoutManager(this);
        recycleFri.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFri.setLayoutManager(recycleFri);
        personalFriAdapter = new personalFriAdapter(this, personalFriList);
        recyclerViewFri.setAdapter(personalFriAdapter);
    }

    private void setListeners() {
        chatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //TODO tzuyen:補上Intent變數
                intent.setClass(personalpage.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}
