package com.roger.joinme;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupChatActivity extends AppCompatActivity
{
    private Toolbar mToolbar;
    private ImageButton SendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, GroupNameRef, GroupMessageKeyRef;
    private FirebaseFirestore db;

    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;


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


//        SendMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                SaveMessageInfoToDatabase();
//
//                userMessageInput.setText("");
//
//                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        });
    }



//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//
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
//    }


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




//    private void SaveMessageInfoToDatabase()
//    {
//        String message = userMessageInput.getText().toString();
//        String messagekEY = GroupNameRef.push().getKey();
//
//        if (TextUtils.isEmpty(message))
//        {
//            Toast.makeText(this, "Please write message first...", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Calendar calForDate = Calendar.getInstance();
//            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
//            currentDate = currentDateFormat.format(calForDate.getTime());
//
//            Calendar calForTime = Calendar.getInstance();
//            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
//            currentTime = currentTimeFormat.format(calForTime.getTime());
//
//
//            HashMap<String, Object> groupMessageKey = new HashMap<>();
//            GroupNameRef.updateChildren(groupMessageKey);
//
//            GroupMessageKeyRef = GroupNameRef.child(messagekEY);
//
//            HashMap<String, Object> messageInfoMap = new HashMap<>();
//            messageInfoMap.put("name", currentUserName);
//            messageInfoMap.put("message", message);
//            messageInfoMap.put("date", currentDate);
//            messageInfoMap.put("time", currentTime);
//            GroupMessageKeyRef.updateChildren(messageInfoMap);
//        }
//    }
//
//
//
//    private void DisplayMessages(DataSnapshot dataSnapshot)
//    {
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
//    }
}