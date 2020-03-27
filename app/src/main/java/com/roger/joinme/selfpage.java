package com.roger.joinme;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class selfpage extends AppCompatActivity {

    private Button gotoactivity;
    private Button gotofriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfpage);

        initViews();
        setListeners();
    }

    private void initViews()
    {
        gotoactivity=(Button)findViewById(R.id.gotoactivity);
        gotofriend=(Button)findViewById(R.id.gotofriend);
    }

    private void initData()
    {
    }

    private void setListeners()
    {
        gotoactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(selfpage.this, allactivity.class);
                startActivity(intent);
            }
        });

//        gotofriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(selfpage.this, friend.class);
//                startActivity(intent);
//            }
//        });
    }
}
