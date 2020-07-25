package com.roger.joinme;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
{
    private String receiverUserID, senderUserID, Current_State;

    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button SendMessageRequestButton, DeclineMessageRequestButton;

//    private DatabaseReference UserRef, ChatRequestRef, ContactsRef, NotificationRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();

        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_image);
        userProfileName = (TextView) findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_status);
        SendMessageRequestButton = (Button) findViewById(R.id.send_message_request_button);
        DeclineMessageRequestButton = (Button) findViewById(R.id.decline_message_request_button);
        Current_State = "new";

        RetrieveUserInfo();
    }



    private void RetrieveUserInfo()
    {
        final DocumentReference docRef = db.collection("user").document(receiverUserID).collection("profile")
                .document(receiverUserID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists() && snapshot.contains("name") && snapshot.contains("image")) {
                    String userImage = snapshot.getString("image");
                    String userName = snapshot.getString("name");
                    String userstatus = snapshot.getString("status");

                    Picasso.get().load(userImage).placeholder(R.drawable.head).into(userProfileImage);
                    userProfileName.setText(userName);
                    userProfileStatus.setText(userstatus);


                    ManageChatRequests();
                } else if(snapshot != null && snapshot.exists() && snapshot.contains("name")) {
                    String userName = snapshot.getString("name");
                    String userstatus = snapshot.getString("status");

                    userProfileName.setText(userName);
                    userProfileStatus.setText(userstatus);


                    ManageChatRequests();
                }
            }
        });
    }

    private void ManageChatRequests()
    {
        final DocumentReference docRef = db.collection("add_friend_request").document(senderUserID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot.contains(receiverUserID))
                {
                    String request_type = snapshot.getString("request_type");

                    if (request_type.equals("sent"))
                    {
                        Current_State = "request_sent";
                        SendMessageRequestButton.setText("Cancel Add Request");
                    }
                    else if (request_type.equals("received"))
                    {
                        Current_State = "request_received";
                        SendMessageRequestButton.setText("Accept Add Request");

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
                }
                else
                {
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
                                } else {

                                }
                            } else {

                            }
                        }
                    });
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
                        db.collection("add_friend_request").document(receiverUserID).
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
                            db.collection("add_friend_request").document(receiverUserID).
                                    collection("friends").document(senderUserID).set(senderdata)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                DocumentReference docRef = db.collection("add_friend_request").document(senderUserID)
                                                        .collection("UserID").document(receiverUserID);

                                                Map<String,Object> updates = new HashMap<>();
                                                updates.put("request_type", FieldValue.delete());

                                                docRef.update(updates).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {

                                                            db.collection("add_friend_request").document(receiverUserID).
                                                                    collection("UserID").
                                                                    document(senderUserID).update(updates)
                                                                    .addOnCompleteListener(new OnCompleteListener() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task task) {
                                                                            if (task.isSuccessful()) {
                                                                                SendMessageRequestButton.setEnabled(true);
                                                                                Current_State = "friends";
                                                                                SendMessageRequestButton.setText("Remove this Friend");

                                                                                DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                                                DeclineMessageRequestButton.setEnabled(false);
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

    private void CancelAddRequest()
    {
        DocumentReference docRef = db.collection("add_friend_request").document(senderUserID)
                .collection("UserID").document(receiverUserID);

        Map<String,Object> updates = new HashMap<>();
        updates.put("request_type", FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            db.collection("add_friend_request").document(receiverUserID).collection("UserID").
                                    document(senderUserID).update(updates)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                SendMessageRequestButton.setEnabled(true);
                                                Current_State = "new";
                                                SendMessageRequestButton.setText("Add Friend");

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
                                                HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                chatNotificationMap.put("from", senderUserID);
                                                chatNotificationMap.put("type", "request");

                                                db.collection("notification").document(receiverUserID).set(chatNotificationMap)
                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    SendMessageRequestButton.setEnabled(true);
                                                                    Current_State = "request_sent";
                                                                    SendMessageRequestButton.setText("Cancel Add Request");
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