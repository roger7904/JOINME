package com.roger.joinme;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class signup extends AppCompatActivity {

    public Button signupbtn, deletebtn, favoritebtn;
    public TextView title;
    public ImageView activityPhoto;
    public TextView activityContent;
    final Map<String, Object> actbook = new HashMap<>();
    public String account;
    public Bitmap actImg;
    String imageUrl;
    String[] participant;


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
        db.collection("activity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("title").equals(home.activitytitle)) {
                                    Date snnippet = document.getTimestamp("startTime").toDate();
                                    SimpleDateFormat ft = new SimpleDateFormat(" yyyy-MM-dd hh :mm:ss ");
                                    title.setText(home.activitytitle);
                                    imageUrl = document.getString("imgUri");
                                    if (!imageUrl.equals("")) { //目前大部分活動的imguri為留空，故沒有的話就不替換了
                                        getBitmapFromUrl thread = new getBitmapFromUrl();
                                        thread.start();
                                        try {
                                            thread.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        activityPhoto.setImageBitmap(actImg);
                                    }

                                    activityContent.setText("時間：" + ft.format(snnippet) + "\n" + "地點：" + document.getString("location") + "\n" + "備註：" + document.getString("postContent") + "\n" + "發起人：" + document.getString("organizerID"));
                                    if (document.getString("organizerID") == home.useraccount) {
                                        deletebtn.setVisibility(View.GONE);
                                    }

                                }
                            }
                        }
                    }
                });

        db.collection("activity").document(home.activitytitle).collection("participant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   int i = 0;
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                       i++;
                                                   }
                                                   participant=new String[i];
                                               }
                                           }
                                       }
                );

        db.collection("activity").document(home.activitytitle).collection("participant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   int i = 0;
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                       participant[i]=document.getString("account");
                                                       i++;
                                                   }
                                               }
                                           }
                                       }
                );

        //MainActivity.useraccount;
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("email").equals(home.useraccount)) {
                                    account = document.getString("email");
                                    actbook.put("account", document.getString("email"));
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

    private void initData() {
    }

    private void initViews() {
        signupbtn = (Button) findViewById(R.id.signupbtn);
        title = (TextView) findViewById(R.id.title);
        activityPhoto = (ImageView) findViewById(R.id.activityphoto);
        activityContent = (TextView) findViewById(R.id.activityContent);
        deletebtn = (Button) findViewById(R.id.deletebtn);
        favoritebtn = (Button) findViewById(R.id.favoritebtn);
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
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                        for (String name : participant){
                            db.collection("activity").document(home.activitytitle).collection("participant").document(name).delete();
                        }
                        db.collection("activity").document(home.activitytitle).delete();
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
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("activity")
                        .document(home.activitytitle)
                        .collection("participant")
                        .document(account)
                        .set(actbook)
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
                signupbtn.setText("已報名");
                signupbtn.setEnabled(false);
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

}
