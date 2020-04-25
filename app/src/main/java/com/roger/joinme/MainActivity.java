package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends FragmentActivity {

    private Button login;
    private Button register;
    private Button forgetpwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setListeners();

        //資料庫連結
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //將欄位寫入資料庫
        DatabaseReference myref = database.getReference("member/No1/accountname");
        //欄位值
        myref.setValue("tzuyen");
        DatabaseReference myref2 = database.getReference("member/No1/email");
        //欄位值
        myref2.setValue("123");
        DatabaseReference myref3 = database.getReference("member/No1/passwd");
        //欄位值
        myref3.setValue("123");

        //讀資料庫資料
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        //pushtest
    }

    private void initViews()
    {
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
        forgetpwd=(Button)findViewById(R.id.forgetpassword);
    }

    private void initData()
    {
    }

    private void setListeners()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,register.class);
                startActivity(intent);
            }
        });
        forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,forgetpwd.class);
                startActivity(intent);
            }
        });

    }
}
