package com.roger.joinme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class testsetting extends AppCompatActivity
{
    private Button UpdateAccountSettings;
    private EditText userName, userStatus,userphone;
    private Button userage;
    private ImageView userProfileImage;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Spinner usergender;

    private static final int GalleryPick = 1;
    private StorageReference UserProfileImagesRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testsetting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(testsetting.this, home.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
//        RootRef = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        InitializeFields();

//        userName.setVisibility(View.INVISIBLE);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{"男", "女"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usergender.setAdapter(adapter);

        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UpdateSettings();
            }
        });

        RetrieveUserInfo();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });


    }

    private void InitializeFields()
    {
        UpdateAccountSettings = (Button) findViewById(R.id.update_settings_button);
        userage = (Button)findViewById(R.id.button5);
        userName = (EditText) findViewById(R.id.set_user_name);
        userStatus = (EditText) findViewById(R.id.set_profile_status);
        userProfileImage = (ImageView) findViewById(R.id.set_profile_image);
        loadingBar = new ProgressDialog(this);
        usergender = (Spinner) findViewById(R.id.gender);
        userphone = (EditText) findViewById(R.id.phone);
    }

    //鎖手機的返回鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
                event.startTracking();
            }else{
                onBackPressed();
            }
        }
        return false;
    }

    @Override
    public  boolean onKeyUp(int keyCode, KeyEvent event){
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                loadingBar.setTitle("設定頭像");
                loadingBar.setMessage("頭像正在設定...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImagesRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(testsetting.this, "頭像上傳成功...", Toast.LENGTH_SHORT).show();

                            final String downloaedUrl = task.getResult().getUploadSessionUri().toString();
                            Map<String, Object> imgdata = new HashMap<>();
                            imgdata.put("image",downloaedUrl);
                            db.collection("user").document(currentUserID).collection("profile")
                                    .document(currentUserID).set(imgdata,SetOptions.merge());
                            RetrieveUserInfo();
                            loadingBar.dismiss();
                            Intent settingsIntent = new Intent(testsetting.this, testsetting.class);
                            startActivity(settingsIntent);
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(testsetting.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }
    }

    public void showDayDialog(final View view) {
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
        TimePickerView pvTime = new TimePickerBuilder(testsetting.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String age = "";
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String var = Integer.toString(cal.get(Calendar.MONTH) + 1) + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
                Time t=new Time("GMT+8");
                t.setToNow();
                int year = t.year;
                int month = t.month;
                int day = t.monthDay;
                String currentDate = String.valueOf(month) + String.valueOf(day);
                if(Integer.valueOf(var)>=Integer.valueOf(currentDate)){
                    age = String.valueOf(year - cal.get(Calendar.YEAR));
                }else{
                    age = String.valueOf(year - cal.get(Calendar.YEAR) - 1);
                }
                if(view.getId()==R.id.button5){
                    if(Integer.valueOf(age) >=7 || Integer.valueOf(age) <= 117){
                        userage.setText(age);
                    }else{
                        if(Integer.valueOf(age) < 7){
                            Toast.makeText(testsetting.this, "年齡不可低於7歲", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(testsetting.this, "年齡不可高於117歲", Toast.LENGTH_SHORT).show();
                        }
                        age = "";
                    }
                }else{
                }
            }
        }).build();
        pvTime.show();
    }

    private void UpdateSettings()
    {
        String setUserName = userName.getText().toString();
        String setStatus = userStatus.getText().toString();
        String gender = usergender.getSelectedItem().toString();
        String age = userage.getText().toString();
        String phone = userphone.getText().toString();

        if(setUserName.equals("") || setStatus.equals("") || gender.equals("") || age.equals("") || phone.equals("")){
            if (TextUtils.isEmpty(setUserName))
            {
                Toast.makeText(this, "請填寫姓名", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(setStatus))
            {
                Toast.makeText(this, "請填寫狀態", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(gender))
            {
                Toast.makeText(this, "請填寫性別", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(age))
            {
                Toast.makeText(this, "請填寫年齡", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(phone))
            {
                Toast.makeText(this, "請填寫手機", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("currentUserID", currentUserID);
            profileMap.put("name", setUserName);
            profileMap.put("status", setStatus);
            profileMap.put("gender", gender);
            profileMap.put("age", age);
            profileMap.put("phone", phone);
            db.collection("user")
                    .document(currentUserID)
                    .collection("profile")
                    .document(currentUserID)
                    .set(profileMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            SendUserToMainActivity();
                            Toast.makeText(testsetting.this, "個人資料修改成功...", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
    }



    private void RetrieveUserInfo()
    {
        final DocumentReference docRef = db.collection("user").document(currentUserID).collection("profile")
                .document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists() && snapshot.contains("name") && snapshot.contains("image")) {
                        String retrieveUserName = snapshot.getString("name");
                        String retrievesStatus = snapshot.getString("status");
                        String gender = snapshot.getString("gender");
                        String age = snapshot.getString("age");
                        String phone = snapshot.getString("phone");

                        userName.setText(retrieveUserName);
                        userStatus.setText(retrievesStatus);
                        for(int i= 0; i < usergender.getAdapter().getCount(); i++)
                        {
                            if(usergender.getAdapter().getItem(i).toString().contains(gender))
                            {
                                usergender.setSelection(i);
                            }
                        }
                        userage.setText(age);
                        userphone.setText(phone);

                        UserProfileImagesRef.child(currentUserID+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Glide.with(testsetting.this)
                                        .load(uri)
                                        .circleCrop()
                                        .into(userProfileImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        Log.d("TAG", "source" + " data: " + snapshot.getData());
                    } else if (snapshot != null && snapshot.exists() && snapshot.contains("name")) {
                        String retrieveUserName = snapshot.getString("name");
                        String retrievesStatus = snapshot.getString("status");
                        String gender = snapshot.getString("gender");
                        String age = snapshot.getString("age");
                        String phone = snapshot.getString("phone");

                        for(int i= 0; i < usergender.getAdapter().getCount(); i++)
                        {
                            if(usergender.getAdapter().getItem(i).toString().contains(gender))
                            {
                                usergender.setSelection(i);
                            }
                        }
                        userage.setText(age);
                        userphone.setText(phone);

                        Glide.with(testsetting.this)
                                .load(R.drawable.head)
                                .circleCrop()
                                .into(userProfileImage);
                        userName.setText(retrieveUserName);
                        userStatus.setText(retrievesStatus);
                        Log.d("TAG", "source" + " data: null");
                    } else {
                        Glide.with(testsetting.this)
                                .load(R.drawable.head)
                                .circleCrop()
                                .into(userProfileImage);
                        userName.setVisibility(View.VISIBLE);
                        Toast.makeText(testsetting.this, "Please set & update your profile information...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(testsetting.this, home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}