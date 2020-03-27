package com.roger.joinme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class notice extends AppCompatActivity {
    private Button btn[]=new Button[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initViews();
        setListeners();
    }
    private void initViews()
    {
        btn[0]=(Button)findViewById(R.id.btn_toverify);
        btn[1]=(Button)findViewById(R.id.btn_toverify2);
        btn[2]=(Button)findViewById(R.id.btn_toverify3);
        btn[3]=(Button)findViewById(R.id.btn_toverify4);

    }

    private void initData()
    {
    }

    private void setListeners()
    {
        for(int i=0;i<=3;i++) {
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(notice.this, verify.class);
                    startActivity(intent);
                }
            });
        }
    }
}

