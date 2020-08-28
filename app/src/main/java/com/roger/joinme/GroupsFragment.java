//package com.roger.joinme;
//
//
//
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class GroupsFragment extends Fragment
//{
//    private View groupFragmentView;
//    private ListView list_view;
//    private ArrayAdapter<String> arrayAdapter;
//    private ArrayList<String> list_of_groups = new ArrayList<>();
//    private FirebaseFirestore db;
//
//
//    public GroupsFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState)
//    {
//        groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);
//
//
//        db=FirebaseFirestore.getInstance();
//        IntializeFields();
//
//
//        RetrieveAndDisplayGroups();
//
//
//
//        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
//            {
//                String currentGroupName = adapterView.getItemAtPosition(position).toString();
//
//                Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
//                groupChatIntent.putExtra("groupName" , currentGroupName);
//                startActivity(groupChatIntent);
//            }
//        });
//
//
//        return groupFragmentView;
//    }
//
//
//
//    private void IntializeFields()
//    {
//        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
//        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
//        list_view.setAdapter(arrayAdapter);
//
//    }
//
//
//
//
//    private void RetrieveAndDisplayGroups()
//    {
//
//        db.collection("chat")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w("TAG", "Listen failed.", e);
//                            return;
//                        }
//
//                        Set<String> set = new HashSet<>();
//                        for (QueryDocumentSnapshot doc : value) {
//                            if (doc.getId()!= null) {
//                                set.add(doc.getId());
//                            }
//                        }
//                        Log.d("TAG", "Current groups : " + set);
//                        list_of_groups.clear();
//                        list_of_groups.addAll(set);
//                        arrayAdapter.notifyDataSetChanged();
//                    }
//                });
//    }
//}
//
//package com.roger.joinme;
//
//        import android.net.Uri;
//        import android.os.Bundle;
//
//        import androidx.annotation.NonNull;
//        import androidx.fragment.app.Fragment;
//        import androidx.recyclerview.widget.LinearLayoutManager;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//
//        import com.google.android.gms.tasks.OnCompleteListener;
//        import com.google.android.gms.tasks.OnFailureListener;
//        import com.google.android.gms.tasks.OnSuccessListener;
//        import com.google.android.gms.tasks.Task;
//        import com.google.firebase.auth.FirebaseAuth;
//        import com.google.firebase.firestore.DocumentSnapshot;
//        import com.google.firebase.firestore.FirebaseFirestore;
//        import com.google.firebase.firestore.QueryDocumentSnapshot;
//        import com.google.firebase.firestore.QuerySnapshot;
//        import com.google.firebase.storage.FirebaseStorage;
//        import com.google.firebase.storage.StorageReference;
//
//        import java.util.ArrayList;
//        import java.util.List;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class GroupsFragment extends Fragment {
//
//    private View groupFragmentView;
//    private List<userprofile> userprofileList;
//
//    private String currentUserID;
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//    private userprofileAdapter userprofileadapter;
//    private StorageReference UserProfileImagesRef;
//
//    public GroupsFragment() {
//        // Required empty public constructor
//    }
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        groupFragmentView=inflater.inflate(R.layout.fragment_groups, container, false);
//        mAuth = FirebaseAuth.getInstance();
//        currentUserID = mAuth.getCurrentUser().getUid();
//        db = FirebaseFirestore.getInstance();
//
//        userprofileList = new ArrayList<>();
//        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
//
//        initView();
//        RetrieveAndDisplayContact();
//
//        // Inflate the layout for this fragment
//        return groupFragmentView;
//    }
//
//    private void RetrieveAndDisplayContact() {
//        db.collection("user").document(currentUserID).collection("activity")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                    db.collection("activity").document(document.getId())
//                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                DocumentSnapshot document = task.getResult();
//                                                if (document.exists()) {
//                                                    String date=document.getString("date");
//                                                    String state=document.getString("state");
//                                                    String time=document.getString("time");
//
//                                                    db.collection("user").document(document.getId()).collection("profile")
//                                                            .document(document.getId())
//                                                            .get()
//                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        DocumentSnapshot documentt = task.getResult();
//                                                                        if (documentt.contains("name") && documentt.contains("image")) {
//                                                                            String name = documentt.getString("name");
//                                                                            String id = documentt.getString("currentUserID");
//                                                                            UserProfileImagesRef.child(id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                                @Override
//                                                                                public void onSuccess(Uri uri) {
//                                                                                    // Got the download URL for 'users/me/profile.png'
//                                                                                    String status="";
//                                                                                    if (state.equals("online"))
//                                                                                    {
//                                                                                        status="online";
//                                                                                    }
//                                                                                    else if (state.equals("offline"))
//                                                                                    {
//                                                                                        status="Last Seen: " + date + " " + time;
//                                                                                    }
//                                                                                    userprofileList.add(new userprofile(
//                                                                                            name, status, uri, id,"chat"));
//                                                                                    userprofileadapter.notifyDataSetChanged();
//                                                                                }
//                                                                            }).addOnFailureListener(new OnFailureListener() {
//                                                                                @Override
//                                                                                public void onFailure(@NonNull Exception exception) {
//                                                                                    // Handle any errors
//                                                                                }
//                                                                            });
//                                                                        }
//                                                                        else if(documentt.contains("name")){
//                                                                            String name=documentt.getString("name");
//                                                                            String id=documentt.getString("currentUserID");
//                                                                            UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                                @Override
//                                                                                public void onSuccess(Uri uri) {
//                                                                                    String status="";
//                                                                                    if (state.equals("online"))
//                                                                                    {
//                                                                                        status="online";
//                                                                                    }
//                                                                                    else if (state.equals("offline"))
//                                                                                    {
//                                                                                        status="Last Seen: " + date + " " + time;
//                                                                                    }
//                                                                                    userprofileList.add(new userprofile(
//                                                                                            name, status, uri, id,"chat"));
//                                                                                    userprofileadapter.notifyDataSetChanged();
//                                                                                }
//                                                                            }).addOnFailureListener(new OnFailureListener() {
//                                                                                @Override
//                                                                                public void onFailure(@NonNull Exception exception) {
//                                                                                    // Handle any errors
//                                                                                }
//                                                                            });
//                                                                        }
//                                                                    }
//                                                                }
//                                                            });
//                                                } else {
//
//                                                }
//                                            } else {
//
//                                            }
//                                        }
//                                    });
//                            }
//                        }
//                    }
//                });
//    }
//
//    public void initView(){
//        RecyclerView recyclerView = (RecyclerView) chatFragmentView.findViewById(R.id.chats_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(chatFragmentView.getContext()));
//        userprofileadapter = new userprofileAdapter(chatFragmentView.getContext(), userprofileList);
//        recyclerView.setAdapter(userprofileadapter);
//    }
//}
