package com.roger.joinme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
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
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.*;
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
    private Button chatroom;
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
    private Timestamp sts, ets;
    public static String username;
    List<String> list;
    private Button limitBtn;
    private String picUrl;
    private Button button5;
    private Button sbtn, ebtn, submitimg,chooseRes;
    public String organizerID;
    public String uriString;
    public boolean imguploaded = false;
    public TextView nowRestriction;
    boolean[] flag_list= {false, false, false};

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


        list = new ArrayList<>(1);
        for (int i = 0; i < 100; i++) {
            list.add(Integer.toString(i));
        }
        //设置数据

        //將主辦人放入揪團資料
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(MainActivity.useraccount);
                                if (document.getString("email").equals(MainActivity.useraccount)) {
//                                    System.out.println(document.getString("name"));
                                    organizerID = document.getString("email");
//                                    System.out.println("one:"+organizerID);
                                    break;
                                }
                            }
                        }
                    }
                });
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
        chatroom = (Button) findViewById(R.id.button14);
        favorite = (ImageButton) findViewById(R.id.imgbtn_favorite);
        jo = (ImageButton) findViewById(R.id.imgbtn_jo);
        notice = (ImageButton) findViewById(R.id.imgbtn_notice);
        setting = (ImageButton) findViewById(R.id.imgbtn_setting);
        spinner = (Spinner) findViewById(R.id.activityType);
        submitimg = (Button) findViewById(R.id.submitimg);

        activityTitle = (TextView) findViewById(R.id.editText6);
//        activityLocation = (TextView) findViewById(R.id.editText10);
//        peopleLimit = (TextView) findViewById(R.id.editText11);
        activityContent = (TextView) findViewById(R.id.editText12);
        submitbtn = (Button) findViewById(R.id.button40);
        imgtest = (ImageView) findViewById(R.id.imageView26);
        imgtest.setClickable(true);
        limitBtn = (Button) findViewById(R.id.peopleLimit);
        sbtn = (Button) findViewById(R.id.sTime);
        ebtn = (Button) findViewById(R.id.eTime);
        button5 = (Button) findViewById(R.id.button5);
        nowRestriction=(TextView)findViewById(R.id.restriction);
        chooseRes=(Button)findViewById(R.id.chooseRestriction);


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

        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

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

        imgtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");      //開啟Pictures畫面Type設定為image
                intent.setAction(Intent.ACTION_GET_CONTENT);    //使用Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 1);      //取得相片後, 返回
                imguploaded = true;
                submitimg.setEnabled(true);

            }
        });

        submitimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imguploaded) {
                    if (activityTitle.getText().toString().equals("")) {
                        Toast.makeText(jo.this, "請先填寫標題", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadimg t1 = new uploadimg();
                        t1.start();
                        try {
                            t1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(jo.this, "封面上傳成功", Toast.LENGTH_SHORT).show();
                        submitimg.setEnabled(false);
                    }
                } else {
                    Toast.makeText(jo.this, "請先選擇圖片", Toast.LENGTH_SHORT).show();
                }

            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (activityTitle.getText().toString().equals("") || userSelectLocation.equals("") || button5.getText().toString().equals("") || sbtn.getText().toString().equals("") || ebtn.getText().toString().equals("") || limitBtn.getText().toString().equals("")) {
                    Toast.makeText(jo.this, "資料未填寫完成", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        sts = new Timestamp(stringToDate(true));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        ets = new Timestamp(stringToDate(false));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (sts.compareTo(ets) < 0) {
                        //初始化Places API

                        final Map<String, Object> book = new HashMap<>();
                        final Map<String, Object> ubook = new HashMap<>();
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();

                        book.put("title", activityTitle.getText().toString());
                        book.put("postContent", activityContent.getText().toString());
                        book.put("activityType", spinner.getSelectedItem().toString());
                        book.put("location", userSelectLocation); //先不上傳地址，轉成經緯度前會導致首頁報錯
                        //先切割字串再轉成geopoint格式
                        String[] tokens = getLocationFromAddress(userSelectLocation).toString().split(",|\\(|\\)");
                        book.put("geopoint", new GeoPoint(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
                        book.put("numberOfPeople", limitBtn.getText());
                        book.put("startTime", sts);//之後討論下資料庫內的型別要直接用String還是時間戳記
                        book.put("endTime", ets);
                        book.put("organizerID", organizerID);
                        book.put("imgUri", uriString);
                        book.put("onlyMale",flag_list[0]);
                        book.put("onlyFemale",flag_list[1]);
                        book.put("Ontime",flag_list[2]);
                        ubook.put("account", "0");

                        //查看map內容

                        db.collection("activity")
                                .document(activityTitle.getText().toString())
                                .set(book)
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
                        db.collection("activity")
                                .document(activityTitle.getText().toString())
                                .collection("participant")
                                .document("0")
                                .set(ubook)
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
                        Toast.makeText(jo.this, "活動建立成功", Toast.LENGTH_LONG).show();
                        submitbtn.setEnabled(false);
                        submitbtn.setText("報名成功");
                    } else {
                        Toast.makeText(jo.this, "活動起訖時間填寫錯誤", Toast.LENGTH_LONG).show();
                    }
                }
            }


        });

        chooseRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(2);
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

    class uploadimg extends Thread {
        public void run() {
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://joinme-6fe0a.appspot.com/");
            StorageReference StorageRef = storage.getReference();
            final StorageReference pRef = StorageRef.child(activityTitle.getText().toString());
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
                    pRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uriString = uri.toString();
                            System.out.println(uriString);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                    ;
                }
            });

        }

    }


    private String getTime(Date date) {//可根據需要自行擷取資料顯示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public void showDialog(View view) {
        final List<String> options1Items = new ArrayList<>();

        for (int i = 1; i <= 99; i++) {
            options1Items.add(Integer.toString(i));
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(jo.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                limitBtn.setText(options1Items.get(options1));
            }
        }).build();
        pvOptions.setPicker(options1Items, null, null);
        pvOptions.show();

    }

    public void showTimeDialog(final View view) {
        final List<String> optionsHour = new ArrayList<>();
        final List<String> optionsMin = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            optionsHour.add(Integer.toString(i));
        }

        optionsMin.add("00");
        for (int i = 10; i < 60; i += 10) {
            optionsMin.add(Integer.toString(i));
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(jo.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (view.getId() == R.id.sTime) {
//                    sbtn=(Button)findViewById(R.id.sTime);
                    sbtn.setText(optionsHour.get(options1) + ":" + optionsMin.get(option2));
                } else {
//                    ebtn=(Button)findViewById(R.id.eTime);
                    ebtn.setText(optionsHour.get(options1) + ":" + optionsMin.get(option2));
                }
            }
        }).build();
        pvOptions.setNPicker(optionsHour, optionsMin, null);
        pvOptions.setTitleText("選擇時間");
        pvOptions.show();

    }

    public void showDayDialog(final View view) {
        TimePickerView pvTime = new TimePickerBuilder(jo.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String var = Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
//                button5=(Button)findViewById(R.id.button5);
                button5.setText(var);
            }
        }).build();
        pvTime.show();
    }

    public Date stringToDate(boolean a) throws ParseException {
        Date date = new Date();
        String dateStr;
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (a == true) {
            dateStr = (String) button5.getText() + " " + (String) sbtn.getText() + ":00";
            date = sdf.parse(dateStr);
            System.out.println(date.toString());
        } else {
            dateStr = (String) button5.getText() + " " + (String) ebtn.getText() + ":00";
            date = sdf.parse(dateStr);
            System.out.println(date.toString());
        }
        return date;
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        switch (id) //判斷所傳入的ID，啟動相應的對話方塊
        {
            case 1:
                //自訂一個名稱為 content_layout 的介面資源檔
                final View content_layout = LayoutInflater.from(jo.this).inflate(R.layout.dialog_timepicker, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("選擇時間") //設定標題文字
                        .setView(content_layout) //設定內容外觀
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() { //設定確定按鈕
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取得 content_layout 介面資源檔中的元件
                                
                            }
                        });
                dialog = builder.create(); //建立對話方塊並存成 dialog
                break;
            case 2:
                String[] str_list={"限男","限女","逾時不候"};

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("請勾選") //設定標題文字
                        .setMultiChoiceItems(str_list, flag_list, new DialogInterface.OnMultiChoiceClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked)
                            {
                                // TODO Auto-generated method stub
                                flag_list[which]=true;
                            }
                        })
                        .setPositiveButton("確認", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // TODO Auto-generated method stub
                                String temp="";
                                for(int i=0; i<flag_list.length; i++)
                                {
                                    if(flag_list[i])
                                        temp = temp + str_list[i]+" ";
                                }
                                System.out.println(temp);
                                nowRestriction.setText("目前活動限制："+temp);
                            }
                        });
                dialog = builder2.create(); //建立對話方塊並存成 dialog
                break;
            default:
                break;
        }
        return dialog;
    }


}

