package com.roger.joinme;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zyyoona7.wheel.WheelView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    private Button submitbtn;
    private EditText start, end;
    private int year, month, day; //選擇日期變數
    private int sHour, sMin, eHour, eMin;  //起訖時間
    public TextView activityTitle, peopleLimit, activityContent;
    private ImageView imgtest;
    public String userSelectLocation;
    public static final int ACTIVITY_FILE_CHOOSER = 1;
    private LatLng placelocation;
    private TimePickerView pTime;
    private Date endTime = new Date();
    private Date startTime = new Date();
    private Timestamp sts,ets;
    private WheelView<Integer> wheelView;
    public static String username;


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
        initTimePicker();
        wheelView = findViewById(R.id.wheelview);

        List<Integer> list = new ArrayList<>(1);
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        //设置数据
        wheelView.setData(list);
    }

    public void initPlace() {
        Places.initialize(getApplicationContext(), "AIzaSyAKuaxAND8zfIysSz1HdoNF88o1aK8ZIN4");
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
        start = (EditText) findViewById(R.id.sTime);
        end = (EditText) findViewById(R.id.eTime);
        activityTitle = (TextView) findViewById(R.id.editText6);
//        activityLocation = (TextView) findViewById(R.id.editText10);
//        peopleLimit = (TextView) findViewById(R.id.editText11);
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


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startTime);
                    pTime.setDate(calendar);
                    pTime.show(view);
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endTime);
                    pTime.setDate(calendar);
                    pTime.show(view);
                }
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
                System.out.print(wheelView.getSelectedItemData());
                if (activityTitle.getText().toString().equals("") || userSelectLocation.equals("") ) {
                    Toast.makeText(jo.this, "資料未填寫完成", Toast.LENGTH_LONG).show();
                } else {
                    if (sts.compareTo(ets) < 0) {
                        //初始化Places API
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                        final Map<String, Object> book = new HashMap<>();
                        book.put("title", activityTitle.getText().toString());
                        book.put("postContent", activityContent.getText().toString());
                        book.put("activityType", spinner.getSelectedItem().toString());
                        book.put("location", userSelectLocation); //先不上傳地址，轉成經緯度前會導致首頁報錯
                        //先切割字串再轉成geopoint格式
                        String[] tokens = getLocationFromAddress(userSelectLocation).toString().split(",|\\(|\\)");
                        book.put("geopoint", new GeoPoint(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
                        book.put("numberOfPeople", wheelView.getSelectedItemData());
                        book.put("startTime", sts);//之後討論下資料庫內的型別要直接用String還是時間戳記
                        book.put("endTime", ets);

                        //將主辦人放入揪團資料
                        db.collection("user")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if(document.getString("email").equals(MainActivity.useraccount)){
                                                    book.put("organizerID",document.getString("name"));
                                                }
                                            }
                                        }
                                    }
                                });

//                        for (Object key : book.keySet()) {
//                            System.out.println(key + " : " + book.get(key));
//                        }
                        //查看map內容
                        uploadImage();
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
                        Toast.makeText(jo.this, "活動建立成功", Toast.LENGTH_LONG).show();
                        submitbtn.setEnabled(false);
                        submitbtn.setText("報名成功");
                    }else{
                        Toast.makeText(jo.this, "活動起訖時間填寫錯誤", Toast.LENGTH_LONG).show();
                    }
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

    //地址轉經緯度method(很容易讀不到資料 要想辦法解決次數問題)
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

    private void initTimePicker() {

        pTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
//                System.out.println(date);
                //如果是開始時間的EditText
                if (v.getId() == R.id.eTime) {
                    endTime = date;
                    ets= new Timestamp(date);
                } else {
                    startTime = date;
                    sts= new Timestamp(date);
                }
                EditText editText = (EditText) v;
                editText.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isDialog(true)
                .build();


        Dialog mDialog = pTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改動畫樣式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部顯示
            }
        }
    }

    private String getTime(Date date) {//可根據需要自行擷取資料顯示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}