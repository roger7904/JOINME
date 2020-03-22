package com.roger.joinme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class informationedit extends AppCompatActivity {

    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informationedit);

        initViews();
        setListeners();
    }

    private void initViews()
    {
        send=(Button)findViewById(R.id.btn_send);
    }


    private void setListeners() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(informationedit.this, homepage.class);
                startActivity(intent);
            }
        });
    }
}
