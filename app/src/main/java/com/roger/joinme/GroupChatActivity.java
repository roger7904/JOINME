package com.roger.joinme;


import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GroupChatActivity extends AppCompatActivity
{
    private Toolbar mToolbar;
    private ImageButton SendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;

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
//        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
//        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);



        InitializeFields();


        GetUserInfo();


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SaveMessageInfoToDatabase();

                userMessageInput.setText("");

                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }



    @Override
    protected void onStart()
    {
        super.onStart();

//        GroupNameRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s)
//            {
//                if (dataSnapshot.exists())
//                {
//                    DisplayMessages(dataSnapshot);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s)
//            {
//                if (dataSnapshot.exists())
//                {
//                    DisplayMessages(dataSnapshot);
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        db.collection("chat").document(currentGroupName).collection("content")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        DisplayMessages(value);
                    }
                });

    }


    private void InitializeFields()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);
        displayTextMessages = (TextView) findViewById(R.id.group_chat_text_display);
        mScrollView = (ScrollView) findViewById(R.id.my_scroll_view);
    }



    private void GetUserInfo()
    {
//        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                if (dataSnapshot.exists())
//                {
//                    currentUserName = dataSnapshot.child("name").getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        db.collection("user").document(currentUserID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot snapshot,
                            @Nullable FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                currentUserName = snapshot.getString("name");
                Log.d("TAG", "Current data: " + snapshot.getData());
            } else {
                Log.d("TAG", "Current data: null");
            }
        }
    });
    }




    private void SaveMessageInfoToDatabase()
    {
        String message = userMessageInput.getText().toString();

        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "Please write message first...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());


//            HashMap<String, Object> groupMessageKey = new HashMap<>();
//            GroupNameRef.updateChildren(groupMessageKey);

//            GroupMessageKeyRef = GroupNameRef.child(messagekEY);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);
            db.collection("chat").document(currentGroupName).collection("content").document().set(messageInfoMap);

//            HashMap<String, String> Notification_type = new HashMap<>();
//            //chatNotificationMap.put("from", currentUserID);
//            Notification_type.put("type", "message_send");
//            db.collection("notification").document(currentUserID).set(Notification_type);
//            HashMap<String, String> receivermap = new HashMap<>();
//            db.collection("chat").document(currentGroupName).collection("participant").get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    receivermap.put("userID",document.getString("userID"));
//                                    db.collection("notification").document(currentUserID).collection("receiver").document()
//                                            .set(receivermap);
//
//                                }
//                            }
//                        }
//                    });

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
                                                            notifcationBody.put("title", "您有新的訊息");
                                                            notifcationBody.put("message", message);

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



        }
    }


    private void DisplayMessages(QuerySnapshot querySnapshot)
    {
        for (DocumentChange dc : querySnapshot.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    String chatDate = dc.getDocument().getString("date");
                    String chatMessage = dc.getDocument().getString("message");
                    String chatName = dc.getDocument().getString("name");
                    String chatTime = dc.getDocument().getString("time");

                    displayTextMessages.append(chatName + " :\n" + chatMessage + "\n" + chatTime + "     " + chatDate + "\n\n\n");
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
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

//        for (QueryDocumentSnapshot doc : querySnapshot) {
//            if (doc.getId()!= null) {
//                String chatDate = doc.getString("date");
//                String chatMessage = doc.getString("message");
//                String chatName = doc.getString("name");
//                String chatTime = doc.getString("time");
//
//                displayTextMessages.append(chatName + " :\n" + chatMessage + "\n" + chatTime + "     " + chatDate + "\n\n\n");
//
//                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        }

//        Iterator iterator = dataSnapshot.getChildren().iterator();
//
//        while(iterator.hasNext())
//        {
//            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
//            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
//            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
//            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();
//
//            displayTextMessages.append(chatName + " :\n" + chatMessage + "\n" + chatTime + "     " + chatDate + "\n\n\n");
//
//            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
//        }
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