package com.roger.joinme;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Button eatbtn;
    private Button viewbtn;
    private Button tripbtn;
    private static int count = 0;
    public double userlat;
    public double userlnt;
    public double distanceresult;
    public Marker marker;
    public Marker marker1;
    public Marker markerSmall;
    private LatLng[] locateArray = new LatLng[10000];
    private LatLng[] locate = new LatLng[10000];
    private String[] title=new String[10000];
    private int dbcount = 0;
    private Marker[] markerArray=new Marker[10000];


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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        if (chechPermission()) {
            init();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();
        setListeners();
        initDB();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
        float zoom = 0;

        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.head);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);


        Toast.makeText(this, "數據加載中",
                Toast.LENGTH_SHORT).show();

        zoom = mMap.getCameraPosition().zoom;
        if(zoom<13){
            System.out.println(zoom);
//            marker.setVisible(false);
            markerSmall.setVisible(true);
            for(int i =0 ;i<dbcount;i++){
                markerArray[i].setVisible(false);
            }
//                distanceresult = getDistance(userlnt,userlat,120.277872,22.734315);
//                if(distanceresult <= 5000) {

            System.out.println("1");
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 14));
//                }
            //這一部分還有很大問題 geocoder的調用
//                System.out.println(zoom);
            //讀取資料庫資料
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
            //抓集合
//                db.collection( "activity" )
//                        .orderBy("startTime")
//                        .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                while(count<20){
////                                    if(document.getString())
//                                    System.out.println(getLocationFromAddress(document.getString("location")));
//                                    locate[count] = getLocationFromAddress(document.getString("location"));
//                                    String locateString = locate[count].toString();
//                                    String[] locatesplit = locateString.replaceAll("lat/lng: \\p{Punct} ","").split(",");
//                                    for(int i = 0;i<25;i++){
//                                        System.out.println(locatesplit[i]);
//                                    }
//                                    System.out.println(locate[count]);
//                                    System.out.println(count);
//                                    count++;
//                                }
//                            }
//                        } else {
//                            Log.w("TAG", "Error getting documents.",task.getException());
//                        }
//                    }
//                });
        }else{
//            marker.setVisible(true);
            for(int i =0 ;i<dbcount;i++){
                markerArray[i].setVisible(true);
            }
            markerSmall.setVisible(false);
            System.out.println(zoom);
        }
    }

    //計算結果單位為公尺
    public double getDistance(double lng1,double lat1,double lng2,double lat2){
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b/2),2)));
        double result = Math.round(s * 10000d) / 10000d;
        return result;
    }

    private static double rad(double d){
        return d * Math.PI /180.0;
    }

    private void initViews() {
        ballbtn = (Button)findViewById(R.id.ballbtn);
        eatbtn = (Button)findViewById(R.id.eatbtn);
        viewbtn = (Button)findViewById(R.id.viewbtn);
        tripbtn = (Button)findViewById(R.id.tripbtn);
        selfpage=(Button)findViewById(R.id.btn_to_selfpage);
        ball=(Button)findViewById(R.id.ballbtn);
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

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

        final LatLng[] locate2 = new LatLng[100000];
        LatLng test = new LatLng(120.277872,22.734315);

        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.head);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

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
        LatLng locate = new LatLng(22.734315,120.277872);
        LatLng locatee = new LatLng(22.718379,120.304477);

        //設定座標的標題以及詳細內容 之後從資料庫抓取
        mMap.setOnInfoWindowClickListener(this);
//        marker = mMap.addMarker(new MarkerOptions().position(locate).title("鬥牛啦").snippet("起：2020/3/11 15:00"+"\n"+"迄：2020/3/11 17:00").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
//        getInfowWindow(mMap);
        markerSmall= mMap.addMarker(new MarkerOptions().position(locatee).title("楠梓區").snippet("4個活動"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locate,14));
//        marker1 = mMap.addMarker(new MarkerOptions().position(test).title("鬥牛啦").snippet("起：2020/5/12 14:00" + "\n" + "迄：2020/5/12 17:00"));
//.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
//        marker1.setVisible(false);

        getInfowWindow(mMap);
        mMap.setOnInfoWindowClickListener(this);

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

    public void getInfowWindow(GoogleMap marker){

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
        selfpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("123");
            }
        });
        ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("123");
            }
        });
        String data[];
        ballbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        .orderBy("startTime")
                        .limit(20)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        document.getData();
                                        //抓取document名稱及內部欄位資料
                                        System.out.println(document.getId () + " => " );
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.",task.getException());
                                }
                            }
                        });
            }
        });
        eatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //讀取資料庫資料
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //抓集合
                db.collection( "activity" )
                        .whereEqualTo("activityType", "ball")
                        .orderBy("startTime")
                        .limit(20)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        document.getData();
                                        System.out.println(document.getId () + " => " + document.getData().size());
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.",task.getException());
                                }
                            }
                        });
            }
        });
        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //讀取資料庫資料
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //抓集合
                db.collection( "activity" )
                        .whereEqualTo("activityType", "ball")
                        .orderBy("startTime")
                        .limit(20)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        document.getData();
                                        System.out.println(document.getId() + " => " + document.getData());
//                                      document.getString();
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.",task.getException());
                                }
                            }
                        });
            }
        });
        tripbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //讀取資料庫資料
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //抓集合
                db.collection( "activity" )
                        .whereEqualTo("activityType", "ball")
//                        .orderBy("startTime")
//                        .limit(20)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        System.out.println(document.getString("location"));
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.",task.getException());
                                }
                            }
                        });
            }
        });
    }

    public void initDB(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //抓集合
        db.collection( "activity" )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                        document.getData();
                                //抓取document名稱及內部欄位資料
                                title[dbcount]=document.getData().get("title").toString();
                                GeoPoint geoPoint = document.getGeoPoint("geopoint");
                                double lat=geoPoint.getLatitude();
                                double lng=geoPoint.getLongitude();
                                locateArray[dbcount]=new LatLng(lat,lng);
                                System.out.println(title[dbcount]+":"+locateArray[dbcount]);
                                dbcount++;
                            }
                            for(int i = 0 ; i < dbcount ; i++){
                                markerArray[i]=mMap.addMarker(new MarkerOptions().position(locateArray[i]).title(title[i]));
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.",task.getException());
                        }

                    }
                });

    }

}
