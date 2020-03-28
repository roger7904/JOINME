package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class holdonact extends AppCompatActivity {

    private Button nearactbtn;
    private Button holdonactbtn;
    private Button joinactbtn;
    private Button allactbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holdonact);

        initViews();
        initData();
        setListeners();

    }

    private void setListeners() {
        allactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(holdonact.this,allactivity.class);
                startActivity(intent);
            }
        });

        joinactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(holdonact.this,joinact.class);
                startActivity(intent);
            }
        });

        holdonactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(holdonact.this,holdonact.class);
                startActivity(intent);
            }
        });

        nearactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(holdonact.this,MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
    }

    private void initViews() {
        holdonactbtn=(Button)findViewById(R.id.holdonbtn);
        joinactbtn=(Button)findViewById(R.id.joinbtn);
        allactbtn=(Button)findViewById(R.id.activitybtn);
        nearactbtn=(Button)findViewById(R.id.nearactbtn);
    }

}
