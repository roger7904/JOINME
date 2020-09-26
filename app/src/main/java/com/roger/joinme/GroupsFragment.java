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
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
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
public class GroupsFragment extends Fragment {

    private View groupFragmentView;
    private List<chatroom> chatroomList;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private chatroomAdapter chatroomadapter;
    private StorageReference ImagesRef;

    public GroupsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupFragmentView=inflater.inflate(R.layout.fragment_groups, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        chatroomList = new ArrayList<>();
        ImagesRef = FirebaseStorage.getInstance().getReference();

        initView();
        RetrieveAndDisplayContact();

        // Inflate the layout for this fragment
        return groupFragmentView;
    }

    private void RetrieveAndDisplayContact() {
        db.collection("chat")
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
                                    db.collection("user")
                                            .document(currentUserID)
                                            .collection("activity")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(document.getId().equals(dc.getDocument().getId())){
                                                                String name=dc.getDocument().getString("activity");
                                                                String newestcontent=dc.getDocument().getString("newestcontent");
                                                                String id=dc.getDocument().getId();
                                                                String time=dc.getDocument().getString("newestmillisecond");
                                                                Integer contentcount=dc.getDocument().getLong("contentcount").intValue();

                                                                DocumentReference docRef = db.collection("activity").document(name);
                                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot document = task.getResult();
                                                                            if (document.exists()) {
                                                                                if (Boolean.parseBoolean(document.getString("img"))) {
                                                                                    chatroomList.add(new chatroom(name,newestcontent,id,id,contentcount,time,"group"));
                                                                                    chatroomadapter.notifyDataSetChanged();
                                                                                }else if (document.getString("activityType").equals("商家優惠")) {
                                                                                    chatroomList.add(new chatroom(name,newestcontent,"商家優惠",id,contentcount,time,"group"));
                                                                                    chatroomadapter.notifyDataSetChanged();
                                                                                }else if (document.getString("activityType").equals("KTV")) {
                                                                                    chatroomList.add(new chatroom(name,newestcontent,"KTV",id,contentcount,time,"group"));
                                                                                    chatroomadapter.notifyDataSetChanged();
                                                                                }else if (document.getString("activityType").equals("限時")) {
                                                                                    chatroomList.add(new chatroom(name,newestcontent,"限時",id,contentcount,time,"group"));
                                                                                    chatroomadapter.notifyDataSetChanged();
                                                                                }else if (document.getString("activityType").equals("球類")) {
                                                                                    chatroomList.add(new chatroom(name,newestcontent,"球類",id,contentcount,time,"group"));
                                                                                    chatroomadapter.notifyDataSetChanged();
                                                                                }
                                                                            } else {

                                                                            }
                                                                        } else {

                                                                        }
                                                                    }
                                                                });
                                                                Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Message.class));
                                                                break;
                                                            }
                                                        }
                                                    } else {

                                                    }
                                                }
                                            });
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

    public void initView(){
        RecyclerView recyclerView = (RecyclerView) groupFragmentView.findViewById(R.id.group_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(groupFragmentView.getContext()));
        chatroomadapter = new chatroomAdapter(groupFragmentView.getContext(), chatroomList);
        recyclerView.setAdapter(chatroomadapter);
    }
}
