package com.roger.joinme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class organizerevaluateActivity extends AppCompatActivity
{
    private Button one,two,three,four,five,send;
    private TextView textName;
    private EditText evaluatecontent;
    private ImageView userProfileImage;
    private Integer star;
    private String currentUserID,activityname,UserID;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private StorageReference UserProfileImagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_organizer);


        activityname = getIntent().getExtras().get("activityname").toString();
        UserID = getIntent().getExtras().get("UserID").toString();


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        star=5;

        InitializeFields();
        setListeners();
        RetrieveUserInfo();

    }

    private void InitializeFields()
    {
        one=(Button) findViewById(R.id.one);
        two=(Button) findViewById(R.id.two);
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
                evaluatemap.put("evaluate_to_organizer", true);

                db.collection("activity").document(activityname)
                        .collection("participant")
                        .document(currentUserID)
                        .set(evaluatemap,SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
//                Map<String, Object> evaluatemap = new HashMap<>();
//                evaluatemap.put("evaluate", true);
//                evaluatemap.put("evaluate_from", currentUserID);
//                evaluatemap.put("star", star);
//                evaluatemap.put("evaluate_content", evaluatecontent.getText().toString());
//                db.collection("activity").document(activityname)
//                        .collection("participant")
//                        .document(UserID)
//                        .set(evaluatemap, SetOptions.merge())
//                        .addOnCompleteListener(new OnCompleteListener() {
//                            @Override
//                            public void onComplete(@NonNull Task task) {
//                                if (task.isSuccessful()) {
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
                                                        Intent Intent = new Intent(organizerevaluateActivity.this, evaluateActivity.class);
                                                        startActivity(Intent);
                                                        Toast.makeText(organizerevaluateActivity.this,
                                                                "評價完成", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
//                                }
//                            }
//                        });



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
                                Glide.with(organizerevaluateActivity.this)
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

                        Glide.with(organizerevaluateActivity.this)
                                .load(R.drawable.head)
                                .circleCrop()
                                .into(userProfileImage);

                    }
                }
            }
        });
    }

//    private void SendUserToMainActivity()
//    {
//        Intent mainIntent = new Intent(testsetting.this, home.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//        finish();
//    }
}