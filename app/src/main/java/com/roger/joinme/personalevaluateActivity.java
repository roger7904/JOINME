package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


public class personalevaluateActivity extends AppCompatActivity
{
    private Button one,two,three,four,five,send;
    private TextView textName;
    private EditText evaluatecontent;
    private ImageView userProfileImage;
    private Integer star;

    private String currentUserID,UserID,activityname;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private StorageReference UserProfileImagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_personal);

        UserID = getIntent().getExtras().get("visit_user_id").toString();
        star = (Integer) getIntent().getExtras().get("star");
        activityname = getIntent().getExtras().get("activityname").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        InitializeFields();
        setListeners();
        RetrieveUserInfo();
        if(star.equals(1)){
            one.setBackgroundResource(R.drawable.star);
            two.setBackgroundResource(R.drawable.star2);
            three.setBackgroundResource(R.drawable.star2);
            four.setBackgroundResource(R.drawable.star2);
            five.setBackgroundResource(R.drawable.star2);
        }else if(star.equals(2)){
            one.setBackgroundResource(R.drawable.star);
            two.setBackgroundResource(R.drawable.star);
            three.setBackgroundResource(R.drawable.star2);
            four.setBackgroundResource(R.drawable.star2);
            five.setBackgroundResource(R.drawable.star2);
        }else if(star.equals(3)){
            one.setBackgroundResource(R.drawable.star);
            two.setBackgroundResource(R.drawable.star);
            three.setBackgroundResource(R.drawable.star);
            four.setBackgroundResource(R.drawable.star2);
            five.setBackgroundResource(R.drawable.star2);
        }else if(star.equals(4)){
            one.setBackgroundResource(R.drawable.star);
            two.setBackgroundResource(R.drawable.star);
            three.setBackgroundResource(R.drawable.star);
            four.setBackgroundResource(R.drawable.star);
            five.setBackgroundResource(R.drawable.star2);
        }else if(star.equals(5)){
            one.setBackgroundResource(R.drawable.star);
            two.setBackgroundResource(R.drawable.star);
            three.setBackgroundResource(R.drawable.star);
            four.setBackgroundResource(R.drawable.star);
            five.setBackgroundResource(R.drawable.star);
        }
    }

    private void InitializeFields()
    {
        one=(Button) findViewById(R.id.one);
        two=(Button) findViewById(R.id.one);
        three=(Button) findViewById(R.id.three);
        four=(Button) findViewById(R.id.four);
        five=(Button) findViewById(R.id.five);
        send=(Button) findViewById(R.id.send_evaluate);

        evaluatecontent = (EditText) findViewById(R.id.editText_evaluate_content);
        userProfileImage = (ImageView) findViewById(R.id.users_profile_image);

        textName = (TextView) findViewById(R.id.user_name);

    }

    private void setListeners() {
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundResource(R.drawable.star);
                two.setBackgroundResource(R.drawable.star2);
                three.setBackgroundResource(R.drawable.star2);
                four.setBackgroundResource(R.drawable.star2);
                five.setBackgroundResource(R.drawable.star2);
                star=1;
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundResource(R.drawable.star);
                two.setBackgroundResource(R.drawable.star);
                three.setBackgroundResource(R.drawable.star2);
                four.setBackgroundResource(R.drawable.star2);
                five.setBackgroundResource(R.drawable.star2);
                star=2;
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundResource(R.drawable.star);
                two.setBackgroundResource(R.drawable.star);
                three.setBackgroundResource(R.drawable.star);
                four.setBackgroundResource(R.drawable.star2);
                five.setBackgroundResource(R.drawable.star2);
                star=3;
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundResource(R.drawable.star);
                two.setBackgroundResource(R.drawable.star);
                three.setBackgroundResource(R.drawable.star);
                four.setBackgroundResource(R.drawable.star);
                five.setBackgroundResource(R.drawable.star2);
                star=4;
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundResource(R.drawable.star);
                two.setBackgroundResource(R.drawable.star);
                three.setBackgroundResource(R.drawable.star);
                four.setBackgroundResource(R.drawable.star);
                five.setBackgroundResource(R.drawable.star);
                star=5;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> evaluatemap = new HashMap<>();
                evaluatemap.put("evaluate", true);
                evaluatemap.put("evaluate_from", currentUserID);
                evaluatemap.put("star", star);
                evaluatemap.put("evaluate_content", evaluatecontent.getText().toString());
                db.collection("activity").document(activityname)
                    .collection("participant")
                    .document(UserID)
                    .set(evaluatemap, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> evaluatemap2 = new HashMap<>();
                                evaluatemap2.put("activityname", activityname);
                                evaluatemap2.put("evaluate_from", currentUserID);
                                evaluatemap2.put("star", star);
                                evaluatemap2.put("evaluate_content", evaluatecontent.getText().toString());
                                db.collection("user").document(UserID)
                                        .collection("evaluate")
                                        .document()
                                        .set(evaluatemap2)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Intent Intent = new Intent(personalevaluateActivity.this, evaluateActivity.class);
                                                    startActivity(Intent);
                                                    Toast.makeText(personalevaluateActivity.this,
                                                            "評價完成", Toast.LENGTH_SHORT).show();
                                                    Intent settingsIntent = new Intent(personalevaluateActivity.this, home.class);
                                                    startActivity(settingsIntent);
                                                }
                                            }
                                        });
                            }
                        }
                    });

            }
        });
    }

    private void RetrieveUserInfo()
    {

        final DocumentReference docRef = db.collection("user").document(UserID).collection("profile")
                .document(UserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists() && snapshot.contains("name") && snapshot.contains("image")) {
                        String retrieveUserName = snapshot.getString("name");

                        textName.setText(retrieveUserName);

                        UserProfileImagesRef.child(UserID+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(personalevaluateActivity.this)
                                        .load(uri)
                                        .circleCrop()
                                        .into(userProfileImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        Log.d("TAG", "source" + " data: " + snapshot.getData());
                    } else if (snapshot != null && snapshot.exists() && snapshot.contains("name")) {
                        String retrieveUserName = snapshot.getString("name");

                        textName.setText(retrieveUserName);

                        Glide.with(personalevaluateActivity.this)
                                .load(R.drawable.head)
                                .circleCrop()
                                .into(userProfileImage);

                    }
                }
            }
        });
    }

    //鎖手機的返回鍵
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
//                event.startTracking();
//                Intent intent = new Intent();
//                intent.setClass(personalevaluateActivity.this, home.class);
//                startActivity(intent);
//            }else{
//                onBackPressed();
//            }
//        }
//        return false;
//    }

//    @Override
//    public  boolean onKeyUp(int keyCode, KeyEvent event){
//        return super.onKeyUp(keyCode, event);
//    }

//    private void SendUserToMainActivity()
//    {
//        Intent mainIntent = new Intent(testsetting.this, home.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//        finish();
//    }
}