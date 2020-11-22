package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class evaluateActivity extends AppCompatActivity {

    private List<evaluate> evaluateList;

    private String currentUserID,activityname;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private evaluateAdapter evaluateadapter;
    private StorageReference UserProfileImagesRef;

    private TextView title;
    private Button btn_evaluate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activityname = getIntent().getExtras().get("activityname").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        evaluateList = new ArrayList<>();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        initView();

        setListeners();

        db.collection("activity").document(activityname).
                collection("participant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!document.contains("evaluate") && !document.getId().equals(currentUserID)){
                                    db.collection("user").document(document.getId()).collection("profile")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot documentt : task.getResult()) {
                                                            if (documentt.contains("name") && documentt.contains("image")) {
                                                                String name = documentt.getString("name");
                                                                String id = documentt.getString("currentUserID");
                                                                UserProfileImagesRef.child(id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        evaluateList.add(new evaluate(
                                                                                name, uri, id, activityname));
                                                                        evaluateadapter.notifyDataSetChanged();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception exception) {
                                                                        // Handle any errors
                                                                    }
                                                                });
                                                            } else if (documentt.contains("name")) {
                                                                String name = documentt.getString("name");
                                                                String id = documentt.getString("currentUserID");
                                                                UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        evaluateList.add(new evaluate(
                                                                                name, uri, id, activityname));
                                                                        evaluateadapter.notifyDataSetChanged();
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

    //鎖手機的返回鍵
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
//                event.startTracking();
//                Intent intent = new Intent();
//                intent.setClass(evaluateActivity.this, home.class);
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

    private void setListeners() {
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> evaluatemap = new HashMap<>();
                evaluatemap.put("evaluate", true);
                evaluatemap.put("evaluate_from", currentUserID);
                evaluatemap.put("star", 5);

                db.collection("activity").document(activityname).
                        collection("participant")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(!document.contains("evaluate") && !document.getId().equals(currentUserID)){
                                            String UserID=document.getId();
                                            db.collection("activity").document(activityname)
                                                    .collection("participant")
                                                    .document(UserID)
                                                    .set(evaluatemap, SetOptions.merge())
                                                    .addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()) {
                                                                Map<String, Object> evaluatemap2 = new HashMap<>();
                                                                evaluatemap2.put("activityname", activityname);
                                                                evaluatemap2.put("evaluate_from", currentUserID);
                                                                evaluatemap2.put("star", 5);

                                                                db.collection("user").document(UserID)
                                                                        .collection("evaluate")
                                                                        .document()
                                                                        .set(evaluatemap2)
                                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Intent settingsIntent = new Intent(evaluateActivity.this, home.class);
                                                                                    startActivity(settingsIntent);
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });

                                        }
                                    }
                                }
                            }
                        });
                btn_evaluate.setText("評價完成");
                btn_evaluate.setEnabled(false);
                Toast.makeText(evaluateActivity.this,
                        "評價完成", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void initView(){
        title = (TextView) findViewById(R.id.Title);
        title.setText(activityname);
        btn_evaluate=(Button) findViewById(R.id.btn_evaluate);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        evaluateadapter = new evaluateAdapter(this, evaluateList);
        recyclerView.setAdapter(evaluateadapter);
    }


}


