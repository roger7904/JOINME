package com.roger.joinme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class chatroom extends AppCompatActivity {
    private Button chatwith2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        initViews();
        setListeners();
    }
    private void initViews()
    {
        chatwith2=(Button)findViewById(R.id.btn_chattingwith2);

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
    }
}

