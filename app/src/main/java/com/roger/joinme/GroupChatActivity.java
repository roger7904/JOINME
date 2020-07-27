package com.roger.joinme;


import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupChatActivity extends AppCompatActivity
{

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;


    private ImageButton SendMessageButton;
    private EditText userMessageInput;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoxsFReA:APA91bFrtTvCQxgBDQMTB7MddpMquycE2wOqh4K4_-yHNC2KSxCW0exYbpzx62KmVMNfY8HoZz67HrSc_xbo9NeWPSB13LGBxmAJujI-n90hm3zYLKbZGkqgGo_GIrdFLvcKP77GE5yA";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    private String RECEIVER_DEVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);



        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupChatActivity.this, currentGroupName, Toast.LENGTH_SHORT).show();


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db=FirebaseFirestore.getInstance();

        InitializeFields();


        GetUserInfo();


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendMessage();
            }
        });

    }



    protected void onStart()
    {
        super.onStart();

        db.collection("chat")
                .document(currentGroupName)
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
                                    String to=currentGroupName;
                                    String time=dc.getDocument().getString("time");
                                    String date=dc.getDocument().getString("date");
                                    String name=currentUserName;


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


    private void InitializeFields()
    {
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);

        messageAdapter = new MessageAdapter(this,messagesList);
        userMessagesList = (RecyclerView) findViewById(R.id.private_messages_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
    }



    private void GetUserInfo()
    {
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
    }

    private void SendMessage()
    {
        String messageText = userMessageInput.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {

        }
        else
        {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", currentUserID);
            messageTextBody.put("to", currentGroupName);
            //messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", currentTime);
            messageTextBody.put("date", currentDate);
            messageTextBody.put("millisecond", ts);


            db.collection("chat").document(currentGroupName).collection("content")
                    .document()
                    .set(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        db.collection("chat").document(currentGroupName).collection("participant").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String participantID = document.getString("userID");
                                        if(!participantID.equals(currentUserID)) {
                                            db.collection("user").document(participantID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {

                                                            RECEIVER_DEVICE = document.getString("device_token");
                                                            JSONObject notification = new JSONObject();
                                                            JSONObject notifcationBody = new JSONObject();
                                                            try {
                                                                notifcationBody.put("title", "您有新的群組訊息");
                                                                notifcationBody.put("message", currentGroupName+": "+messageText);

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
                                        }

                                    }
                                }
                            }
                        });
                    } else {

                    }
                    userMessageInput.setText("");
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
                        Toast.makeText(GroupChatActivity.this, "Request error", Toast.LENGTH_LONG).show();
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