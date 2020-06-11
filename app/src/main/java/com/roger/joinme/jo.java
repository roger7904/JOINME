package com.roger.joinme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.internal.PlatformServiceClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class jo extends AppCompatActivity {
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
    private AppBarConfiguration mAppBarConfiguration;
    private Spinner spinner;
    private Button startBtn, endBtn, dateBtn, submitbtn;
    private int year, month, day; //選擇日期變數
    private int sHour, sMin, eHour, eMin;  //起訖時間
    public TextView activityTitle, peopleLimit, activityContent;
    private ImageView imgtest;
    public String userSelectLocation;
    public static final int ACTIVITY_FILE_CHOOSER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createact);
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

        initPlace();
        initViews();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{"商家優惠", "球類", "限時", "KTV", "其他"});
        setListeners();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void initPlace(){
        Places.initialize(getApplicationContext(),"AIzaSyAKuaxAND8zfIysSz1HdoNF88o1aK8ZIN4");
        PlacesClient placesClient = Places.createClient(jo.this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                userSelectLocation = place.getName();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });
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

    private void initViews() {
        user = (Button) findViewById(R.id.btn_user);
        homepage = (Button) findViewById(R.id.btn_to_homepage);
        selfpage = (Button) findViewById(R.id.btn_to_selfpage);
        activitypage = (Button) findViewById(R.id.btn_to_jo);
        friendpage = (Button) findViewById(R.id.btn_to_notice);
        logout = (Button) findViewById(R.id.btn_logout);
        chatroom = (ImageButton) findViewById(R.id.imgbtn_chatroom);
        favorite = (ImageButton) findViewById(R.id.imgbtn_favorite);
        jo = (ImageButton) findViewById(R.id.imgbtn_jo);
        notice = (ImageButton) findViewById(R.id.imgbtn_notice);
        setting = (ImageButton) findViewById(R.id.imgbtn_setting);
        spinner = (Spinner) findViewById(R.id.activityType);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        endBtn = (Button) findViewById(R.id.endBtn);
        startBtn = (Button) findViewById(R.id.startBtn);
        activityTitle = (TextView) findViewById(R.id.editText6);
//        activityLocation = (TextView) findViewById(R.id.editText10);
        peopleLimit = (TextView) findViewById(R.id.editText11);
        activityContent = (TextView) findViewById(R.id.editText12);
        submitbtn = (Button) findViewById(R.id.button40);
        imgtest = (ImageView) findViewById(R.id.imageView26);
        imgtest.setClickable(true);
    }

    private void initData() {
    }

    private void setListeners() {
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, informationedit.class);
                startActivity(intent);
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, homepage.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, MainActivity.class);
                startActivity(intent);
            }
        });

        activitypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, allactivity.class);
                startActivity(intent);
            }
        });

        selfpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(jo.this, selfpage.class);
                startActivity(intent);
            }
        });

//        chatroom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, chatroom.class);
//                startActivity(intent);
//            }
//        });

//        friendpage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, friend.class);
//                startActivity(intent);
//            }
//        });

//        favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, favorite.class);
//                startActivity(intent);
//            }
//        });


//        jo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, jo.class);
//                startActivity(intent);
//            }
//        });

//        notice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, notice.class);
//                startActivity(intent);
//            }
//        });
//
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(jo.this, setting.class);
//                startActivity(intent);
//            }
//        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(jo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        dateBtn.setText(setDateFormat(year, month, day));
                    }

                }, year, month, day).show();
            }

        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                sHour = c.get(Calendar.HOUR_OF_DAY);
                sMin = c.get(Calendar.MINUTE);
                new TimePickerDialog(jo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Min) {
                        startBtn.setText(setTimeFormat(Hour, Min));
                        sHour = Hour;
                        sMin = Min;
//                        System.out.println(Hour+":"+Min);
                    }
                }, sHour, sMin, false).show();

            }

        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                eHour = c.get(Calendar.HOUR_OF_DAY);
                eMin = c.get(Calendar.MINUTE);
                new TimePickerDialog(jo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Min) {
                        endBtn.setText(setTimeFormat(Hour, Min));
                        eHour = Hour;
                        eMin = Min;
                    }
                }, eHour, eMin, false).show();
            }

        });

        imgtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");      //開啟Pictures畫面Type設定為image
                intent.setAction(Intent.ACTION_GET_CONTENT);    //使用Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 1);      //取得相片後, 返回
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityTitle.getText().toString().equals("") || userSelectLocation.equals("") || peopleLimit.getText().toString().equals("") || setTimeFormat(sHour, sMin).equals("0:0") || setTimeFormat(eHour, eMin).equals("0:0") || setDateFormat(year, month, day).equals("0-1-0")) {
                    Toast.makeText(jo.this, "資料未填寫完成", Toast.LENGTH_LONG).show();
                } else {
                    //初始化Places API


                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> book = new HashMap<>();
                    book.put("activityTitle", activityTitle.getText().toString());
                    book.put("postContent", activityContent.getText().toString());
                    book.put("activityType", spinner.getSelectedItem().toString());
                    book.put("location",userSelectLocation); //先不上傳地址，轉成經緯度前會導致首頁報錯
//                    book.put("geopoint", GeoPoint(getLocationFromAddress(userSelectLocation).getClass("Lat")));
                    book.put("numberOfPeople", peopleLimit.getText().toString());
                    book.put("startTime", setTimeFormat(sHour, sMin));//之後討論下資料庫內的型別要直接用String還是時間戳記
                    book.put("endTime", setTimeFormat(eHour, eMin));
                    book.put("date", setDateFormat(year, month, day));
                    for (Object key : book.keySet()) {
                        System.out.println(key + " : " + book.get(key));
                    }
                    //查看map內容
                    uploadImage();
                    db.collection("activity")
                            .add(book)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>(){
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task){
                                    if(task.isSuccessful()){
                                        Log.d("TAG","Book added");
                                    }else{
                                        Log.d("TAG","Book added failed");
                                    }
                                }
                            });
                    Toast.makeText(jo.this, "活動建立成功", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* 當使用者按下確定後 */
        //這個用法沒指定requestCode時會和place api牴觸(引用api時會用到這個方法)，檢查完後發現圖片抓取用的requestcode是1所以用變數存再判斷
        if (requestCode == ACTIVITY_FILE_CHOOSER) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();                       //取得圖檔的路徑
                Log.e("uri", uri.toString());                   //寫log
                ContentResolver cr = this.getContentResolver(); //抽象資料的接口

                try {
                    /* 由抽象資料接口轉換圖檔路徑為Bitmap */
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    imgtest.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    private String setDateFormat(int year, int monthOfYear, int dayOfMonth) {
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }

    private String setTimeFormat(int hr, int min) {
        return String.valueOf(hr) + ":" + String.valueOf(min);
    }

    private void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://joinme-6fe0a.appspot.com/");
        StorageReference StorageRef = storage.getReference();
        StorageReference pRef = StorageRef.child(activityTitle.getText().toString());
        imgtest.setDrawingCacheEnabled(true);
        imgtest.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgtest.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = pRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }
}




