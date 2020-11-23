package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
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

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FindFriendsActivity extends AppCompatActivity {

    private List<userprofile> userprofileList;
    public int count = 1,x = 0,y = 0;
    public String joineraccount;
    public String[] account = new String[10000];

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private userprofileAdapter userprofileadapter;
    private StorageReference UserProfileImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        userprofileList = new ArrayList<>();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

//        for(x=0;x<MainActivity.count;x++){
//            db.collection("activity")
//                    .document(MainActivity.docString[x])
//                    .collection("participant")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    if(!document.getString("account").equals("0")){
////                                        System.out.println(count);
//                                        account[count] = document.getString("account");
//                                        itemList.add(new item(count,account[count]));
//                                        count++;
//                                    }
//                                }
//                            }
//                        }
//                    });
//        }
        initView();
        db.collection("user")
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
                                                                            name, status, uri, id,"find_friend"));
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
                                                            System.out.println("測試");
                                                            String name=documentt.getString("name");
                                                            String status=documentt.getString("status");
                                                            String id=documentt.getString("currentUserID");
                                                            UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    // Got the download URL for 'users/me/profile.png'
                                                                    userprofileList.add(new userprofile(
                                                                            name, status, uri, id,"find_friend"));
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


//        System.out.println(y);
//        for(y=0;y<=count;y++){
//            System.out.println("00000"+account[y]);
//            y++;
//            itemList.add(new item(y,account[y-1]));
//        }

//        System.out.println("test"+itemListt);
        //userprofileList.add(new userprofile("name","status","image"));

    }

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userprofileadapter = new userprofileAdapter(this, userprofileList);
        recyclerView.setAdapter(userprofileadapter);
    }
    //鎖手機的返回鍵
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
//                event.startTracking();
//                Intent intent = new Intent();
//                intent.setClass(FindFriendsActivity.this, home.class);
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
