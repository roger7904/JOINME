package com.roger.joinme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpwd extends AppCompatActivity {

    private Button back_to_login;
    private EditText emailtext;
    private Button btnsend;

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
        emailtext= (EditText)findViewById(R.id.editText_account);
        btnsend=(Button)findViewById(R.id.button_send);
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

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailtext.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "請至信箱修改密碼...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

