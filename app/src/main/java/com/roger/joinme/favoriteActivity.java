package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class favoriteActivity extends AppCompatActivity {

    private List<favorite> favoriteList;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private favoriteAdapter favoriteadapter;
    private StorageReference UserProfileImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritee);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        favoriteList = new ArrayList<>();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference();

        initView();
        db.collection("user")
                .document(currentUserID)
                .collection("favorite")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("activity").document(document.getId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot d = task.getResult();
                                            if (task.isSuccessful()) {
                                                if (d.getBoolean("img")) {
                                                    String name = d.getId();
                                                    String time = DateFormat.format("yyyy/MM/dd mm:ss", d.getTimestamp("startTime").getSeconds()*1000).toString();
                                                    String place = d.getString("location");
                                                    UserProfileImagesRef.child(name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            // Got the download URL for 'users/me/profile.png'
                                                            favoriteList.add(new favorite(
                                                                    uri,name,place,time));
                                                            favoriteadapter.notifyDataSetChanged();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            // Handle any errors
                                                        }
                                                    });
                                                }else if (d.getString("activityType").equals("商家優惠")) {
                                                    String name = d.getId();
                                                    String time = DateFormat.format("yyyy/MM/dd mm:ss", d.getTimestamp("startTime").getSeconds()*1000).toString();
                                                    String place = d.getString("location");
                                                    UserProfileImagesRef.child("商家優惠.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            // Got the download URL for 'users/me/profile.png'
                                                            favoriteList.add(new favorite(
                                                                    uri,name,place,time));
                                                            favoriteadapter.notifyDataSetChanged();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            // Handle any errors
                                                        }
                                                    });
                                                }else if (d.getString("activityType").equals("KTV")) {
                                                    String name = d.getId();
                                                    String time = DateFormat.format("yyyy/MM/dd mm:ss", d.getTimestamp("startTime").getSeconds()*1000).toString();
                                                    String place = d.getString("location");
                                                    UserProfileImagesRef.child("KTV.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            // Got the download URL for 'users/me/profile.png'
                                                            favoriteList.add(new favorite(
                                                                    uri,name,place,time));
                                                            favoriteadapter.notifyDataSetChanged();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            // Handle any errors
                                                        }
                                                    });
                                                }else if (d.getString("activityType").equals("限時")) {
                                                    String name = d.getId();
                                                    String time = DateFormat.format("yyyy/MM/dd mm:ss", d.getTimestamp("startTime").getSeconds()*1000).toString();
                                                    String place = d.getString("location");
                                                    UserProfileImagesRef.child("限時.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            // Got the download URL for 'users/me/profile.png'
                                                            favoriteList.add(new favorite(
                                                                    uri,name,place,time));
                                                            favoriteadapter.notifyDataSetChanged();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            // Handle any errors
                                                        }
                                                    });
                                                }else if (d.getString("activityType").equals("球類")) {
                                                    String name = d.getId();
                                                    String time = DateFormat.format("yyyy/MM/dd mm:ss", d.getTimestamp("startTime").getSeconds()*1000).toString();
                                                    String place = d.getString("location");
                                                    UserProfileImagesRef.child("球類.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            // Got the download URL for 'users/me/profile.png'
                                                            favoriteList.add(new favorite(
                                                                    uri,name,place,time));
                                                            favoriteadapter.notifyDataSetChanged();
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
                Intent intent = new Intent();
                intent.setClass(favoriteActivity.this, home.class);
                startActivity(intent);
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.favorite_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteadapter = new favoriteAdapter(this, favoriteList);
        recyclerView.setAdapter(favoriteadapter);
    }

}
