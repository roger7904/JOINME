package com.roger.joinme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.Menu;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private GoogleMap mMapp;
    private Location mLastKnownLocation;
    private Boolean mLocationPermissionGranted = false;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mDefaultLocation;
    private Button homepage;
    private Button selfpage;
    private Button logout;
    private Button chatroom;
    private Button favorite;
    private Button jo;
    private Button notice;
    private Button setting;
    private Button ball;
    //test
    private AppBarConfiguration mAppBarConfiguration; //宣告

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initViews();
        setListeners();

        if (chechPermission()) {
            init();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

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

            if (ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    };

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
            return true;
        }
        return false;
    }

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


    private void initViews()
    {
        homepage=(Button)findViewById(R.id.btn_to_homepage);
        selfpage=(Button)findViewById(R.id.btn_to_selfpage);
        logout=(Button)findViewById(R.id.btn_logout);
        chatroom=(Button)findViewById(R.id.btn_to_messagepage);
        favorite=(Button)findViewById(R.id.btn_to_favorite);
        jo=(Button)findViewById(R.id.btn_to_jo);
        notice=(Button)findViewById(R.id.btn_to_notice);
        setting=(Button)findViewById(R.id.btn_to_setting);
        ball=(Button)findViewById(R.id.ballbtn);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMapp = googleMap;
//        LatLng[] locate3 = new LatLng[1000000000];

        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.head);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

        //座標位置 之後從使用者輸入的地址抓經緯度 之後用陣列存位置
        LatLng locate = new LatLng(23.861053,120.915834);
        LatLng locatee = new LatLng(22.44065,120.285000);

        //設定座標的標題以及詳細內容 之後從資料庫抓取
        mMap.setOnInfoWindowClickListener(this);
        mMap.addMarker(new MarkerOptions().position(locate).title("鬥牛啦").snippet("起：2020/3/11 15:00"+"\n"+"迄：2020/3/11 17:00").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        getInfowWindow(mMap);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locate,14));

        mMapp.addMarker(new MarkerOptions().position(locatee).title("kaohsiung").snippet("Testt"));
        mMapp.moveCamera(CameraUpdateFactory.newLatLngZoom(locate,14));

    }

    //地址轉經緯度method
    public LatLng getLocationFromAddress(String address){
        Geocoder geo = new Geocoder(this);
        List<Address> adres;
        LatLng point = null;
        try{
            adres = geo.getFromLocationName(address,5);
            if(adres == null){
                return null;
            }
            Address location = adres.get(0);
            point = new LatLng(location.getLatitude(),location.getLongitude());
        } catch (IOException e) {
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
        intent.setClass(MapsActivity.this, signup.class);
        startActivity(intent);
    }

    private void setListeners() {

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        selfpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, selfpage.class);
                startActivity(intent);
            }
        });

        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, chatroom.class);
                startActivity(intent);
            }
        });


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, favorite.class);
                startActivity(intent);
            }
        });



        jo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, jo.class);
                startActivity(intent);
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, notice.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, setting.class);
                startActivity(intent);
            }
        });

        ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("ball");
            }
        });
        System.out.println("test");

    }

}