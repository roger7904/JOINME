package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class forgetpwd extends AppCompatActivity {

    private Button back_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);

        initViews();
        setListeners();
    }
    private void initViews()
    {
        back_to_login=(Button)findViewById(R.id.btn_back_to_login);
    }

    private void initData()
    {
    }

    private void setListeners()
    {
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetpwd.this.finish();
            }
        });
    }
}

