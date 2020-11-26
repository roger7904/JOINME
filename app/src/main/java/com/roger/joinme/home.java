package com.roger.joinme;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import org.w3c.dom.Text;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class home extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener {
    private Button user;
    private Button homepage;
    private Button selfpage;
    private Button activitypage;
    private Button friendpage;
    private Button logout;
    private ImageButton chatroom;
    private ImageButton favorite;
    private ImageButton jo;
    private ImageButton notice;
    private ImageButton setting;
    private Button ball;
    public GoogleMap mMap;
    private Location mLastKnownLocation;
    private Boolean mLocationPermissionGranted = false;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mDefaultLocation;
    private Button ballbtn;
    private Button storebtn;
    private Button ktvbtn;
    private Button informationbtn;
    private Button homebtn;
    private Button jobtn;
    private Button messagebtn;
    private Button favoritebtn;
    private TextView noticebtn;
    private Button findFriendBtn,settingbtn,inviteFriendBtn,actInviteBtn,logoutBtn;
    private Button setProfileBtn;
    private Button refreshbtn;
    private Button otherbtn;
    private static int count = 0;
    private LatLng[] locate = new LatLng[10000];
    public double userlat;
    public double userlnt;
    public double distanceresult;
    public Marker marker;
    public Marker marker1;
    double lat;
    double lng;
    public Bitmap bitmap;
    private ClusterManager<MyItem> mClusterManager;
    public MyItem offsetItem;
    public BitmapDescriptor markerDescriptor;
    public int maplistener = 0;
    //    public static String useraccount;
    private String currentUserID, currentUserName;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    //test
    private AppBarConfiguration mAppBarConfiguration; //宣告
    public static double camera_position_lat;
    public static double camera_position_lng;

    private TextView notice_count;

    private int n;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoxsFReA:APA91bFrtTvCQxgBDQMTB7MddpMquycE2wOqh4K4_-yHNC2KSxCW0exYbpzx62KmVMNfY8HoZz67HrSc_xbo9NeWPSB13LGBxmAJujI-n90hm3zYLKbZGkqgGo_GIrdFLvcKP77GE5yA";
    final private String contentType = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (chechPermission()) {
            init();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            SendUserToLoginActivity();
        }

        initViews();
        setListeners();
        maplistener = 0;

        notice_count.bringToFront();

        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Date curDate = new Date(System.currentTimeMillis());
                                if (document.getTimestamp("endTime").toDate().before(curDate) && !document.contains("notification")) {
                                    String activityname = document.getId();
                                    HashMap<String, Object> notimap = new HashMap<>();
                                    notimap.put("notification", true);
                                    db.collection("activity").document(activityname)
                                            .set(notimap, SetOptions.merge())
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

                                    Long tsLong = System.currentTimeMillis() / 1000;
                                    String ts = tsLong.toString();

                                    HashMap<String, String> chatNotificationMap = new HashMap<>();
                                    chatNotificationMap.put("from", document.getString("organizerID"));
                                    chatNotificationMap.put("type", "evaluate");
                                    chatNotificationMap.put("time", saveCurrentTime);
                                    chatNotificationMap.put("date", saveCurrentDate);
                                    chatNotificationMap.put("millisecond", ts);
                                    chatNotificationMap.put("activityname", activityname);


                                    db.collection("activity").document(activityname).
                                            collection("participant")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String UserID = document.getId();
                                                            db.collection("user").document(UserID)
                                                                    .collection("notification")
                                                                    .document()
                                                                    .set(chatNotificationMap)
                                                                    .addOnCompleteListener(new OnCompleteListener() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task task) {
                                                                            if (task.isSuccessful()) {
                                                                                db.collection("user")
                                                                                        .document(UserID)
                                                                                        .get()
                                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    DocumentSnapshot document = task.getResult();
                                                                                                    if (document.exists()) {
                                                                                                        String RECEIVER_DEVICE = document.getString("device_token");
                                                                                                        JSONObject notification = new JSONObject();
                                                                                                        JSONObject notifcationBody = new JSONObject();
                                                                                                        try {
                                                                                                            notifcationBody.put("title", "您有新的評價通知");
                                                                                                            notifcationBody.put("message", "您參與的活動" + activityname + "已經結束" + "，至通知處前往評價");
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
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance() == null || FirebaseAuth.getInstance().getCurrentUser() == null || FirebaseAuth.getInstance().getCurrentUser().getUid() == null) {
            SendUserToLoginActivity();
        } else {
            updateUserStatus("online");
            getDBlistener();
            VerifyUserExistance();

            currentUserID = mAuth.getCurrentUser().getUid();
            n=0;
            db.collection("user").document(currentUserID)
                    .collection("notification")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(!document.contains("islook")){
                                        n=n+1;
                                        notice_count.setText(String.valueOf(n));
                                    }
                                }
                                if( n == 0){
                                    notice_count.setVisibility(View.INVISIBLE);
                                }else{
                                    notice_count.setVisibility(View.VISIBLE);
                                }
                            } else {

                            }
                        }
                    });

        }

        Date curDate = new Date(System.currentTimeMillis());
        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getTimestamp("endTime").toDate().before(curDate)){
                                    String actName = document.getId();
                                    db.collection("activity").document(document.getId()).collection("participant")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(!document.contains("checkIn")){
                                                                HashMap<String, Object> check = new HashMap<>();
                                                                check.put("checkIn", false);
                                                                db.collection("activity").document(actName)
                                                                        .collection("participant")
                                                                        .document(document.getId())
                                                                        .set(check, SetOptions.merge())
                                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task task) {
                                                                                if (task.isSuccessful()) {
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    }
                });
    }


//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//
//        if (currentUser != null)
//        {
//            updateUserStatus("offline");
//        }
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//
//        if (currentUser != null)
//        {
//            updateUserStatus("offline");
//        }
//    }

    private void VerifyUserExistance() {
        currentUserID = mAuth.getCurrentUser().getUid();
        db.collection("user").document(currentUserID)
                .collection("profile")
                .document(currentUserID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        if (snapshot != null && snapshot.exists() && snapshot.contains("name")) {
                            Toast.makeText(home.this, "歡迎", Toast.LENGTH_SHORT).show();
                        } else {
                            SendUserToSettingsActivity();
                            Log.d("TAG", "Current data: null");
                        }
                    }
                });
    }

    private void SendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(home.this, testsetting.class);
        startActivity(settingsIntent);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(home.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void updateUserStatus(String state) {
        currentUserID = mAuth.getCurrentUser().getUid();
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        Map<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time", saveCurrentTime);
        onlineStateMap.put("date", saveCurrentDate);
        onlineStateMap.put("state", state);

        db.collection("user")
                .document(currentUserID)
                .set(onlineStateMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

//        if (item.getItemId() == R.id.action_logout) {
//            updateUserStatus("offline");
//            LoginManager.getInstance().logOut();
//            mAuth.signOut();
//            SendUserToLoginActivity();
//        }
//        if (item.getItemId() == R.id.findfriend) {
//            SendUserToFindFriendsActivity();
//        }
//        if (item.getItemId() == R.id.main_settings_option) {
//            SendUserToSettingsActivity();
//        }
//        if (item.getItemId() == R.id.friend_request) {
//            SendUserTorequest();
//        }
//        if (item.getItemId() == R.id.verify_request) {
//            SendUserToverify();
//        }
        if (item.getItemId() == R.id.personal_page) {
            SendUserToPersonalPage();
        }

        return true;
    }

    private void SendUserToPersonalPage() {
        Intent myIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("visit_user_id", currentUserID);
        myIntent.putExtras(bundle);
        myIntent.setClass(home.this, personalpage.class);
        startActivity(myIntent);
    }

    private void SendUserToverify() {
        Intent findFriendsIntent = new Intent(home.this, verifyActivity.class);
        startActivity(findFriendsIntent);
    }

    private void SendUserTorequest() {
        Intent findFriendsIntent = new Intent(home.this, friend_request.class);
        startActivity(findFriendsIntent);
    }

    private void SendUserToFindFriendsActivity() {
        Intent findFriendsIntent = new Intent(home.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void getDBlistener() {
        //監聽資料庫
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference docRef = db.collection("activity");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.");
                    return;
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (doc.getType()) {
                        case ADDED:
                            maplistener = 1;
                            break;
                        case REMOVED:
                            maplistener = 1;
                            break;
                    }
                }
                if(maplistener == 1){
                    addItems();
                    maplistener = 0;
                }
            }
        });
    }

    //監聽攝影機(使用者)是否開始移動
    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
//                Toast.makeText(this, "The user gestured on the map.",
//                        Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
//                Toast.makeText(this, "The user tapped something on the map.",
//                        Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
//                Toast.makeText(this, "The app moved the camera.",
//                        Toast.LENGTH_SHORT).show();
        }
    }

    //抓取使用者當下狀態--移動地圖
    @Override
    public void onCameraMove() {
//            Toast.makeText(this, "The camera is moving.",
//                    Toast.LENGTH_SHORT).show();
    }

    //抓取使用者當下狀態--取消移動地圖的瞬間
    @Override
    public void onCameraMoveCanceled() {
//            Toast.makeText(this, "Camera movement canceled.",
//                    Toast.LENGTH_SHORT).show();
    }

    //抓取使用者當下狀態--停止移動地圖後一段時間
    @Override
    public void onCameraIdle() {
//        Toast.makeText(this, "數據加載中",
//                Toast.LENGTH_SHORT).show();
    }

    private void setUpClusterer() {
        // Position the map.

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        // Add cluster items (markers) to the cluster manager.
        mClusterManager.setOnClusterItemInfoWindowClickListener(
                new ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>() {
                    @Override
                    public void onClusterItemInfoWindowClick(MyItem stringClusterItem) {
                        if (stringClusterItem.getTitle().equals("此處有多個活動")){
                            Intent intent = new Intent();
                            intent.setClass(home.this, signupPageActivity.class);
                            intent.putExtra("activityLat", stringClusterItem.getPosition().latitude);
                            intent.putExtra("activityLong", stringClusterItem.getPosition().longitude);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent();
                            intent.setClass(home.this, signup.class);
                            intent.putExtra("activitytitle", stringClusterItem.getTitle());
                            startActivity(intent);
                        }
                    }
                });
        mMap.setOnInfoWindowClickListener(mClusterManager);
    }

    private void addItems() {
        mClusterManager.clearItems();

        db.collection("activity")
                .orderBy("geopoint")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double latitude = 0;
                            double longitude = 0;
                            String lonLat = "";
                            ArrayList geopoint = new ArrayList();
                            String actName = "";
                            Date snnippet = new Date();
                            SimpleDateFormat ft = new SimpleDateFormat(" yyyy-MM-dd HH:mm ");
                            int count = 0;
                            boolean flag = false;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getTimestamp("endTime").getSeconds() > System.currentTimeMillis() / 1000) {
                                    if(latitude != document.getGeoPoint("geopoint").getLatitude()
                                            && longitude != document.getGeoPoint("geopoint").getLongitude()){
                                        if(count == 1){
                                            offsetItem = new MyItem(latitude, longitude, actName, ft.format(snnippet), markerDescriptor);
                                            mClusterManager.addItem(offsetItem);
                                            mMap.setOnInfoWindowClickListener(mClusterManager);
                                        }
                                        count++;
                                        if(count != 1){
                                            offsetItem = new MyItem(latitude, longitude, actName, ft.format(snnippet), markerDescriptor);
                                            mClusterManager.addItem(offsetItem);
                                            mMap.setOnInfoWindowClickListener(mClusterManager);
                                        }

                                        latitude = document.getGeoPoint("geopoint").getLatitude();
                                        longitude = document.getGeoPoint("geopoint").getLongitude();
                                        snnippet = document.getTimestamp("startTime").toDate();
                                        actName = document.getString("title");

                                    }else{
                                        lonLat = latitude + "," + longitude;
                                        offsetItem = new MyItem(latitude, longitude, "此處有多個活動", "", markerDescriptor);
                                        mClusterManager.addItem(offsetItem);
                                        mMap.setOnInfoWindowClickListener(mClusterManager);
                                        geopoint.add(lonLat);
                                    }
                                }
                            }
                            mClusterManager.cluster();
                        }

                    }
                });
        mClusterManager.cluster();
        setUpClusterer();
        final MyRenderer renderer = new MyRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);
    }

    private void addItemType(String type){

        mClusterManager.clearItems();
        db.collection("activity")
                .orderBy("geopoint")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double latitude = 0;
                            double longitude = 0;
                            String lonLat = "";
                            ArrayList geopoint = new ArrayList();
                            String actName = "";
                            Date snnippet = new Date();
                            SimpleDateFormat ft = new SimpleDateFormat(" yyyy-MM-dd HH:mm ");
                            int count = 0;
                            boolean flag = false;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (((document.getTimestamp("endTime").getSeconds()) > System.currentTimeMillis() / 1000) && document.getString("activityType").equals(type)) {
                                    if(latitude != document.getGeoPoint("geopoint").getLatitude()
                                            && longitude != document.getGeoPoint("geopoint").getLongitude()){
                                        if(count == 1){
                                            offsetItem = new MyItem(latitude, longitude, actName, ft.format(snnippet), markerDescriptor);
                                            mClusterManager.addItem(offsetItem);
                                            mMap.setOnInfoWindowClickListener(mClusterManager);
                                        }
                                        count++;
                                        if(count != 1){
                                            offsetItem = new MyItem(latitude, longitude, actName, ft.format(snnippet), markerDescriptor);
                                            mClusterManager.addItem(offsetItem);
                                            mMap.setOnInfoWindowClickListener(mClusterManager);
                                        }

                                        latitude = document.getGeoPoint("geopoint").getLatitude();
                                        longitude = document.getGeoPoint("geopoint").getLongitude();
                                        snnippet = document.getTimestamp("startTime").toDate();
                                        actName = document.getString("title");

                                    }else{
                                        lonLat = latitude + "," + longitude;
                                        offsetItem = new MyItem(latitude, longitude, "此處有多個活動", "", markerDescriptor);
                                        mClusterManager.addItem(offsetItem);
                                        mMap.setOnInfoWindowClickListener(mClusterManager);
                                        geopoint.add(lonLat);
                                    }
                                }
                            }
                            mClusterManager.cluster();
                        }
                    }
                });
        mClusterManager.cluster();
        setUpClusterer();
        final MyRenderer renderer = new MyRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);
    }

    private void initViews() {
        ballbtn = (Button) findViewById(R.id.ballbtn);
        storebtn = (Button) findViewById(R.id.storebtn);
        ktvbtn = (Button) findViewById(R.id.ktvbtn);
        informationbtn = (Button) findViewById(R.id.informationbtn);
        jobtn = (Button) findViewById(R.id.joBtn);
        favoritebtn = (Button) findViewById(R.id.collectBtn);
        settingbtn = (Button) findViewById(R.id.settingBtn);
        messagebtn = (Button) findViewById(R.id.messageBtn);
        noticebtn = (TextView) findViewById(R.id.noticeBtn);
        refreshbtn = (Button) findViewById(R.id.refreshBtn);
        otherbtn = (Button) findViewById(R.id.otherbtn);
        findFriendBtn = (Button) findViewById(R.id.button21);
        setProfileBtn = (Button) findViewById(R.id.button15);
        inviteFriendBtn = (Button) findViewById(R.id.button30);
        actInviteBtn = (Button) findViewById(R.id.button31);
        logoutBtn = (Button) findViewById(R.id.button32);
        notice_count=(TextView) findViewById(R.id.notice_count);
    }

    //取得使用者當前位置 -1
    public void init() {
        mLocationPermissionGranted = true;

        Places.initialize(getApplicationContext(), "AIzaSyAKuaxAND8zfIysSz1HdoNF88o1aK8ZIN4");
        PlacesClient placesClient = Places.createClient(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addConnectionCallbacks(connectionCallbacks)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //取得使用者當前位置 -2
    private GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(onMapReadyCallback);
        }

        @Override
        public void onConnectionSuspended(int i) {
        }
    };

    //取得使用者當前位置 -3 & 初始化地圖
    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mDefaultLocation = new LatLng(25.033493, 121.564101);
            if (!getDeviceLocation()) {
                //mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("Marker in Taipei 101"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mDefaultLocation));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            }

            if (ActivityCompat.checkSelfPermission(home.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(home.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //隱藏放大縮小按鈕
            mMap.getUiSettings().setZoomControlsEnabled(false);
        }
    };

    //使用者是否開啟定位
    private Boolean getDeviceLocation() {

        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), 16));
            userlat = mLastKnownLocation.getLatitude();
            userlnt = mLastKnownLocation.getLongitude();
            return true;
        }
        return false;
    }

    //是否給予JOINME讀取定位資訊
    public Boolean chechPermission() {
        String[] pm = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
        List<String> list = new ArrayList<>();

        for (int i = 0; i < pm.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, pm[i]) != PackageManager.PERMISSION_GRANTED) {
                list.add(pm[i]);
            }
        }
        if (list.size() > 0) {
            ActivityCompat.requestPermissions(this, list.toArray(new String[list.size()]), 1);
            return false;
        }
        return true;
    }

    //是否給予joinme權限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    int i;
                    for (i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            break;
                        }
                    }
                    if (i == permissions.length) {
                        init();
                    }
                } else {
                    init();
                }
                return;
        }
    }

    //地圖初始化
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpClusterer();
        final MyRenderer renderer = new MyRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent();
        intent.setClass(home.this, signup.class);
        startActivity(intent);
    }

    private void initData() {
    }

    private void setListeners() {

        jobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, jo.class);
                startActivity(intent);
                home.this.onStop();
            }
        });

        messagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, testmain.class);
                startActivity(intent);
                home.this.onStop();
            }
        });

        noticebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, noticeupdate.class);
                startActivity(intent);
                n=0;
                home.this.onStop();
            }
        });

        favoritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, favoriteActivity.class);
                startActivity(intent);
                home.this.onStop();
            }
        });

        ballbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("運動");

                ballbtn.setBackground(getResources().getDrawable(R.drawable.ballclick));
                storebtn.setBackground(getResources().getDrawable(R.drawable.discount));
                ktvbtn.setBackground(getResources().getDrawable(R.drawable.ktv));
                informationbtn.setBackground(getResources().getDrawable(R.drawable.timelimit));
                otherbtn.setBackground(getResources().getDrawable(R.drawable.others));
            }
        });

        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("商家優惠");

                ballbtn.setBackground(getResources().getDrawable(R.drawable.ball));
                storebtn.setBackground(getResources().getDrawable(R.drawable.storeclick));
                ktvbtn.setBackground(getResources().getDrawable(R.drawable.ktv));
                informationbtn.setBackground(getResources().getDrawable(R.drawable.timelimit));
                otherbtn.setBackground(getResources().getDrawable(R.drawable.others));
            }
        });

        ktvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("KTV");

                ballbtn.setBackground(getResources().getDrawable(R.drawable.ball));
                storebtn.setBackground(getResources().getDrawable(R.drawable.discount));
                ktvbtn.setBackground(getResources().getDrawable(R.drawable.ktvclick));
                informationbtn.setBackground(getResources().getDrawable(R.drawable.timelimit));
                otherbtn.setBackground(getResources().getDrawable(R.drawable.others));
            }
        });

        informationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("限時");

                ballbtn.setBackground(getResources().getDrawable(R.drawable.ball));
                storebtn.setBackground(getResources().getDrawable(R.drawable.discount));
                ktvbtn.setBackground(getResources().getDrawable(R.drawable.ktv));
                informationbtn.setBackground(getResources().getDrawable(R.drawable.infoclick));
                otherbtn.setBackground(getResources().getDrawable(R.drawable.others));
            }
        });

        otherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("其他");

                ballbtn.setBackground(getResources().getDrawable(R.drawable.ball));
                storebtn.setBackground(getResources().getDrawable(R.drawable.discount));
                ktvbtn.setBackground(getResources().getDrawable(R.drawable.ktv));
                informationbtn.setBackground(getResources().getDrawable(R.drawable.timelimit));
                otherbtn.setBackground(getResources().getDrawable(R.drawable.otherclick));
            }
        });

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItems();

                ballbtn.setBackground(getResources().getDrawable(R.drawable.ball));
                storebtn.setBackground(getResources().getDrawable(R.drawable.discount));
                ktvbtn.setBackground(getResources().getDrawable(R.drawable.ktv));
                informationbtn.setBackground(getResources().getDrawable(R.drawable.timelimit));
                otherbtn.setBackground(getResources().getDrawable(R.drawable.others));
            }
        });
        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(findFriendBtn.getVisibility() == View.INVISIBLE){
                    findFriendBtn.setVisibility(View.VISIBLE);
                    setProfileBtn.setVisibility(View.VISIBLE);
                    inviteFriendBtn.setVisibility(View.VISIBLE);
                    actInviteBtn.setVisibility(View.VISIBLE);
                    logoutBtn.setVisibility(View.VISIBLE);
                    findFriendBtn.setEnabled(true);
                    setProfileBtn.setEnabled(true);
                    inviteFriendBtn.setEnabled(true);
                    actInviteBtn.setEnabled(true);
                    logoutBtn.setEnabled(true);
                }else{
                    findFriendBtn.setVisibility(View.INVISIBLE);
                    setProfileBtn.setVisibility(View.INVISIBLE);
                    inviteFriendBtn.setVisibility(View.INVISIBLE);
                    actInviteBtn.setVisibility(View.INVISIBLE);
                    logoutBtn.setVisibility(View.INVISIBLE);
                    findFriendBtn.setEnabled(false);
                    setProfileBtn.setEnabled(false);
                    inviteFriendBtn.setEnabled(false);
                    actInviteBtn.setEnabled(false);
                    logoutBtn.setEnabled(false);
                }
            }
        });
        findFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, FindFriendsActivity.class);
                startActivity(intent);
                home.this.onStop();
            }
        });
        setProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, testsetting.class);
                startActivity(intent);
                home.this.onStop();
            }
        });
        inviteFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, friend_request.class);
                startActivity(intent);
                home.this.onStop();
            }
        });
        actInviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, verifyActivity.class);
                startActivity(intent);
                home.this.onStop();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, MainActivity.class);
                startActivity(intent);
                updateUserStatus("offline");
                LoginManager.getInstance().logOut();
                mAuth.signOut();
                SendUserToLoginActivity();
            }
        });
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
                        Toast.makeText(home.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }) {
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
