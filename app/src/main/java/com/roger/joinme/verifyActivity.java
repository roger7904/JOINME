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

public class verifyActivity extends AppCompatActivity {

    private List<verify> verifyList;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private verifyAdapter verifyadapter;
    private StorageReference UserProfileImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        verifyList = new ArrayList<>();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        initView();
        db.collection("join_act_request")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("organizerID").equals(currentUserID)){
                                    String activity=document.getId();
                                    db.collection("join_act_request").document(document.getId()).collection("UserID")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot documentt : task.getResult()) {
                                                            db.collection("user").document(documentt.getId())
                                                                    .collection("profile")
                                                                    .document(documentt.getId())
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            DocumentSnapshot d = task.getResult();
                                                                            if (task.isSuccessful()) {
                                                                                if (d.contains("name") && d.contains("image") && d.contains("gender")) {
                                                                                    String name = d.getString("name");
                                                                                    String id = d.getString("currentUserID");
                                                                                    String gender = d.getString("gender");
                                                                                    String age = d.getString("age");
                                                                                    String phone = d.getString("phone");
                                                                                    UserProfileImagesRef.child(id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                        @Override
                                                                                        public void onSuccess(Uri uri) {
                                                                                            // Got the download URL for 'users/me/profile.png'
                                                                                            verifyList.add(new verify(
                                                                                                    uri,name,gender,age,phone,id,activity ));
                                                                                            verifyadapter.notifyDataSetChanged();
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception exception) {
                                                                                            // Handle any errors
                                                                                        }
                                                                                    });
                                                                                } else if (d.contains("name") && d.contains("gender")) {
                                                                                    String name = d.getString("name");
                                                                                    String id = d.getString("currentUserID");
                                                                                    String gender = d.getString("gender");
                                                                                    String age = d.getString("age");
                                                                                    String phone = d.getString("phone");
                                                                                    UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                        @Override
                                                                                        public void onSuccess(Uri uri) {
                                                                                            // Got the download URL for 'users/me/profile.png'
                                                                                            verifyList.add(new verify(
                                                                                                    uri,name,gender,age,phone,id,activity ));
                                                                                            verifyadapter.notifyDataSetChanged();
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
                            }
                        }
                    }
                });
    }

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.verify_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        verifyadapter = new verifyAdapter(this, verifyList);
        recyclerView.setAdapter(verifyadapter);
    }

    //鎖手機的返回鍵
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
//                event.startTracking();
//                Intent intent = new Intent();
//                intent.setClass(verifyActivity.this, home.class);
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
