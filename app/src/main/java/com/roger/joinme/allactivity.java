package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class allactivity extends AppCompatActivity {
    private Button nearactbtn;
    private Button holdonactbtn;
    private Button joinactbtn;
    private Button allactbtn;
    private Button signupbtn;
    private Button signupbtn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allactivity);

        initViews();
        initData();
        setListeners();
    }
    private void initData()
    {
    }
    private void initViews() {
        nearactbtn=(Button)findViewById(R.id.nearactbtn);
        holdonactbtn=(Button)findViewById(R.id.holdonbtn);
        joinactbtn=(Button)findViewById(R.id.joinbtn);
        allactbtn=(Button)findViewById(R.id.activitybtn);
        signupbtn=(Button)findViewById(R.id.gogo);
        signupbtn2=(Button)findViewById(R.id.gotosignup);
    }

    private void setListeners()
    {
        nearactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(allactivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(allactivity.this,signup.class);
                startActivity(intent);
            }
        });
        signupbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(allactivity.this,signup.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.activitybtn:
                setContentView(R.layout.activity_allactivity);
                break;
            case R.id.holdonbtn:
                setContentView(R.layout.activity_holdonact);
                break;
            case R.id.joinbtn:
                setContentView(R.layout.activity_joinact);
                break;
        }
    }
}
