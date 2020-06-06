package com.roger.joinme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;

public class jo extends AppCompatActivity {
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
    private Spinner spinner;
    private Button startBtn, endBtn, dateBtn;
    private int year, month, day; //選擇日期變數
    private int sHour,sMin,eHour,eMin;  //起訖時間變數


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createact);
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
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{"商家優惠", "球類", "限時", "KTV", "其他"});
        setListeners();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    private void initViews() {
        user = (Button) findViewById(R.id.btn_user);
        homepage = (Button) findViewById(R.id.btn_to_homepage);
        selfpage = (Button) findViewById(R.id.btn_to_selfpage);
        activitypage = (Button) findViewById(R.id.btn_to_jo);
        friendpage = (Button) findViewById(R.id.btn_to_notice);
        logout = (Button) findViewById(R.id.btn_logout);
        chatroom = (ImageButton) findViewById(R.id.imgbtn_chatroom);
        favorite = (ImageButton) findViewById(R.id.imgbtn_favorite);
        jo = (ImageButton) findViewById(R.id.imgbtn_jo);
        notice = (ImageButton) findViewById(R.id.imgbtn_notice);
        setting = (ImageButton) findViewById(R.id.imgbtn_setting);
        spinner = (Spinner) findViewById(R.id.activityType);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        endBtn = (Button) findViewById(R.id.endBtn);
        startBtn = (Button) findViewById(R.id.startBtn);
    }

    private void initData() {
    }

    private void setListeners() {
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, informationedit.class);
                startActivity(intent);
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, homepage.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, MainActivity.class);
                startActivity(intent);
            }
        });

        activitypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, allactivity.class);
                startActivity(intent);
            }
        });

        selfpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, selfpage.class);
                startActivity(intent);
            }
        });

//        chatroom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, chatroom.class);
//                startActivity(intent);
//            }
//        });

//        friendpage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, friend.class);
//                startActivity(intent);
//            }
//        });

//        favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, favorite.class);
//                startActivity(intent);
//            }
//        });


//        jo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, jo.class);
//                startActivity(intent);
//            }
//        });

//        notice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, notice.class);
//                startActivity(intent);
//            }
//        });
//
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, setting.class);
//                startActivity(intent);
//            }
//        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(jo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        dateBtn.setText(setDateFormat(year, month, day));
                    }

                }, year, month, day).show();
            }

        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                sHour = c.get(Calendar.HOUR_OF_DAY);
                sMin = c.get(Calendar.MINUTE);
                new TimePickerDialog(jo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Min) {
                        startBtn.setText(setTimeFormat(Hour,Min));
                        sHour=Hour;
                        sMin=Min;
//                        System.out.println(Hour+":"+Min);
                    }
                }, sHour,sMin,false).show();

            }

        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                eHour = c.get(Calendar.HOUR_OF_DAY);
                eMin = c.get(Calendar.MINUTE);
                new TimePickerDialog(jo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Min) {
                        endBtn.setText(setTimeFormat(Hour,Min));
                        eHour=Hour;
                        eMin=Min;
                    }
                }, eHour,eMin,false).show();
            }

        });
    }

    private String setDateFormat(int year, int monthOfYear, int dayOfMonth) {
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }

    private String setTimeFormat(int hr,int min) {
        return String.valueOf(hr)+":"+String.valueOf(min);
    }
}




