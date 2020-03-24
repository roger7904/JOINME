package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class signup extends AppCompatActivity{
    private Button signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        initData();
        setListeners();
    }

    private void initData()
    {
    }

    private void initViews() {
        signupbtn=(Button)findViewById(R.id.signin);
    }

    private void setListeners()
    {

    }
}
