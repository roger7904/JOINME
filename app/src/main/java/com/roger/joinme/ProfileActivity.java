package com.roger.joinme;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
{
    private String receiverUserID, senderUserID, Current_State,currentUserID,currentUserName;

    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button SendMessageRequestButton, DeclineMessageRequestButton;
    public String fromWhichPage = "",UserID="";

//    private DatabaseReference UserRef, ChatRequestRef, ContactsRef, NotificationRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoxsFReA:APA91bFrtTvCQxgBDQMTB7MddpMquycE2wOqh4K4_-yHNC2KSxCW0exYbpzx62KmVMNfY8HoZz67HrSc_xbo9NeWPSB13LGBxmAJujI-n90hm3zYLKbZGkqgGo_GIrdFLvcKP77GE5yA";
    final private String contentType = "application/json";

    private Bitmap userImgbitmap;
    private StorageReference UserProfileImagesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        fromWhichPage = getIntent().getExtras().get("fromPersonal").toString();
        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();
        currentUserID = mAuth.getCurrentUser().getUid();

        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_image);
        userProfileName = (TextView) findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_status);
        SendMessageRequestButton = (Button) findViewById(R.id.send_message_request_button);
        DeclineMessageRequestButton = (Button) findViewById(R.id.decline_message_request_button);
        Current_State = "new";

        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        final DocumentReference docRef = db.collection("user").document(currentUserID).collection("profile")
                .document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {
                        currentUserName=snapshot.getString("name");
                    } else {

                    }
                }
            }
        });
        RetrieveUserInfo();
    }

    //鎖手機的返回鍵
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR) {
                event.startTracking();
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this, home.class);
                startActivity(intent);
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

    private void RetrieveUserInfo()
    {
        final DocumentReference docRef = db.collection("user").document(receiverUserID).collection("profile")
                .document(receiverUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists() && snapshot.contains("name") && snapshot.contains("image")) {
                        String userImage = snapshot.getString("image");
                        String userName = snapshot.getString("name");
                        String userstatus = snapshot.getString("status");
                        String userID2 = snapshot.getString("currentUserID");

//                        userProfileImage.setImageURI(Uri.fromFile(new File(userImage)));
                        UserProfileImagesRef.child(receiverUserID+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Glide.with(ProfileActivity.this)
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

//                        getBitmapFromUrl thread=new getBitmapFromUrl();
//                        thread.start();
//                        try {
//                            thread.join();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        userProfileImage.setImageBitmap(userImgbitmap);

                        userProfileName.setText(userName);
                        userProfileStatus.setText(userstatus);
                        userProfileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("visit_user_id", userID2);
                                myIntent.putExtras(bundle);
                                myIntent.setClass( ProfileActivity.this, personalpage.class);
                                startActivity(myIntent);
                            }
                        });
                        ManageChatRequests();
                    } else if(snapshot != null && snapshot.exists() && snapshot.contains("name")){
                        String userName = snapshot.getString("name");
                        String userstatus = snapshot.getString("status");
                        String userID2 = snapshot.getString("currentUserID");
                        Glide.with(ProfileActivity.this)
                                .load(R.drawable.head)
                                .circleCrop()
                                .into(userProfileImage);
                        userProfileName.setText(userName);
                        userProfileStatus.setText(userstatus);

                        userProfileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("visit_user_id", userID2);
                                myIntent.putExtras(bundle);
                                myIntent.setClass( ProfileActivity.this, personalpage.class);
                                startActivity(myIntent);
                            }
                        });

                        ManageChatRequests();
                    }

                } else {

                }
            }
        });
    }

//    public class getBitmapFromUrl extends Thread{
//        public void run(){
//            try
//            {
//                URL url = new URL(userImage);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                userImgbitmap = BitmapFactory.decodeStream(input);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

    private void ManageChatRequests()
    {
        final DocumentReference docRef = db.collection("add_friend_request").document(senderUserID).
                collection("UserID").document(receiverUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {
                        String request_type = snapshot.getString("request_type");

                        if (request_type.equals("sent"))
                        {
                            Current_State = "request_sent";
                            SendMessageRequestButton.setText("Cancel Add Request");
                            SendMessageRequestButton.setBackgroundColor(Color.RED);
                        }
                        else if (request_type.equals("received"))
                        {
                            Current_State = "request_received";
                            SendMessageRequestButton.setText("Accept Add Request");
                            SendMessageRequestButton.setBackgroundColor(Color.GREEN);
                            DeclineMessageRequestButton.setVisibility(View.VISIBLE);
                            DeclineMessageRequestButton.setEnabled(true);

                            DeclineMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    CancelAddRequest();
                                }
                            });
                        }


                        ManageChatRequests();
                    } else{
                        DocumentReference docIdRef = db.collection("user").document(senderUserID).
                                collection("friends").document(receiverUserID);
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Current_State = "friends";
                                        SendMessageRequestButton.setText("Remove this Friend");
                                        SendMessageRequestButton.setBackgroundColor(Color.RED);
                                    } else {

                                    }
                                } else {

                                }
                            }
                        });
                    }
                } else {

                }
            }
        });


        if (!senderUserID.equals(receiverUserID))
        {
            SendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    SendMessageRequestButton.setEnabled(false);

                    if (Current_State.equals("new"))
                    {
                        SendAddRequest();
                    }
                    if (Current_State.equals("request_sent"))
                    {
                        CancelAddRequest();
                    }
                    if (Current_State.equals("request_received"))
                    {
                        AcceptAddRequest();
                    }
                    if (Current_State.equals("friends"))
                    {
                        RemoveSpecificFriend();
                    }
                }
            });
        }
        else
        {
            SendMessageRequestButton.setVisibility(View.INVISIBLE);
        }
    }



    private void RemoveSpecificFriend()
    {
        db.collection("user").document(senderUserID).
                collection("friends").document(receiverUserID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("user").document(receiverUserID).
                                collection("friends").document(senderUserID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        SendMessageRequestButton.setEnabled(true);
                                        Current_State = "new";
                                        SendMessageRequestButton.setText("Add Friend");

                                        DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                        DeclineMessageRequestButton.setEnabled(false);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                });

    }



    private void AcceptAddRequest()
    {
        Map<String, Object> receiverdata = new HashMap<>();
        receiverdata.put("UserID", receiverUserID);
        Map<String, Object> senderdata = new HashMap<>();
        senderdata.put("UserID", senderUserID);
        db.collection("user").document(senderUserID).
            collection("friends").document(receiverUserID).set(receiverdata)
            .addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        db.collection("user").document(receiverUserID).
                                collection("friends").document(senderUserID).set(senderdata)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            DocumentReference docRef = db.collection("add_friend_request").document(senderUserID)
                                                    .collection("UserID").document(receiverUserID);

                                            Map<String,Object> updates = new HashMap<>();
                                            updates.put("request_type", FieldValue.delete());

                                            docRef.delete().addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {

                                                        db.collection("add_friend_request").document(receiverUserID).
                                                                collection("UserID").
                                                                document(senderUserID).delete()
                                                                .addOnCompleteListener(new OnCompleteListener() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task task) {
                                                                        if (task.isSuccessful()) {
                                                                            SendMessageRequestButton.setEnabled(true);
                                                                            Current_State = "friends";
                                                                            SendMessageRequestButton.setText("Remove this Friend");
                                                                            SendMessageRequestButton.setBackgroundColor(Color.RED);

                                                                            DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                                            DeclineMessageRequestButton.setEnabled(false);

                                                                            String saveCurrentTime, saveCurrentDate;

                                                                            Calendar calendar = Calendar.getInstance();

                                                                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                                            saveCurrentDate = currentDate.format(calendar.getTime());

                                                                            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                                                            saveCurrentTime = currentTime.format(calendar.getTime());

                                                                            Long tsLong = System.currentTimeMillis()/1000;
                                                                            String ts = tsLong.toString();

                                                                            HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                                            chatNotificationMap.put("from", senderUserID);
                                                                            chatNotificationMap.put("type", "accept");
                                                                            chatNotificationMap.put("time", saveCurrentTime);
                                                                            chatNotificationMap.put("date", saveCurrentDate);
                                                                            chatNotificationMap.put("millisecond", ts);

                                                                            db.collection("user").document(receiverUserID).
                                                                                    collection("notification").
                                                                                    document().
                                                                                    set(chatNotificationMap);

                                                                            db.collection("user").document(receiverUserID).
                                                                                    get().
                                                                                    addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                DocumentSnapshot document = task.getResult();
                                                                                                if (document.exists()) {
                                                                                                    String RECEIVER_DEVICE = document.getString("device_token");
                                                                                                    JSONObject notification = new JSONObject();
                                                                                                    JSONObject notifcationBody = new JSONObject();
                                                                                                    try {
                                                                                                        notifcationBody.put("title", "您有新的好友");
                                                                                                        notifcationBody.put("message", currentUserName+"接受了您的交友邀請");
                                                                                                        notification.put("to", RECEIVER_DEVICE);
                                                                                                        notification.put("data", notifcationBody);
                                                                                                    } catch (JSONException e) {
                                                                                                    }
                                                                                                    sendNotification(notification);
                                                                                                } else {
                                                                                                }
                                                                                            } else {
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                }
            });

        db.collection("message").document(senderUserID).
                collection("UserID").document(receiverUserID)
                .set(receiverdata)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            db.collection("message").document(receiverUserID).
                                    collection("UserID").document(senderUserID).set(senderdata)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void CancelAddRequest()
    {
        DocumentReference docRef = db.collection("add_friend_request").document(senderUserID)
                .collection("UserID").document(receiverUserID);

        Map<String,Object> updates = new HashMap<>();
        updates.put("request_type", FieldValue.delete());

        docRef.delete().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            db.collection("add_friend_request").document(receiverUserID).collection("UserID").
                                    document(senderUserID).delete()
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                SendMessageRequestButton.setEnabled(true);
                                                Current_State = "new";
                                                SendMessageRequestButton.setText("Add Friend");
                                                SendMessageRequestButton.setBackgroundColor(Color.GREEN);

                                                DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                DeclineMessageRequestButton.setEnabled(false);

                                            }
                                        }
                                    });
                        }
                    }
                });
    }




    private void SendAddRequest()
    {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("UserID", receiverUserID);
        dataMap.put("request_type", "sent");
        HashMap<String, String> dataMap2 = new HashMap<>();
        dataMap2.put("UserID", senderUserID);
        dataMap2.put("request_type", "received");
        db.collection("add_friend_request").document(senderUserID).collection("UserID").document(receiverUserID).set(dataMap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            db.collection("add_friend_request").document(receiverUserID).collection("UserID").document(senderUserID).set(dataMap2)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                String saveCurrentTime, saveCurrentDate;

                                                Calendar calendar = Calendar.getInstance();

                                                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                saveCurrentDate = currentDate.format(calendar.getTime());

                                                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                                saveCurrentTime = currentTime.format(calendar.getTime());

                                                Long tsLong = System.currentTimeMillis()/1000;
                                                String ts = tsLong.toString();

                                                HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                chatNotificationMap.put("from", senderUserID);
                                                chatNotificationMap.put("type", "request");
                                                chatNotificationMap.put("time", saveCurrentTime);
                                                chatNotificationMap.put("date", saveCurrentDate);
                                                chatNotificationMap.put("millisecond", ts);

                                                db.collection("user").document(receiverUserID).
                                                        collection("notification").
                                                        document().
                                                        set(chatNotificationMap)
                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    SendMessageRequestButton.setEnabled(true);
                                                                    Current_State = "request_sent";
                                                                    SendMessageRequestButton.setText("Cancel Add Request");
                                                                    SendMessageRequestButton.setBackgroundColor(Color.RED);
                                                                }
                                                            }
                                                        });
                                                db.collection("user").document(receiverUserID).
                                                        get().
                                                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                String RECEIVER_DEVICE = document.getString("device_token");
                                                                JSONObject notification = new JSONObject();
                                                                JSONObject notifcationBody = new JSONObject();
                                                                try {
                                                                    notifcationBody.put("title", "您有新的好友邀請");
                                                                    notifcationBody.put("message", currentUserName+"對您傳送了交友邀請");
                                                                    notification.put("to", RECEIVER_DEVICE);
                                                                    notification.put("data", notifcationBody);
                                                                } catch (JSONException e) {
                                                                }
                                                                sendNotification(notification);
                                                            } else {
                                                            }
                                                        } else {
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    }
                });



    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}