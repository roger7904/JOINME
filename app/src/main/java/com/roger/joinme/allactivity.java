package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class allactivity extends AppCompatActivity {
    private Button nearactbtn;

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
    }

    private void setListeners()
    {
        nearactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(allactivity.this,nearact.class);
                startActivity(intent);
            }
        });
    }
}
