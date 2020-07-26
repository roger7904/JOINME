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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class home extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener{
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
    private Button messagepagebtn;
    private Button settingbtn;
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
    public static String activitytitle;
    public int maplistener = 0;
    public static String useraccount;
    private String currentUserID, currentUserName;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    //test
    private AppBarConfiguration mAppBarConfiguration; //宣告

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        //NavigationView navigationView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        if (chechPermission()) {
            init();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("user").document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        useraccount=document.getString("email");
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        initViews();
        setListeners();
        maplistener = 0;
        getDBlistener();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            updateUserStatus("online");

            //VerifyUserExistance();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        if (currentUser != null)
        {
            updateUserStatus("offline");
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (currentUser != null)
        {
            updateUserStatus("offline");
        }
    }

    private void VerifyUserExistance()
    {
        db.collection("user").document(currentUserID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists() && snapshot.contains("name")) {
                    //這邊要驗證有沒有設定名字
                    Toast.makeText(home.this, "歡迎", Toast.LENGTH_SHORT).show();
                } else {
                    SendUserToSettingsActivity();
                    Log.d("TAG", "Current data: null");
                }
            }
        });

    }

    private void SendUserToSettingsActivity()
    {
        Intent settingsIntent = new Intent(home.this, testsetting.class);
        startActivity(settingsIntent);
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(home.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void updateUserStatus(String state)
    {
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.action_logout)
        {
            updateUserStatus("offline");
            LoginManager.getInstance().logOut();
            mAuth.signOut();
            SendUserToLoginActivity();
        }if (item.getItemId() == R.id.findfriend)
        {
            SendUserToFindFriendsActivity();
        }
        if (item.getItemId() == R.id.main_settings_option)
        {
            SendUserToSettingsActivity();
        }if (item.getItemId() == R.id.friend_request)
        {
            SendUserTorequest();
        }

        return true;
    }

    private void SendUserTorequest()
    {
        Intent findFriendsIntent = new Intent(home.this, friend_request.class);
        startActivity(findFriendsIntent);
    }

    private void SendUserToFindFriendsActivity()
    {
        Intent findFriendsIntent = new Intent(home.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void getDBlistener(){
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
                            System.out.println("1");
                            mClusterManager.clearItems();
                            mClusterManager.cluster();
                            maplistener = 1;
                            break;
                        case REMOVED:
                            System.out.println("2");
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
                    @Override public void onClusterItemInfoWindowClick(MyItem stringClusterItem) {
                        activitytitle = stringClusterItem.getTitle();
                        Intent intent = new Intent();
                        intent.setClass(home.this, signup.class);
                        startActivity(intent);
                    }
                });
        mMap.setOnInfoWindowClickListener(mClusterManager);
    }

    private void addItems() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete (@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                lat = document.getGeoPoint("geopoint").getLatitude();
                                lng = document.getGeoPoint("geopoint").getLongitude();
                                if(document.getString("activityType").equals("ball")){
                                    markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                                }else if(document.getString("activityType").equals("eat")){
                                    markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                                }else if(document.getString("activityType").equals("KTV")){
                                    markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                                }else{
                                    markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                                }
                                //將資料庫中timestamp型態轉為date後用simpledateformat儲存
                                Date snnippet = document.getTimestamp("startTime").toDate();
                                SimpleDateFormat ft = new SimpleDateFormat( " yyyy-MM-dd hh :mm:ss " );
                                offsetItem = new MyItem(lat, lng,document.getString("title"),ft.format(snnippet),markerDescriptor);
                                mClusterManager.addItem(offsetItem);
                                mMap.setOnInfoWindowClickListener(mClusterManager);
                            }
                        }
                    }
                });
    }

    private void initViews() {
        ballbtn = (Button)findViewById(R.id.ballbtn);
        storebtn = (Button)findViewById(R.id.storebtn);
        ktvbtn = (Button)findViewById(R.id.ktvbtn);
        informationbtn = (Button)findViewById(R.id.informationbtn);
        jobtn = (Button)findViewById(R.id.joBtn);
        favoritebtn = (Button)findViewById(R.id.collectBtn);
        settingbtn = (Button)findViewById(R.id.settingBtn);
        messagebtn = (Button)findViewById(R.id.messageBtn);
        noticebtn = (Button)findViewById(R.id.noticeBtn);
    }

    //取得使用者當前位置 -1
    public void init() {
        mLocationPermissionGranted = true;

        Places.initialize(getApplicationContext(),"AIzaSyAKuaxAND8zfIysSz1HdoNF88o1aK8ZIN4");
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
    public Boolean chechPermission(){
        String[] pm = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};

        List<String> list = new ArrayList<>();

        for(int i=0;i<pm.length;i++) {
            if (ActivityCompat.checkSelfPermission(this, pm[i]) != PackageManager.PERMISSION_GRANTED) {
                list.add(pm[i]);
            }
        }
        if(list.size()>0){
            ActivityCompat.requestPermissions(this, list.toArray(new String[list.size()]), 1);
            return false;
        }
        return true;
    }

    //是否給予joinme權限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case 1:
                if (grantResults.length > 0){
                    int i;
                    for(i =0;i<permissions.length;i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            break;
                        }
                    }
                    if(i==permissions.length){
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

//        mMap.setOnCameraIdleListener(this);
//        mMap.setOnCameraMoveStartedListener(this);
//        mMap.setOnCameraMoveListener(this);
//        mMap.setOnCameraMoveCanceledListener(this);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.732375, 120.276439), 10));

        //讀取資料庫資料
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //抓集合
//        db.collection( "activity" )
//                .orderBy("startTime")
//                .limit(20)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
//                        if (task.isSuccessful()) {
//                            for(int i=0;i<20;i++){
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    //抓取document名稱及內部欄位資料
//                                    System.out.println(document.getId ());
//                                    locate2[i] = getLocationFromAddress(document.getString("location"));
//                                }
//                            }
//                        } else {
//                            Log.w("TAG", "Error getting documents.",task.getException());
//                        }
//                    }
//                });

        //座標位置 之後從使用者輸入的地址抓經緯度 之後用陣列存位置120.277872,22.734315
//        LatLng locate = new LatLng(22.734315,120.277872);
//        LatLng locatee = new LatLng(22.44065,120.285000);

    }

    //地址轉經緯度method(很容易讀不到資料)
    public LatLng getLocationFromAddress(String address){
        Geocoder geo = new Geocoder(this);
        List<Address> adres;
        LatLng point = null;
        try{
            adres = geo.getFromLocationName(address,5);
            Thread.sleep(500);
            while(adres.size() == 0){
                adres = geo.getFromLocationName(address,5);
                Thread.sleep(500);
            }
            Address location = adres.get(0);
            point = new LatLng(location.getLatitude(),location.getLongitude());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return point;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //氣泡視窗訊息
//        Toast.makeText(this, "鬥牛啦\n"+"開始時間：2020/3/11 15:00"+"\n"+"結束時間：2020/3/11 17:00",
//                Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(home.this, signup.class);
        startActivity(intent);
    }


    private void initData()
    {
    }

    private void setListeners() {
//        selfpage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(home.this, selfpage.class);
//                startActivity(intent);
//            }
//        });
//        homebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(home.this, home.class);
//                startActivity(intent);
//            }
//        });
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
        ballbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(this, "數據加載中",Toast.LENGTH_SHORT).show();
                mClusterManager.clearItems();
                mClusterManager.cluster();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                firestore.setFirestoreSettings(settings);
                //讀取資料庫資料
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //抓集合
                db.collection( "activity" )
                        .whereEqualTo("activityType", "ball")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        lat = document.getGeoPoint("geopoint").getLatitude();
                                        lng = document.getGeoPoint("geopoint").getLongitude();
//                                        System.out.println(document.getString("activityPhoto"));
//                                        try {
//                                            URL url = new URL(document.getString("activityPhoto"));
//                                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//                                            connection.setDoInput(true);
//                                            connection.connect();
//                                            InputStream input = connection.getInputStream();
//                                            Bitmap bitmap = BitmapFactory.decodeStream(input);
////                                            BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(bitmap);
//                                        } catch (MalformedURLException e) {
//                                            e.printStackTrace();
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
                                        //將資料庫中timestamp型態轉為date後用simpledateformat儲存
                                        Date snnippet = document.getTimestamp("startTime").toDate();
                                        SimpleDateFormat ft = new SimpleDateFormat( " yyyy-MM-dd hh :mm:ss " );
                                        markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                                        MyItem offsetItem = new MyItem(lat, lng,document.getString("title"),ft.format(snnippet),markerDescriptor);

                                        mClusterManager.addItem(offsetItem);
                                        mMap.setOnInfoWindowClickListener(mClusterManager);
                                    }
                                }
                            }
                        });
                mMap.setOnCameraIdleListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
            }
        });
        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(this, "數據加載中",
//                        Toast.LENGTH_SHORT).show();
                mClusterManager.clearItems();
                mClusterManager.cluster();
                //讀取資料庫資料
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //抓集合
                db.collection( "activity" )
                        .whereEqualTo("activityType", "eat")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        lat = document.getGeoPoint("geopoint").getLatitude();
                                        lng = document.getGeoPoint("geopoint").getLongitude();

                                        //將資料庫中timestamp型態轉為date後用simpledateformat儲存
                                        Date snnippet = document.getTimestamp("startTime").toDate();
                                        SimpleDateFormat ft = new SimpleDateFormat( " yyyy-MM-dd hh :mm:ss " );
                                        markerDescriptor = markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                                        MyItem offsetItem = new MyItem(lat, lng,document.getString("title"),ft.format(snnippet),markerDescriptor);

                                        mClusterManager.addItem(offsetItem);
                                        mMap.setOnInfoWindowClickListener(mClusterManager);
                                    }
                                }
                            }
                        });
                mMap.setOnCameraIdleListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
            }
        });
        ktvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(this, "數據加載中",
//                        Toast.LENGTH_SHORT).show();
                mClusterManager.clearItems();
                mClusterManager.cluster();
                //讀取資料庫資料
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //抓集合
                db.collection( "activity" )
                        .whereEqualTo("activityType", "KTV")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        lat = document.getGeoPoint("geopoint").getLatitude();
                                        lng = document.getGeoPoint("geopoint").getLongitude();

                                        //將資料庫中timestamp型態轉為date後用simpledateformat儲存
                                        Date snnippet = document.getTimestamp("startTime").toDate();
                                        SimpleDateFormat ft = new SimpleDateFormat( " yyyy-MM-dd hh :mm:ss " );
                                        markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                                        MyItem offsetItem = new MyItem(lat, lng,document.getString("title"),ft.format(snnippet),markerDescriptor);

                                        mClusterManager.addItem(offsetItem);
                                        mMap.setOnInfoWindowClickListener(mClusterManager);
                                    }
                                }
                            }
                        });
                mMap.setOnCameraIdleListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
            }
        });
        informationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(this, "數據加載中",
//                        Toast.LENGTH_SHORT).show();
                mClusterManager.clearItems();
                mClusterManager.cluster();
                //讀取資料庫資料
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //抓集合
                db.collection( "activity" )
                        .whereEqualTo("activityType", "information")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        lat = document.getGeoPoint("geopoint").getLatitude();
                                        lng = document.getGeoPoint("geopoint").getLongitude();

                                        //將資料庫中timestamp型態轉為date後用simpledateformat儲存
                                        Date snnippet = document.getTimestamp("startTime").toDate();
                                        SimpleDateFormat ft = new SimpleDateFormat( " yyyy-MM-dd hh :mm:ss " );
                                        markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                                        MyItem offsetItem = new MyItem(lat, lng,document.getString("title"),ft.format(snnippet),markerDescriptor);

                                        mClusterManager.addItem(offsetItem);
                                        mMap.setOnInfoWindowClickListener(mClusterManager);
                                    }
                                }
                            }
                        });
                mMap.setOnCameraIdleListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
            }
        });
    }

}
