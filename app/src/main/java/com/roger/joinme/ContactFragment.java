package com.roger.joinme;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private View contactFragmentView;
    private List<chatroom> chatroomList;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private chatroomAdapter chatroomadapter;
    private StorageReference UserProfileImagesRef;

    public ContactFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactFragmentView=inflater.inflate(R.layout.fragment_contact, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        chatroomList = new ArrayList<>();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        initView();
        RetrieveAndDisplayContact();

        // Inflate the layout for this fragment
        return contactFragmentView;
    }

    private void RetrieveAndDisplayContact() {
        db.collection("message")
                .document(currentUserID)
                .collection("UserID")
                .orderBy("newestmillisecond", Query.Direction.DESCENDING)
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
                                    String name=dc.getDocument().getString("from");
                                    String newestcontent=dc.getDocument().getString("newestcontent");
                                    String id=dc.getDocument().getId();
                                    String time=dc.getDocument().getString("newestmillisecond");
                                    String contentcount=dc.getDocument().getString("contentcount");

                                    UserProfileImagesRef.child(id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Got the download URL for 'users/me/profile.png'
                                            chatroomList.add(new chatroom(name,newestcontent,uri,id,time,contentcount,"contact"));
                                            chatroomadapter.notifyDataSetChanged();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                            UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    // Got the download URL for 'users/me/profile.png'
                                                    chatroomList.add(new chatroom(name,newestcontent,uri,id,time,contentcount,"contact"));
                                                    chatroomadapter.notifyDataSetChanged();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle any errors
                                                }
                                            });
                                        }
                                    });

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
//        db.collection("me").document(currentUserID).collection("friends")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                db.collection("user").document(document.getId()).collection("profile")
//                                        .get()
//                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                if (task.isSuccessful()) {
//                                                    for (QueryDocumentSnapshot documentt : task.getResult()) {
//                                                        if (documentt.contains("name") && documentt.contains("image")) {
//                                                            String name = documentt.getString("name");
//                                                            String status = documentt.getString("status");
//                                                            String id = documentt.getString("currentUserID");
//                                                            UserProfileImagesRef.child(id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                @Override
//                                                                public void onSuccess(Uri uri) {
//                                                                    // Got the download URL for 'users/me/profile.png'
//                                                                    userprofileList.add(new userprofile(
//                                                                            name, status, uri, id,"friend"));
//                                                                    userprofileadapter.notifyDataSetChanged();
//                                                                }
//                                                            }).addOnFailureListener(new OnFailureListener() {
//                                                                @Override
//                                                                public void onFailure(@NonNull Exception exception) {
//                                                                    // Handle any errors
//                                                                }
//                                                            });
//                                                        }
//                                                        else if(documentt.contains("name")){
//                                                            String name=documentt.getString("name");
//                                                            String status=documentt.getString("status");
//                                                            String id=documentt.getString("currentUserID");
//                                                            UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                @Override
//                                                                public void onSuccess(Uri uri) {
//                                                                    // Got the download URL for 'users/me/profile.png'
//                                                                    userprofileList.add(new userprofile(
//                                                                            name, status, uri, id,"friend"));
//                                                                    userprofileadapter.notifyDataSetChanged();
//                                                                }
//                                                            }).addOnFailureListener(new OnFailureListener() {
//                                                                @Override
//                                                                public void onFailure(@NonNull Exception exception) {
//                                                                    // Handle any errors
//                                                                }
//                                                            });
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        });
//                            }
//                        }
//                    }
//                });
    }

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) contactFragmentView.findViewById(R.id.contacts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(contactFragmentView.getContext()));
        chatroomadapter = new chatroomAdapter(contactFragmentView.getContext(), chatroomList);
        recyclerView.setAdapter(chatroomadapter);
    }
}