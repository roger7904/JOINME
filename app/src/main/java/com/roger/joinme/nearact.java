package com.roger.joinme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class nearact extends AppCompatActivity implements OnMapReadyCallback {
//    FragmentActivity 可能得拆另一個頁面寫或是這個頁面只顯示地圖
    private Button actbtn;
    private MapView mMapView;

//    MapFragment mMapFragment= MapFragment.newInstance();
//    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//    fragmentTransaction.add(R.id.map, mMapFragment);
//    fragmentTransaction.commit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearact);
        initViews();
        setListeners();

    }

    @Override
    protected  void onResume(){
        super.onResume();

    }

    private void initViews()
    {
        actbtn=(Button)findViewById(R.id.activitybtn);
    }

    private void setListeners()
    {
        actbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(nearact.this,allactivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
//        map.addMarker(new MArkerOptions().position(new LatLng(0,0)).title("Marker"));
    }

//    @Override
//    protected void onDestroy(){

//    }

    @Override
    public void onLowMemory(){

    }
    
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

//    private class MArkerOptions {
//        public MarkerOptions position(LatLng latLng){
//        }
//    }
}
