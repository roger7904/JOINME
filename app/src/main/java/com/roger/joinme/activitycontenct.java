package com.roger.joinme;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activitycontenct extends AppCompatActivity {
    private Button actbtn;
    private Button joinbtn;
    private Button holdonbtn;
    private Button nearactbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informationedit);

        initViews();
        setListeners();
    }

    private void setListeners() {
        actbtn=(Button)findViewById(R.id.activitybtn);
        joinbtn=(Button)findViewById(R.id.joinbtn);
        holdonbtn=(Button)findViewById(R.id.holdonbtn);
        nearactbtn=(Button)findViewById(R.id.holdonbtn);
    }

    private void initViews() {
    }
}
