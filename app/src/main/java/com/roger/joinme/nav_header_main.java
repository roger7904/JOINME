package com.roger.joinme;

import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

public class nav_header_main extends AppCompatActivity {
    private Button user;
    private Button homepage;
    private Button selfpage;
    private Button activitypage;
    private Button friendpage;
    private Button logout;

    //test
    private AppBarConfiguration mAppBarConfiguration;


//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.nav_header_main);
//            initViews();
//            setListeners();
//    }

    public void navheader(){
        initViews();
        setListeners();
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
        activitypage=(Button)findViewById(R.id.btn_to_activitypage);
        friendpage=(Button)findViewById(R.id.btn_to_notice);
        logout=(Button)findViewById(R.id.btn_logout);


    }

    private void initData()
    {
    }

    private void setListeners() {
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(nav_header_main.this, informationedit.class);
                startActivity(intent);
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(nav_header_main.this, homepage.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(nav_header_main.this, MainActivity.class);
                startActivity(intent);
            }
        });

        activitypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(nav_header_main.this, allactivity.class);
                startActivity(intent);
            }
        });

        selfpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(nav_header_main.this, selfpage.class);
                startActivity(intent);
            }
        });



        friendpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(nav_header_main.this, friend.class);
                startActivity(intent);
            }
        });



    }

}
