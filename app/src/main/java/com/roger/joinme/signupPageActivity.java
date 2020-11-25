package com.roger.joinme;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class signupPageActivity extends AppCompatActivity {

    private List<signuppage> signupList;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private signuppageAdapter signuppageAdapter;
    public double activityLat = 0, activityLong = 0;
    private StorageReference UserActImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        signupList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        UserActImageRef = FirebaseStorage.getInstance().getReference();

        activityLat = Double.parseDouble(getIntent().getExtras().get("activityLat").toString());
        activityLong = Double.parseDouble(getIntent().getExtras().get("activityLong").toString());

        initView();

        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getGeoPoint("geopoint").getLatitude() == activityLat
                                        && document.getGeoPoint("geopoint").getLongitude() == activityLong) {
                                    String actTitle = document.getString("title");
                                    String actType = document.getString("activityType");
                                    String activityLocation = document.getString("location");
                                    Date startTime = document.getTimestamp("startTime").toDate();
                                    SimpleDateFormat ft = new SimpleDateFormat(" yyyy-MM-dd HH:mm ");
                                    String actTime = ft.format(startTime);
                                    Boolean isImage = document.getBoolean("img");
                                    String actTypeRes = actType.equals("運動") ? "球類" : actType;

                                    if (isImage) {
                                        UserActImageRef.child(actTitle + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // Got the download URL for 'users/me/profile.png'
                                                signupList.add(new signuppage(uri, actTitle, activityLocation, actTime));
                                                signuppageAdapter.notifyDataSetChanged();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle any errors
                                            }
                                        });
                                    } else {
                                        UserActImageRef.child(actTypeRes + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // Got the download URL for 'users/me/profile.png'
                                                signupList.add(new signuppage(uri, actTitle, activityLocation, actTime));
                                                signuppageAdapter.notifyDataSetChanged();
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

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.signupList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        signuppageAdapter = new signuppageAdapter(this, signupList);
        recyclerView.setAdapter(signuppageAdapter);
    }
}
