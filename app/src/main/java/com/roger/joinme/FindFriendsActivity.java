package com.roger.joinme;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FindFriendsActivity extends AppCompatActivity {

    public List<userprofile> userprofileList;
    public int count = 1,x = 0,y = 0;
    public String joineraccount;
    public String[] account = new String[10000];

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private userprofileAdapter userprofileadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        userprofileList = new ArrayList<>();

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
                                                        if (documentt.contains("name")) {
                                                            userprofileList.add(new userprofile(
                                                                    documentt.getString("name"),
                                                                    documentt.getString("status"),
                                                                    documentt.getString("image"),
                                                                    documentt.getString("currentUserID")));
                                                            userprofileadapter.notifyDataSetChanged();

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


}
