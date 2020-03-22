package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button register;
    private Button forgetpw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控制項
        initViews();
        //初始化資料
        initData();
        //設定監聽事件
        setListeners();
    }

    private void initViews()
    {
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
        forgetpw=(Button)findViewById(R.id.forgetpassword);
    }

    private void initData()
    {
    }

    private void setListeners()
    {
        Button login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,homepage.class);
                startActivity(intent);
            }
        });
    }
}
