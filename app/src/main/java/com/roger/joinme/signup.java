package com.roger.joinme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

public class signup extends AppCompatActivity {

    public Button signupbtn, deletebtn, favoritebtn;
    public TextView title;
    public ImageView activityPhoto;
    public TextView activityContent;

    public Bitmap actImg;
    private String activitytitle,organizerID,activityType,organizerName;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth mAuth;
    private String currentUserID,currentUserName;
    String imageUrl;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoxsFReA:APA91bFrtTvCQxgBDQMTB7MddpMquycE2wOqh4K4_-yHNC2KSxCW0exYbpzx62KmVMNfY8HoZz67HrSc_xbo9NeWPSB13LGBxmAJujI-n90hm3zYLKbZGkqgGo_GIrdFLvcKP77GE5yA";
    final private String contentType = "application/json";


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        activitytitle = getIntent().getExtras().get("activitytitle").toString();
        final DocumentReference docRef = db.collection("user").document(currentUserID).collection("profile")
                .document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {
                        currentUserName=snapshot.getString("name");
                    } else {

                    }
                }
            }
        });

        db.collection("activity").document(activitytitle).
                get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                activityType=document.getString("activityType");
                                Date snnippet = document.getTimestamp("startTime").toDate();
                                Date snnippet2 = document.getTimestamp("endTime").toDate();
                                Boolean haveImg=document.getBoolean("img");
                                SimpleDateFormat ft = new SimpleDateFormat(" yyyy-MM-dd HH :mm:ss ");
                                title.setText(activitytitle);
                                organizerID=document.getString("organizerID");
                                if(haveImg) {
                                    StorageReference img = firebaseStorage.getReference();
                                    img.child(activitytitle).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(signup.this).load(uri).into(activityPhoto);

                                        }
                                    });
                                }

                                final DocumentReference docRef = db.collection("user").document(organizerID).collection("profile")
                                        .document(organizerID);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snapshot = task.getResult();
                                            if (snapshot != null && snapshot.exists()) {
                                                organizerName=snapshot.getString("name");
                                            } else {

                                            }
                                        }
                                    }
                                });

                                activityContent.setText("開始時間：" + ft.format(snnippet) + "\n結束時間：" + ft.format(snnippet2) + "\n" + "地點：" + document.getString("location") + "\n" + "備註：" + document.getString("postContent") + "\n" + "發起人：" + organizerName);
                                if (!document.getString("organizerID").equals(currentUserID)) {
                                    deletebtn.setVisibility(View.GONE);
                                }else{
                                    signupbtn.setVisibility(View.GONE);
                                }
                            } else {
                            }
                        } else {
                        }
                    }
                });

        initViews();
        initData();
        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    private void initData() {
    }

    private void initViews() {
        signupbtn = (Button) findViewById(R.id.signupbtn);
        title = (TextView) findViewById(R.id.title);
        activityPhoto = (ImageView) findViewById(R.id.activityphoto);
        activityContent = (TextView) findViewById(R.id.activityContent);
        deletebtn = (Button) findViewById(R.id.deletebtn);
        favoritebtn = (Button) findViewById(R.id.favoritebtn);
    }

    private void setListeners() {
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder warning = new AlertDialog.Builder(signup.this);
                warning.setTitle("警告");
                warning.setTitle("確認要刪除此活動？");
                warning.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        db.collection("activity").document(activitytitle).delete();
                        db.collection("activity").document(activitytitle).collection("participant").document().delete();
                        final DocumentReference docRef = db.collection("chat").document(activitytitle);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot snapshot = task.getResult();
                                    if (snapshot != null && snapshot.exists()) {
                                        db.collection("chat").document(activitytitle).delete();
                                        db.collection("chat").document(activitytitle).collection("participant").document().delete();
                                        db.collection("chat").document(activitytitle).collection("content").document().delete();
                                    } else {

                                    }
                                }
                            }
                        });
                        db.collection("join_act_request").document(activitytitle)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot snapshot = task.getResult();
                                    if (snapshot != null && snapshot.exists()) {
                                        db.collection("join_act_request").document(activitytitle).delete();
                                        db.collection("join_act_request").document(activitytitle).collection("UserID").document().delete();
                                    } else {

                                    }
                                }
                            }
                        });
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }); //刪除圖片
                        Intent intent = new Intent(signup.this, home.class);
                        startActivity(intent);
                    }

                });
                warning.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                    }
                });
                warning.show();
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Map<String, Object> actbook = new HashMap<>();
//                actbook.put("UserID",currentUserID);
//                final FirebaseFirestore db = FirebaseFirestore.getInstance();
//                db.collection("activity")
//                        .document(activitytitle)
//                        .collection("participant")
//                        .document(currentUserID)
//                        .set(actbook)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d("TAG", "DocumentSnapshot successfully written!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w("TAG", "Error writing document", e);
//                            }
//                        });
                HashMap<String, String> join = new HashMap<>();
                join.put("UserID", currentUserID);
                db.collection("join_act_request").document(activitytitle)
                        .collection("UserID")
                        .document(currentUserID)
                        .set(join)
                        .addOnCompleteListener(new OnCompleteListener() {
                               @Override
                               public void onComplete(@NonNull Task task) {
                                   if (task.isSuccessful()) {

                                   }
                               }
                           });
                String saveCurrentTime, saveCurrentDate;

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                saveCurrentTime = currentTime.format(calendar.getTime());

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                HashMap<String, String> chatNotificationMap = new HashMap<>();
                chatNotificationMap.put("from", currentUserID);
                chatNotificationMap.put("type", "joinact");
                chatNotificationMap.put("time", saveCurrentTime);
                chatNotificationMap.put("date", saveCurrentDate);
                chatNotificationMap.put("millisecond", ts);

                db.collection("user").document(organizerID).
                        collection("notification").
                        document().
                        set(chatNotificationMap)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });
                db.collection("user").document(organizerID).
                        get().
                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String RECEIVER_DEVICE = document.getString("device_token");
                                        JSONObject notification = new JSONObject();
                                        JSONObject notifcationBody = new JSONObject();
                                        try {
                                            notifcationBody.put("title", "您有新的入團申請");
                                            notifcationBody.put("message", currentUserName+"對"+activitytitle+"提出了入團申請");
                                            notification.put("to", RECEIVER_DEVICE);
                                            notification.put("data", notifcationBody);
                                        } catch (JSONException e) {
                                        }
                                        sendNotification(notification);
                                    } else {
                                    }
                                } else {
                                }
                            }
                        });
                signupbtn.setText("已申請報名");
                signupbtn.setEnabled(false);
            }
        });

        favoritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> favoritemap = new HashMap<>();
                favoritemap.put("activity", activitytitle);
                db.collection("user")
                        .document(currentUserID)
                        .collection("favorite")
                        .document(activitytitle)
                        .set(favoritemap)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });

                favoritebtn.setText("已加入收藏");
                favoritebtn.setEnabled(false);
            }
        });
    }


    public class getBitmapFromUrl extends Thread {
        public void run() {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                actImg = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(signup.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }



}
