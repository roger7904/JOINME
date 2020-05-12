package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {

    private Button back_to_login;
    private EditText editText_email;
    private EditText editText_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setListeners();


//        memberaccount.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("TAG", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("TAG", "Failed to read value.", error.toException());
//            }
//        });
    }

    private void initViews()
    {
        editText_pwd = (EditText)findViewById(R.id.editText_pwd);
        editText_email = (EditText)findViewById(R.id.editText_email);
        back_to_login=(Button)findViewById(R.id.btn_back_to_login);
    }

    private void initData()
    {
    }

    private void setListeners()
    {
        String email = editText_email.getText().toString();
        String passwd = editText_pwd.getText().toString();
        Log.d(email,"1");

//        //連結資料庫
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        //欄位名
//        DatabaseReference memberemail = database.getReference("member/"+email+"/emailinfo");
//        //值
//        memberemail.setValue(email);
//
//        DatabaseReference memberpasswd = database.getReference("member/"+email+"/passwd");
//        memberpasswd.setValue(passwd);

        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.this.finish();
            }
        });
    }
}
