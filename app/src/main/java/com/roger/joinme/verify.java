package com.roger.joinme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class verify extends AppCompatActivity {
    private Button user;
    private Button homepage;
    private Button selfpage;
    private Button activitypage;
    private Button friendpage;
    private Button logout;
    private ImageButton chatroom;
    private ImageButton favorite;
    private ImageButton jo;
    private ImageButton notice;
    private ImageButton setting;
    private AppBarConfiguration mAppBarConfiguration;
    public TextView userAccount,userName,userSex,userAge,userPhone,activityTitle;
    public CollectionReference[] docRef = new CollectionReference[1000000];

    public verify(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);

        initViews();
        setListeners();
        displayPage();
        getDBlistener();
    }

    public void displayPage(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("organizerID").equals(MainActivity.useraccount)){
//                                    System.out.println("333");
                                    db.collection("activity")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(db.collection("activity").document().collection("participant") != null){
                                                                activityTitle.setText(document.getString("title"));
                                                                db.collection("user")
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                        userAccount.setText(document.getString("name"));
                                                                                        userName.setText(document.getString("name"));
                                                                                        userPhone.setText(document.getString("cellphone"));
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
                            }
                        }
                    }
                });
    }

    //監聽資料庫
    public void getDBlistener() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (int j = 0; j < MainActivity.count; j++) {
            docRef[j] = db.collection("activity").document(MainActivity.docString[j]).collection("participant");
            docRef[j].addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("TAG", "Listen failed.");
                        return;
                    }
                    for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (doc.getType()) {
                            case ADDED:
                                db.collection("user")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if ((doc.getDocument().getString("account")).equals(document.getString("email"))) {
                                                            userAccount.setText(document.getString("name"));
                                                            userName.setText(document.getString("name"));
                                                            userPhone.setText(document.getString("cellphone"));
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                activityTitle.setText("");
//                            userAccount.setText();
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initViews()
    {
        user=(Button)findViewById(R.id.btn_user);
        homepage=(Button)findViewById(R.id.btn_to_homepage);
        selfpage=(Button)findViewById(R.id.btn_to_selfpage);
        activitypage=(Button)findViewById(R.id.btn_to_jo);
        friendpage=(Button)findViewById(R.id.btn_to_notice);
        logout=(Button)findViewById(R.id.btn_logout);
        chatroom=(ImageButton)findViewById(R.id.imgbtn_chatroom);
        favorite=(ImageButton)findViewById(R.id.imgbtn_favorite);
        jo=(ImageButton)findViewById(R.id.imgbtn_jo);
        notice=(ImageButton)findViewById(R.id.imgbtn_notice);
        setting=(ImageButton)findViewById(R.id.imgbtn_setting);
        userAccount=(TextView)findViewById(R.id.useraccount);
        userName=(TextView)findViewById(R.id.userName);
        userAge=(TextView)findViewById(R.id.userAge);
        userPhone=(TextView)findViewById(R.id.userPhone);
        userSex=(TextView)findViewById(R.id.userSex);
        activityTitle=(TextView)findViewById(R.id.activityTitle);
    }

    private void initData()
    {
    }

    private void setListeners()
    {
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(verify.this, informationedit.class);
                startActivity(intent);
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(verify.this, homepage.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(verify.this, MainActivity.class);
                startActivity(intent);
            }
        });

        activitypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(verify.this, allactivity.class);
                startActivity(intent);
            }
        });

        selfpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(verify.this, selfpage.class);
                startActivity(intent);
            }
        });

//        chatroom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(verify.this, chatroom.class);
//                startActivity(intent);
//            }
//        });

        friendpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(verify.this, friend.class);
                startActivity(intent);
            }
        });

//        favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(verify.this, favorite.class);
//                startActivity(intent);
//            }
//        });



//        jo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(verify.this, jo.class);
//                startActivity(intent);
//            }
//        });

//        notice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(verify.this, notice.class);
//                startActivity(intent);
//            }
//        });
//
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(verify.this, setting.class);
//                startActivity(intent);
//            }
//        });

    }
}

