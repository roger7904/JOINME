package com.roger.joinme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class chatroom extends AppCompatActivity {
    private Button chatwith2;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

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
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        //NavigationView navigationView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);


        initchat();
        initViews();
//        setListeners();
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

    public void initchat() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentt : task.getResult()) {
                                String activityname= documentt.getString("activity");
                                String newestcontent= documentt.getString("newestcontent");
                                db.collection("chat").document(documentt.getId()).collection("participant").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if (document.getString("useraccount").equals(MainActivity.useraccount)) {
                                                            LinearLayout linear=(LinearLayout) findViewById(R.id.linear_addactivitychat);
                                                            LinearLayout l1 = new LinearLayout(getApplication());
                                                            l1.setOrientation(LinearLayout.HORIZONTAL);
                                                            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.sixty));
                                                            layoutParams1.setMargins(0, 0, 0, 10);
                                                            //l1.setLayoutParams(layoutParams1);
                                                            ImageView imageView = new ImageView(getApplication());
                                                            //setting image resource
                                                            imageView.setImageResource(R.drawable.photo);
                                                            //setting image position
                                                            LinearLayout.LayoutParams layoutParamsimg = new LinearLayout.LayoutParams(
                                                                    (int)getResources().getDimension(R.dimen.twenty_six), LinearLayout.LayoutParams.MATCH_PARENT);
                                                            layoutParamsimg.weight=1;
                                                            layoutParamsimg.setMargins(0, 0, 10, 00);
                                                            imageView.setLayoutParams(layoutParamsimg);
                                                            //adding view to layout
                                                            l1.addView(imageView);
                                                            LinearLayout l2 = new LinearLayout(getApplication());
                                                            l2.setOrientation(LinearLayout.VERTICAL);
                                                            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                                                                    (int)getResources().getDimension(R.dimen.two_hundred_twenty_three), LinearLayout.LayoutParams.MATCH_PARENT);
                                                            layoutParams2.weight=1;
                                                            //l2.setLayoutParams(layoutParams2);
                                                            //        layoutParams2.setMargins(0, 0, 0, 10);
                                                            LinearLayout.LayoutParams layoutParamsText1 = new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                            layoutParamsText1.setMargins(0, 0, 150, 0);
                                                            layoutParamsText1.gravity= Gravity.CENTER_VERTICAL;
                                                            layoutParamsText1.weight=1;
                                                            TextView t1=new TextView(getApplication());
                                                            t1.setText(documentt.getId().toString());
                                                            t1.setTextSize(18);
                                                            //t1.setLayoutParams(layoutParamsText1);
                                                            LinearLayout.LayoutParams layoutParamsText2 = new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                            layoutParamsText2.weight=1;
                                                            TextView t2=new TextView(getApplication());
                                                            t2.setText(newestcontent);
                                                            t2.setTextSize(13);
                                                            //t2.setLayoutParams(layoutParamsText2);
                                                            l2.addView(t1,layoutParamsText1);
                                                            l2.addView(t2,layoutParamsText2);
                                                            Button b=new Button(getApplication());
                                                            LinearLayout.LayoutParams layoutParamsbutton = new LinearLayout.LayoutParams(
                                                                    (int)getResources().getDimension(R.dimen.seventeen), LinearLayout.LayoutParams.MATCH_PARENT);
                                                            layoutParamsbutton.gravity= Gravity.RIGHT;
                                                            layoutParamsbutton.weight=1;
                                                            b.setBackgroundResource(R.drawable.chat);
                                                            b.setText("5");
                                                            b.setTextColor(Color.parseColor("#FFFFFF"));
                                                            b.setTextSize(24);
                                                            b.setTag(activityname);
                                                            b.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    Intent intent = new Intent();
                                                                    intent.setClass(chatroom.this, chatting.class);
                                                                    intent.putExtra("activityname", activityname);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                            //b.setLayoutParams(layoutParamsbutton);
                                                            l1.addView(l2,layoutParams2);
                                                            l1.addView(b,layoutParamsbutton);
                                                            linear.addView(l1,layoutParams1);
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

    private void initViews()
    {
        chatwith2=(Button)findViewById(R.id.btn_chattingwith2);
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

    }

    private void initData()
    {
    }

    private void setListeners()
    {
        chatwith2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, chatting.class);
                startActivity(intent);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, informationedit.class);
                startActivity(intent);
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, homepage.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, MainActivity.class);
                startActivity(intent);
            }
        });

        activitypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, allactivity.class);
                startActivity(intent);
            }
        });

        selfpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, selfpage.class);
                startActivity(intent);

            }
        });

        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, chatroom.class);
                startActivity(intent);
            }
        });

        friendpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, friend.class);
                startActivity(intent);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, favorite.class);
                startActivity(intent);
            }
        });



        jo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, jo.class);
                startActivity(intent);
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, notice.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(chatroom.this, setting.class);
                startActivity(intent);

            }
        });

    }
}

