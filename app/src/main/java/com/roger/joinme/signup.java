package com.roger.joinme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class signup extends AppCompatActivity {

    public Button signupbtn ;
    public TextView title;
    public ImageView activityPhoto;
    public TextView activityContent;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);

        initViews();
        initData();
        setListeners();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //抓集合
        db.collection( "activity" )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete ( @NonNull Task< QuerySnapshot > task ) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("title").equals(home.activitytitle)){
                                    Date snnippet = document.getTimestamp("startTime").toDate();
                                    SimpleDateFormat ft = new SimpleDateFormat( " yyyy-MM-dd hh :mm:ss " );
                                    title.setText(home.activitytitle);
                                    activityContent.setText("時間："+ft.format(snnippet)+"\n"+"地點："+document.getString("location")+"\n"+"備註："+document.getString("postContent")+"\n"+"發起人："+document.getString("organizerID"));
                                }
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

    private void initData()
    {
    }

    private void initViews() {
        signupbtn = (Button)findViewById(R.id.signupbtn);
        title = (TextView)findViewById(R.id.title);
        activityPhoto = (ImageView)findViewById(R.id.activityphoto);
        activityContent = (TextView)findViewById(R.id.activityContent);
    }

    private void setListeners() {
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                final Map<String, Object> book = new HashMap<>();
                final Map<String, Object> actbook = new HashMap<>();
                //MainActivity.useraccount;
                db.collection("user")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task){
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.getString("email").equals(MainActivity.useraccount)){
                                            book.put("organizerID",document.getString("name"));
                                        }
                                    }
                                }
                            }
                        });

                db.collection("activity")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task){
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.getString("title").equals(home.activitytitle)){
                                            actbook.put("account",MainActivity.useraccount);
                                            db.collection("activity")
                                                    .document()
                                                    .collection("participant")
                                                    .add(actbook)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("TAG", "Book added");
                                                            } else {
                                                                Log.d("TAG", "Book added failed");
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }
                        });

                db.collection("activity")
                        .add(book)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Book added");
                                } else {
                                    Log.d("TAG", "Book added failed");
                                }
                            }
                        });



                signupbtn.setText("已報名");
                signupbtn.setEnabled(false);
            }
        });
    }

}
