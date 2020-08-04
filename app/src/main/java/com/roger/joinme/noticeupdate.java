package com.roger.joinme;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        itemList = new ArrayList<>();
//        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        initView();

        db.collection("user").document(currentUserID).collection("notification")
                .orderBy("millisecond", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentt : task.getResult()) {
                                String from = documentt.getString("from");
                                String type = documentt.getString("type");
                                itemList.add(new item(from,type));
                                itemadapter.notifyDataSetChanged();
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


}
