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
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private View chatFragmentView;
    private List<chatroom> chatroomList;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private chatroomAdapter chatroomadapter;
    private StorageReference UserProfileImagesRef;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatFragmentView=inflater.inflate(R.layout.fragment_chat, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        chatroomList = new ArrayList<>();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        RetrieveAndDisplayContact();
        initView();
        // Inflate the layout for this fragment
        return chatFragmentView;
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
                                    Integer contentcount=dc.getDocument().getLong("contentcount").intValue();

                                    chatroomList.add(new chatroom(name,newestcontent,id,id,contentcount,time,"contact"));
                                    chatroomadapter.notifyDataSetChanged();

                                    Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Message.class));
                                    break;
                                case MODIFIED:
//                                    chatroomadapter.notifyDataSetChanged();
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

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) chatFragmentView.findViewById(R.id.chats_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(chatFragmentView.getContext()));
        chatroomadapter = new chatroomAdapter(chatFragmentView.getContext(), chatroomList);
        recyclerView.setAdapter(chatroomadapter);
    }
}