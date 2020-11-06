package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

public class noticeupdate extends AppCompatActivity {

    private List<item> itemList;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private itemAdapter itemadapter;
//    private StorageReference UserProfileImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticeupdate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        itemList = new ArrayList<>();

        initView();

        db.collection("user").document(currentUserID).collection("notification")
                .orderBy("millisecond", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentt : task.getResult()) {
                                String from = documentt.getString("from");
                                String type = documentt.getString("type");
                                if(documentt.contains("islook")){
                                    if(documentt.contains("activityname")){
                                        String activityname = documentt.getString("activityname");
                                        itemList.add(new item(from,type,activityname,"look",documentt.getId()));
                                        itemadapter.notifyDataSetChanged();
                                    }else{
                                        itemList.add(new item(from,type,"none","look",documentt.getId()));
                                        itemadapter.notifyDataSetChanged();
                                    }
                                }else{
                                    if(documentt.contains("activityname")){
                                        String activityname = documentt.getString("activityname");
                                        itemList.add(new item(from,type,activityname,"not",documentt.getId()));
                                        itemadapter.notifyDataSetChanged();
                                    }else{
                                        itemList.add(new item(from,type,"none","not",documentt.getId()));
                                        itemadapter.notifyDataSetChanged();
                                    }
                                }


                            }
                        }
                    }
                });
    }

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemadapter = new itemAdapter(this, itemList);
        recyclerView.setAdapter(itemadapter);
    }

    //鎖手機的返回鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
                event.startTracking();
                Intent intent = new Intent();
                intent.setClass(noticeupdate.this, home.class);
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
}
