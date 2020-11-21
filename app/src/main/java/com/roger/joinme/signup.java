package com.roger.joinme;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class signup extends AppCompatActivity {

    public Button signupbtn, deletebtn, favoritebtn;
    public TextView title;
    public ImageView activityPhoto;
    public TextView activityContent,activityStartTime,activityEndTime,activityPlace,activityPost,activityOrgName,actRestriction;
    public static double camera_Position_Lat;
    public static double camera_Position_Lng;

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
    private StorageReference UserActImageRef;
    private LocationManager locationManager;
    public String commadStr;
    public double userLat,userLog;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        commadStr = LocationManager.GPS_PROVIDER;

        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        activitytitle = getIntent().getExtras().get("activitytitle").toString();
        UserActImageRef = FirebaseStorage.getInstance().getReference();
        initViews();
        initData();
        setListeners();
        getUserLocation();

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

        final DocumentReference docRef2 = db.collection("user").document(currentUserID).collection("favorite")
                .document(activitytitle);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {
                        favoritebtn.setBackground(getResources().getDrawable(R.drawable.heart));
                    } else {

                    }
                }
            }
        });

        db.collection("activity").document(activitytitle)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                activityType = document.getString("activityType");
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
                                            Glide.with(signup.this).load(uri).into(activityPhoto);  //主辦活動
                                        }
                                    });
                                }else{
                                    String activityType = document.getString("activityType");
                                    if(activityType.equals("運動")){
                                        activityType = "球類";
                                    }
                                    UserActImageRef.child(activityType + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(signup.this)
                                                    .load(uri)
                                                    .into(activityPhoto);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
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
                                                String userID = snapshot.getString("currentUserID");
                                                activityContent.setText("類別：" + document.getString("activityType"));
                                                activityStartTime.setText("開始時間：" + ft.format(snnippet));
                                                activityEndTime.setText("結束時間：" + ft.format(snnippet2));
                                                activityPlace.setText(document.getString("location"));
                                                activityPost.setText("備註：" + document.getString("postContent"));
                                                activityOrgName.setText(organizerName);
                                                if(document.getBoolean("notification")){
                                                    String noEatingout = "";
                                                    String noSmoking = "";
                                                    String noWine = "";
                                                    String onlyFemale = "";
                                                    String onlyMale = "";
                                                    if(document.getBoolean("noEatingOut")){
                                                        noEatingout = "禁帶外食";
                                                    }
                                                    if(document.getBoolean("noSmoking")){
                                                        noSmoking = "禁菸";
                                                    }
                                                    if(document.getBoolean("noWine")){
                                                        noWine = "禁酒";
                                                    }
                                                    if(document.getBoolean("onlyFemale")){
                                                        onlyFemale = "限男";
                                                    }
                                                    if(document.getBoolean("onlyMale")){
                                                        onlyMale = "限女";
                                                    }
                                                    actRestriction.setText("活動限制：" + noEatingout + noSmoking + noWine + onlyFemale + onlyMale);
                                                }
                                                activityPlace.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                                Uri.parse("http://maps.google.com/maps?daddr="
                                                                        + document.getGeoPoint("geopoint").getLatitude()+ "," + document.getGeoPoint("geopoint").getLongitude()
                                                                        +"&language=zh-tw")
                                                        );

                                                        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                                                        startActivity(intent);
                                                    }
                                                });


                                                activityOrgName.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent myIntent = new Intent();
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("visit_user_id", userID);
                                                        myIntent.putExtras(bundle);
                                                        myIntent.setClass(signup.this, personalpage.class);
                                                        startActivity(myIntent);
                                                    }
                                                });
                                            } else {

                                            }
                                        }
                                    }
                                });

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
        db.collection("join_act_request").document(activitytitle).collection("UserID").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        signupbtn.setText("您已申請該活動");
                        signupbtn.setEnabled(false);
                    }
                }
            }
        });

        db.collection("activity").document(activitytitle).collection("participant").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        signupbtn.setText("您已加入該活動");
                        signupbtn.setEnabled(false);
                    }
                }
            }
        });
    }

    public void getUserLocation(){

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(signup.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(signup.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(signup.this, new String[]{ACCESS_FINE_LOCATION}, Integer.parseInt(ACCESS_COARSE_LOCATION));
            return;
        }
        locationManager.requestLocationUpdates(commadStr,1000,0,locationListener);
    }

    public LocationListener locationListener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location){
            userLat = location.getLatitude();
            userLog = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}
    };


    @Override
    protected void onStart() {
        super.onStart();
        getDBlistener();
    }

    private static double rad(double d) {
        return d * Math.PI / 180.00; //角度轉換成弧度
    }
    /*
     * 根據經緯度計算兩點之間的距離（單位米）
     * */
    public static String algorithm(double longitude1, double latitude1, double longitude2, double latitude2) {

        double Lat1 = rad(latitude1); // 緯度
        double Lat2 = rad(latitude2);

        double a = Lat1 - Lat2;//兩點緯度之差
        double b = rad(longitude1) - rad(longitude2); //經度之差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//計算兩點距離的公式
        s = s * 6378137.0;//弧長乘地球半徑（半徑為米）
        s = Math.round(s * 10000d) / 10000d;//精確距離的數值

        //四捨五入 保留一位小數
        DecimalFormat df = new DecimalFormat("#.0");

        return df.format(s);

    }

    public void getDBlistener() {
        System.out.println("Lat:" + userLat + "\nLog:" + userLog);
        Date curDate = new Date(System.currentTimeMillis());
        db.collection("activity").document(activitytitle).collection("participant").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        db.collection("activity").document(activitytitle)
                                .collection("participant")
                                .document(currentUserID).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                final DocumentReference docRef = db.collection("activity").document(activitytitle);
                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                       @Override
                                                                                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                           if (task.isSuccessful()) {
                                                                                               DocumentSnapshot snapshot = task.getResult();
                                                                                               double activityLog = snapshot.getGeoPoint("geopoint").getLongitude();
                                                                                               double activityLat = snapshot.getGeoPoint("geopoint").getLatitude();
                                                                                               double distance = Double.parseDouble(algorithm(activityLog, activityLat, userLog, userLat));
                                                                                               if (snapshot.getTimestamp("startTime").toDate().getTime() <= curDate.getTime() && curDate.getTime() <= snapshot.getTimestamp("endTime").toDate().getTime() && distance <= 100) {
                                                                                                    signupbtn.setEnabled(true);
                                                                                                    signupbtn.setText("報到");
                                                                                               }else if(snapshot.getTimestamp("startTime").toDate().getTime() <= curDate.getTime() && curDate.getTime() <= snapshot.getTimestamp("endTime").toDate().getTime() && distance > 100){
                                                                                                    signupbtn.setEnabled(false);
                                                                                                    signupbtn.setText("報到");
                                                                                               }else if(curDate.getTime() > snapshot.getTimestamp("endTime").toDate().getTime()){
                                                                                                   db.collection("activity").document(activitytitle).collection("participant")
                                                                                                            .get()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                                            if(!document.contains("checkIn")){
                                                                                                                                HashMap<String, Object> check = new HashMap<>();
                                                                                                                                check.put("checkIn", false);
                                                                                                                                db.collection("activity").document(activitytitle)
                                                                                                                                        .collection("participant")
                                                                                                                                        .document(currentUserID)
                                                                                                                                        .set(check, SetOptions.merge())
                                                                                                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onComplete(@NonNull Task task) {
                                                                                                                                                if (task.isSuccessful()) {

                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        });
                                                                                                                            }else{
                                                                                                                                signupbtn.setEnabled(false);
                                                                                                                                signupbtn.setText("報到成功");
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                               }
                                                                                           }
                                                                                       }
                                                                                   });

                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        });

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
        activityStartTime = (TextView) findViewById(R.id.activityStartTime);
        activityEndTime = (TextView) findViewById(R.id.activityEndTime);
        activityPlace = (TextView) findViewById(R.id.activityPlace);
        activityPost = (TextView) findViewById(R.id.activityPost);
        activityOrgName = (TextView) findViewById(R.id.activityOrgName);
        actRestriction = (TextView) findViewById(R.id.actRestriction);
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
                                        db.collection("user").document(currentUserID).collection("activity").document(activitytitle).delete();
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
                if(signupbtn.getText().equals("報名")){
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
                    Intent settingsIntent = new Intent(signup.this, home.class);
                    startActivity(settingsIntent);
                }else if(signupbtn.getText().equals("報到")){
                    HashMap<String, Object> check = new HashMap<>();
                    check.put("checkIn", true);
                    db.collection("activity").document(activitytitle)
                            .collection("participant")
                            .document(currentUserID)
                            .set(check, SetOptions.merge())
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                    signupbtn.setText("報到成功");
                    signupbtn.setEnabled(false);
                }
            }
        });

        favoritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritebtn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.heart).getConstantState()){
                    favoritebtn.setBackground(getResources().getDrawable(R.drawable.nojoin));
                    db.collection("user")
                            .document(currentUserID)
                            .collection("favorite")
                            .document(activitytitle)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else{
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
                    favoritebtn.setBackground(getResources().getDrawable(R.drawable.heart));
                }
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

    //鎖手機的返回鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
                event.startTracking();
                Intent intent = new Intent();
                intent.setClass(signup.this, home.class);
                startActivity(intent);
            }else{
                onBackPressed();
            }
        }
        return false;
    }

    @Override
    public  boolean onKeyUp(int keyCode, KeyEvent event){
        return super.onKeyUp(keyCode, event);
    }
}
