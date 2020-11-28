package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
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
    private Button chatView,addFriend;
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
    private String currentUserID,UserID;
    private TextView userProfileName, userAge, userSex, userStatus;
    private personholdactAdapter personholdactadapter;
    private personalactRecAdapter personalactRecadapter;
    private personalFriAdapter personalFriAdapter;
    private List<personal> personalList,personalRecList,personalFriList;
    public String cussrentUserName, cussrentUserImg;
    public LinearLayout evaluatePage;
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

        currentUserID = getIntent().getExtras().get("visit_user_id").toString();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        UserActImageRef = FirebaseStorage.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        final DocumentReference docRefff = db.collection("user").document(currentUserID).collection("profile")
                .document(currentUserID);
        docRefff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    cussrentUserName = snapshot.getString("name");
                    cussrentUserImg = currentUserID + ".jpg";
                }
            }
        });

        personalList = new ArrayList<>();
        personalRecList = new ArrayList<>();
        personalFriList = new ArrayList<>();

        initViews();
        initData();
        setListeners();

        if(currentUserID.equals(UserID)){
            chatView.setVisibility(View.INVISIBLE);
            chatView.setEnabled(false);
            addFriend.setVisibility(View.INVISIBLE);
            addFriend.setEnabled(false);
        }

        //個人列表上方user資訊
        final DocumentReference docRef = db.collection("user").document(currentUserID).collection("profile")
                .document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    String userName = snapshot.getString("name");
                    String userage = snapshot.getString("age");
                    String usersex = snapshot.getString("gender");
                    String userstatus = snapshot.getString("status");
                    if (snapshot != null && snapshot.exists() && snapshot.contains("name") && snapshot.contains("image")) {
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

                    } else if (snapshot != null && snapshot.exists() && snapshot.contains("name")) {
                        Glide.with(personalpage.this)
                                .load(R.drawable.head)
                                .circleCrop()
                                .into(userProfileImage);
                    }
                    userProfileName.setText(userName);
                    userAge.setText("年齡：" + userage);
                    userSex.setText("性別：" + usersex);
                    userStatus.setText("狀態：" + userstatus);
                }
            }
        });

        db.collection("user").document(currentUserID)
                .collection("evaluate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double score = 0;
                        int count = 0;
                        double finalescore = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 score = score + document.getDouble("star");
                                 count++;
                            }
                        }
                        finalescore = score / count;
                        DecimalFormat df = new DecimalFormat("##.0");
                        evaluation.setText(String.valueOf(df.format(finalescore)));
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
                    }

                });


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
                                                    String userID = document.getString("currentUserID");
                                                    if (document.contains("image")) {
                                                        UserProfileImagesRef.child(friends + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                // Got the download URL for 'users/me/profile.png'
                                                                personalFriList.add(new personal(uri, userID,"","",userName));
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
                                                                personalFriList.add(new personal(uri, userID,"","",userName));
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
        userStatus = (TextView) findViewById(R.id.Status);
        userAge = (TextView) findViewById(R.id.userAge);
        userSex = (TextView) findViewById(R.id.userSex);
        first = (TextView) findViewById(R.id.first);
        second = (TextView) findViewById(R.id.second);
        third = (TextView) findViewById(R.id.third);
        forth = (TextView) findViewById(R.id.forth);
        fifth = (TextView) findViewById(R.id.fifth);
        evaluation = (TextView) findViewById(R.id.score);
        evaluatePage = (LinearLayout) findViewById(R.id.evaluatePage);
        addFriend = (Button) findViewById(R.id.addFriend);

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
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("visit_user_id", currentUserID);
                bundle.putString("visit_user_name", cussrentUserName);
                bundle.putString("visit_image", cussrentUserImg);
                myIntent.putExtras(bundle);
                myIntent.setClass(personalpage.this, ChatActivity.class);
                startActivity(myIntent);
            }
        });
        evaluatePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("visit_user_id", currentUserID);
                myIntent.putExtras(bundle);
                myIntent.setClass(personalpage.this, totalEvaluteActivity.class);
                startActivity(myIntent);
            }
        });
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String visit_user_id = currentUserID;
                Intent profileIntent = new Intent(personalpage.this , ProfileActivity.class);
                profileIntent.putExtra("visit_user_id", visit_user_id);
                profileIntent.putExtra("fromPersonal","personal");
                startActivity(profileIntent);
            }
        });
    }

    //鎖手機的返回鍵
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
//                event.startTracking();
//                Intent intent = new Intent();
//                intent.setClass(personalpage.this, home.class);
//                startActivity(intent);
//            }else{
//                onBackPressed();
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public  boolean onKeyUp(int keyCode, KeyEvent event){
//        return super.onKeyUp(keyCode, event);
//    }
}
