package com.roger.joinme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class personalpage extends AppCompatActivity {
    private Button chatView;
    private Button selfView;
    public ImageView activityPhoto;
    public TextView title;
    public String name,id,activityname;
    private CircleImageView userProfileImage;
    public Uri image;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private StorageReference UserProfileImagesRef;
    private String currentUserID;
    private TextView userProfileName, userProfileStatus;

    public personalpage()
    {

    }

    public personalpage(String name,  Uri image,String id,String activityname) {
        this.name = name;
        this.image = image;
        this.id=id;
        this.activityname=activityname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalpage);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        final DocumentReference docRef = db.collection("user").document(currentUserID).collection("profile")
                .document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists() && snapshot.contains("name") && snapshot.contains("image")) {
                        String userImage = snapshot.getString("image");
                        String userName = snapshot.getString("name");
                        String userstatus = snapshot.getString("status");

//                        userProfileImage.setImageURI(Uri.fromFile(new File(userImage)));
                        UserProfileImagesRef.child(currentUserID+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Glide.with(personalpage.this)
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
                        userProfileName.setText(userName);
                        userProfileStatus.setText(userstatus);
                    } else if(snapshot != null && snapshot.exists() && snapshot.contains("name")){
                        String userName = snapshot.getString("name");
                        String userstatus = snapshot.getString("status");
                        Glide.with(personalpage.this)
                                .load(R.drawable.head)
                                .circleCrop()
                                .into(userProfileImage);
                        userProfileName.setText(userName);
                        userProfileStatus.setText(userstatus);
                    }
                } else {

                }
            }
        });
    }

    private void initData() {
    }

    private void initViews() {
        chatView = (Button) findViewById(R.id.chatView);
        title = (TextView) findViewById(R.id.title);
        activityPhoto = (ImageView) findViewById(R.id.activityphoto);
        recyclerView = (RecyclerView)findViewById(R.id.actHold);
        userProfileImage = (CircleImageView) findViewById(R.id.users_profile_image);
        userProfileName = (TextView) findViewById(R.id.userName);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_status);
    }

    private void setListeners() {
        chatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(personalpage.this, chatting.class);
                startActivity(intent);
            }
        });
    }
}
