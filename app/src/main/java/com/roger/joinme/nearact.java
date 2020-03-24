package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class nearact extends AppCompatActivity {
    private Button actbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearact);
        initViews();
        setListeners();
    }

    private void initViews()
    {
        actbtn=(Button)findViewById(R.id.activitybtn);
    }

    private void setListeners()
    {
        actbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(nearact.this,allactivity.class);
                startActivity(intent);
            }
        });
    }

}
