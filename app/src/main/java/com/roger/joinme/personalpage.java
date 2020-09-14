package com.roger.joinme;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class personalpage extends AppCompatActivity {
    private Button chatView;
    private Button selfView;
    public String name,id,activityname;
    public Uri image;

    public personalpage()
    {

    }

    public personalpage(String name,  Uri image,String id,String activityname) {
        this.name = name;
        this.image = image;
        this.id=id;
        this.activityname=activityname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalpage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        initViews();
        initData();
        setListeners();

    }

    private void initData() {
    }

    private void initViews() {
        chatView = (Button) findViewById(R.id.chatView);
        selfView = (Button) findViewById(R.id.selfView);
    }

    private void setListeners() {

    }
}
