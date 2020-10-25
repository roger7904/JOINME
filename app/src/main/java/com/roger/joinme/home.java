package com.roger.joinme;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    private Button noticebtn;
    private Button findFriendBtn,settingbtn,inviteFriendBtn,actInviteBtn;
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
        getDBlistener();

        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (((document.getTimestamp("endTime").getSeconds()) < System.currentTimeMillis() / 1000) && !document.contains("notification")) {
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

            VerifyUserExistance();

            currentUserID = mAuth.getCurrentUser().getUid();
        }
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

        if (item.getItemId() == R.id.action_logout) {
            updateUserStatus("offline");
            LoginManager.getInstance().logOut();
            mAuth.signOut();
            SendUserToLoginActivity();
        }
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
        Intent Intent = new Intent(home.this, personalpage.class);
        startActivity(Intent);
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
                            mClusterManager.clearItems();
                            mClusterManager.cluster();
                            maplistener = 1;
                            break;
                        case REMOVED:
                            mClusterManager.clearItems();
                            mClusterManager.cluster();
                            maplistener = 1;
                            break;
                    }
                }
                if (maplistener == 1) {
                    addItems();
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
        //抓使用者定位  mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.732375, 120.276439), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        // Add cluster items (markers) to the cluster manager.
//        addItems();
        mClusterManager.setOnClusterItemInfoWindowClickListener(
                new ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>() {
                    @Override
                    public void onClusterItemInfoWindowClick(MyItem stringClusterItem) {
                        Intent intent = new Intent();
                        intent.setClass(home.this, signup.class);
                        intent.putExtra("activitytitle", stringClusterItem.getTitle());
                        startActivity(intent);
                    }
                });
        mMap.setOnInfoWindowClickListener(mClusterManager);
    }

    private void addItems() {
        mClusterManager.clearItems();

        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if ((document.getTimestamp("endTime").getSeconds()) > System.currentTimeMillis() / 1000) {
                                    lat = document.getGeoPoint("geopoint").getLatitude();
                                    lng = document.getGeoPoint("geopoint").getLongitude();
                                    markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

                                    //將資料庫中timestamp型態轉為date後用simpledateformat儲存
                                    Date snnippet = document.getTimestamp("startTime").toDate();
                                    SimpleDateFormat ft = new SimpleDateFormat(" yyyy-MM-dd HH :mm:ss ");
                                    offsetItem = new MyItem(lat, lng, document.getString("title"), ft.format(snnippet), markerDescriptor);
                                    mClusterManager.addItem(offsetItem);
                                    mMap.setOnInfoWindowClickListener(mClusterManager);
                                }
                            }
                        }
                    }
                });

        mClusterManager.cluster();
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }

    private void addItemType(String type){
        mClusterManager.clearItems();

        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (((document.getTimestamp("endTime").getSeconds()) > System.currentTimeMillis() / 1000) && document.getString("activityType").equals(type)) {
                                    lat = document.getGeoPoint("geopoint").getLatitude();
                                    lng = document.getGeoPoint("geopoint").getLongitude();

                                    //將資料庫中timestamp型態轉為date後用simpledateformat儲存
                                    Date snnippet = document.getTimestamp("startTime").toDate();
                                    SimpleDateFormat ft = new SimpleDateFormat(" yyyy-MM-dd HH :mm:ss ");
                                    offsetItem = new MyItem(lat, lng, document.getString("title"), ft.format(snnippet), markerDescriptor);
                                    mClusterManager.addItem(offsetItem);
                                    mMap.setOnInfoWindowClickListener(mClusterManager);
                                }
                            }
                        }
                    }
                });

        mClusterManager.cluster();
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
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
        noticebtn = (Button) findViewById(R.id.noticeBtn);
        refreshbtn = (Button) findViewById(R.id.refreshBtn);
        otherbtn = (Button) findViewById(R.id.otherbtn);
        findFriendBtn = (Button) findViewById(R.id.button21);
        setProfileBtn = (Button) findViewById(R.id.button15);
        inviteFriendBtn = (Button) findViewById(R.id.button30);
        actInviteBtn = (Button) findViewById(R.id.button31);
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
                mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            }

            if (ActivityCompat.checkSelfPermission(home.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(home.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
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
                            mLastKnownLocation.getLongitude()), 12));
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

    //地址轉經緯度method(很容易讀不到資料)
    public LatLng getLocationFromAddress(String address) {
        Geocoder geo = new Geocoder(this);
        List<Address> adres;
        LatLng point = null;
        try {
            adres = geo.getFromLocationName(address, 5);
            Thread.sleep(500);
            while (adres.size() == 0) {
                adres = geo.getFromLocationName(address, 5);
                Thread.sleep(500);
            }
            Address location = adres.get(0);
            point = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return point;
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
            }
        });

        messagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, testmain.class);
                startActivity(intent);
            }
        });

        noticebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, noticeupdate.class);
                startActivity(intent);
            }
        });

        favoritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, favoriteActivity.class);
                startActivity(intent);
            }
        });

        ballbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("運動");
            }
        });

        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("商家優惠");
            }
        });

        ktvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("KTV");
            }
        });

        informationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("限時");
            }
        });

        otherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemType("其他");
            }
        });

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItems();
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
                    findFriendBtn.setEnabled(true);
                    setProfileBtn.setEnabled(true);
                    inviteFriendBtn.setEnabled(true);
                    actInviteBtn.setEnabled(true);
                }else{
                    findFriendBtn.setVisibility(View.INVISIBLE);
                    setProfileBtn.setVisibility(View.INVISIBLE);
                    inviteFriendBtn.setVisibility(View.INVISIBLE);
                    actInviteBtn.setVisibility(View.INVISIBLE);
                    findFriendBtn.setEnabled(false);
                    setProfileBtn.setEnabled(false);
                    inviteFriendBtn.setEnabled(false);
                    actInviteBtn.setEnabled(false);
                }
            }
        });
        findFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, FindFriendsActivity.class);
                startActivity(intent);
            }
        });
        setProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, testsetting.class);
                startActivity(intent);
            }
        });
        inviteFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, friend_request.class);
                startActivity(intent);
            }
        });
        actInviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(home.this, verifyActivity.class);
                startActivity(intent);
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
