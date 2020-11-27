package com.roger.joinme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.core.OrderBy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity
{
    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;

    private TextView userName, userLastSeen;
    private ImageView userImage;

    private Toolbar ChatToolBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ImageButton SendMessageButton, SendFilesButton;
    private EditText MessageInputText;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;


    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoxsFReA:APA91bFrtTvCQxgBDQMTB7MddpMquycE2wOqh4K4_-yHNC2KSxCW0exYbpzx62KmVMNfY8HoZz67HrSc_xbo9NeWPSB13LGBxmAJujI-n90hm3zYLKbZGkqgGo_GIrdFLvcKP77GE5yA";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    private String RECEIVER_DEVICE;

    private String saveCurrentTime, saveCurrentDate,currentUserName,saveCurrentTime2,saveCurrentDate2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();
        db=FirebaseFirestore.getInstance();

        messageReceiverID = getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().get("visit_user_name").toString();
        messageReceiverImage = getIntent().getExtras().get("visit_image").toString();


        IntializeControllers();

        final DocumentReference docRef = db.collection("user").document(messageSenderID).collection("profile")
                .document(messageSenderID);
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


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendMessage();
            }
        });

    }

//    @Override
//    public  boolean onKeyUp(int keyCode, KeyEvent event){
//        return super.onKeyUp(keyCode, event);
//    }

    private void IntializeControllers()
    {
        ChatToolBar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(ChatToolBar);
        getSupportActionBar().setTitle(messageReceiverName);
        ChatToolBar.setTitleTextColor(Color.BLACK);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_btn);
        SendFilesButton = (ImageButton) findViewById(R.id.send_files_btn);
        MessageInputText = (EditText) findViewById(R.id.input_message);
        messageAdapter = new MessageAdapter(this,messagesList);
        userMessagesList = (RecyclerView) findViewById(R.id.private_messages_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
    }

    //鎖手機的返回鍵
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
//                event.startTracking();
//                Intent intent = new Intent();
//                intent.setClass(ChatActivity.this, testmain.class);
//                startActivity(intent);
//            }else{
//                onBackPressed();
//            }
//        }
//        return false;
//    }

    @Override
    protected void onStart()
    {
        super.onStart();
        db.collection("message")
                .document(messageSenderID)
                .collection("UserID")
                .document(messageReceiverID)
                .collection("content")
                .orderBy("millisecond",Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    String from=dc.getDocument().getString("from");
                                    String message=dc.getDocument().getString("message");
                                    String type=dc.getDocument().getString("type");
                                    String to=dc.getDocument().getString("to");
                                    String time=dc.getDocument().getString("time");
                                    String date=dc.getDocument().getString("date");
                                    String name=dc.getDocument().getString("name");

                                    messagesList.add(new Messages(from,message,type,to,time,date,name));
                                    messageAdapter.notifyDataSetChanged();
                                    userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
                                    Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Message.class));
                                    break;
                                case MODIFIED:
                                    Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Message.class));
                                    break;
                                case REMOVED:
                                    Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Message.class));
                                    break;
                            }
                        }
                    }
                });
    }

    private void SendMessage()
    {
        String messageText = MessageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {

        }
        else
        {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            System.out.print(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            SimpleDateFormat currentDate2 = new SimpleDateFormat("MM/dd");
            saveCurrentDate2 = currentDate2.format(calendar.getTime());

            SimpleDateFormat currentTime2 = new SimpleDateFormat("hh:mm 79a");
            saveCurrentTime2 = currentTime2.format(calendar.getTime());

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderID);
            messageTextBody.put("to", messageReceiverID);
            //messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);
            messageTextBody.put("millisecond", ts);

            Map newcontent = new HashMap();
            newcontent.put("newestcontent", messageText);
            newcontent.put("newestmillisecond", ts);
            newcontent.put("time", saveCurrentTime2);
            newcontent.put("date", saveCurrentDate2);

            DocumentReference docRef = db.collection("message").document("messageSenderID")
                    .collection("UserID")
                    .document(messageReceiverID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists() && document.contains("contentcount")) {
                            Integer count;
                            count=document.getLong("contentcount").intValue();
                            Map<String, Number> contentcount = new HashMap<>();
                            contentcount.put("contentcount",count+1);

                            db.collection("message").document(messageSenderID).collection("UserID")
                                    .document(messageReceiverID).set(newcontent,SetOptions.merge());
                            db.collection("message").document(messageReceiverID).collection("UserID")
                                    .document(messageSenderID).set(newcontent,SetOptions.merge());
                            db.collection("message").document(messageReceiverID).collection("UserID")
                                    .document(messageSenderID).set(contentcount,SetOptions.merge());

                            db.collection("message").document(messageSenderID).collection("UserID")
                                    .document(messageReceiverID).collection("content").document()
                                    .set(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        db.collection("message").document(messageReceiverID).collection("UserID")
                                                .document(messageSenderID).collection("content").document()
                                                .set(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    db.collection("user").document(messageReceiverID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {

                                                                    RECEIVER_DEVICE = document.getString("device_token");
                                                                    JSONObject notification = new JSONObject();
                                                                    JSONObject notifcationBody = new JSONObject();
                                                                    try {
                                                                        notifcationBody.put("title", "您有新的訊息");
                                                                        notifcationBody.put("message", currentUserName+": "+messageText);

                                                                        notification.put("to", RECEIVER_DEVICE);
                                                                        notification.put("data", notifcationBody);
                                                                    } catch (JSONException e) {
                                                                        Log.e(TAG, "onCreate: " + e.getMessage());
                                                                    }
                                                                    sendNotification(notification);
                                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                                } else {
                                                                    Log.d(TAG, "No such document");
                                                                }
                                                            } else {
                                                                Log.d(TAG, "get failed with ", task.getException());
                                                            }
                                                        }
                                                    });
                                                } else {

                                                }
                                            }
                                        });
                                    } else {

                                    }
                                    MessageInputText.setText("");
                                }
                            });
                        } else {
                            Integer count;
                            count=0;
                            Map contentcount = new HashMap();
                            contentcount.put("contentcount",count+1);

                            db.collection("message").document(messageSenderID).collection("UserID")
                                    .document(messageReceiverID).set(newcontent,SetOptions.merge());
                            db.collection("message").document(messageReceiverID).collection("UserID")
                                    .document(messageSenderID).set(newcontent,SetOptions.merge());
                            db.collection("message").document(messageSenderID).collection("UserID")
                                    .document(messageReceiverID).set(contentcount,SetOptions.merge());
                            db.collection("message").document(messageSenderID).collection("UserID")
                                    .document(messageReceiverID).collection("content").document()
                                    .set(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        db.collection("message").document(messageReceiverID).collection("UserID")
                                                .document(messageSenderID).collection("content").document()
                                                .set(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    db.collection("user").document(messageReceiverID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {

                                                                    RECEIVER_DEVICE = document.getString("device_token");
                                                                    JSONObject notification = new JSONObject();
                                                                    JSONObject notifcationBody = new JSONObject();
                                                                    try {
                                                                        notifcationBody.put("title", "您有新的訊息");
                                                                        notifcationBody.put("message", currentUserName+": "+messageText);

                                                                        notification.put("to", RECEIVER_DEVICE);
                                                                        notification.put("data", notifcationBody);
                                                                    } catch (JSONException e) {
                                                                        Log.e(TAG, "onCreate: " + e.getMessage());
                                                                    }
                                                                    sendNotification(notification);
                                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                                } else {
                                                                    Log.d(TAG, "No such document");
                                                                }
                                                            } else {
                                                                Log.d(TAG, "get failed with ", task.getException());
                                                            }
                                                        }
                                                    });
                                                } else {

                                                }
                                            }
                                        });
                                    } else {

                                    }
                                    MessageInputText.setText("");
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
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