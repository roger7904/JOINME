package com.roger.joinme;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
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
    public ImageView activityPhoto;
    public TextView title;
    public String name,id,activityname;
    private String activitytitle,organizerID,activityType,organizerName;
    public Uri image;
    private String messageReceiverImage;
    private FirebaseStorage firebaseStorage;
    private ImageView actImage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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
        activitytitle = getIntent().getExtras().get("activitytitle").toString();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        initViews();
        initData();
        setListeners();

        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

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
                                                       Boolean haveImg = document.getBoolean("img");
                                                       SimpleDateFormat ft = new SimpleDateFormat(" yyyy-MM-dd HH :mm:ss ");
                                                       title.setText(activitytitle);
                                                       organizerID = document.getString("organizerID");
                                                       if (haveImg) {
                                                           StorageReference img = firebaseStorage.getReference();
                                                           img.child(activitytitle).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                               @Override
                                                               public void onSuccess(Uri uri) {
                                                                   Glide.with(personalpage.this).load(uri).into(activityPhoto);

                                                               }
                                                           });
                                                       }
                                                   }
                                               }
                                           }
                                       });

        messageReceiverImage = getIntent().getExtras().get("visit_image").toString();
        Glide.with(this)
                .load(messageReceiverImage)
                .circleCrop()
                .into(actImage);
    }

    private void initData() {
    }

    private void initViews() {
        chatView = (Button) findViewById(R.id.chatView);
        selfView = (Button) findViewById(R.id.selfView);
        title = (TextView) findViewById(R.id.title);
        activityPhoto = (ImageView) findViewById(R.id.activityphoto);
    }

    private void setListeners() {

    }
}
