package com.roger.joinme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> {
    private Context context;
    private List<item> itemList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private String fromname;

    public itemAdapter(Context context, List<item> itemList){
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public itemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(itemAdapter.ViewHolder holder, int position) {
        item item = itemList.get(position);
        if(item.getLookornot().equals("look")){
            holder.test.setBackgroundColor(Color.WHITE);
            holder.activity.setBackgroundColor(Color.WHITE);
        }else{
            holder.test.setBackgroundColor(context.getResources().getColor(R.color.colorlightGray));
            holder.activity.setBackgroundColor(context.getResources().getColor(R.color.colorlightGray));
        }


        final DocumentReference docRef = db.collection("user").document(item.getFrom()).collection("profile")
                .document(item.getFrom());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {
                        fromname=snapshot.getString("name");
                        if(item.getType().equals("accept")){
                            String content = fromname+"接受了你的交友邀請";
                            if((content.length()) > 20){
                                holder.activity.setText(fromname+"接受...");
                            }else{
                                holder.activity.setText(content);
                            }
                            holder.activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    Map<String, Object> a = new HashMap<>();
                                    a.put("islook","look");
                                    db.collection("user").document(currentUserID).collection("notification")
                                            .document(item.getId()).set(a, SetOptions.merge());
                                    Intent profileIntent = new Intent(holder.itemView.getContext(), noticeupdate.class);
                                    holder.itemView.getContext().startActivity(profileIntent);
                                }
                            });
                        }else if(item.getType().equals("request")){
                            String content = fromname+"對你寄出了交友邀請";
                            if((content.length()) > 20){
                                holder.activity.setText(fromname+"對你寄出...");
                            }else{
                                holder.activity.setText(content);
                            }
                            holder.activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    Map<String, Object> a = new HashMap<>();
                                    a.put("islook","look");
                                    db.collection("user").document(currentUserID).collection("notification")
                                            .document(item.getId()).set(a, SetOptions.merge());
//                    String visit_user_id = request.getID();
                                    Intent profileIntent = new Intent(holder.itemView.getContext(), friend_request.class);
//                    profileIntent.putExtra("visit_user_id", visit_user_id);
                                    holder.itemView.getContext().startActivity(profileIntent);
                                }
                            });
                        }else if(item.getType().equals("act_accept")){
                            String content = fromname+"核准了您的入團申請";
                            if(content.length() > 20){
                                holder.activity.setText(fromname+"核准了...");
                            }else{
                                holder.activity.setText(content);
                            }
                            holder.activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    Map<String, Object> a = new HashMap<>();
                                    a.put("islook","look");
                                    db.collection("user").document(currentUserID).collection("notification")
                                            .document(item.getId()).set(a, SetOptions.merge());
                                    Intent profileIntent = new Intent(holder.itemView.getContext(), noticeupdate.class);
                                    holder.itemView.getContext().startActivity(profileIntent);
                                }
                            });
                        }else if(item.getType().equals("joinact")){
                            String content = fromname+"對您辦的活動提出了加入申請";
                            if(content.length() > 20){
                                holder.activity.setText(fromname+"對您提出...");
                            }else{
                                holder.activity.setText(content);
                            }
                            holder.activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    Map<String, Object> a = new HashMap<>();
                                    a.put("islook","look");
                                    db.collection("user").document(currentUserID).collection("notification")
                                            .document(item.getId()).set(a, SetOptions.merge());
//                    String visit_user_id = request.getID();
                                    Intent profileIntent = new Intent(holder.itemView.getContext(), verifyActivity.class);
//                    profileIntent.putExtra("visit_user_id", visit_user_id);
                                    holder.itemView.getContext().startActivity(profileIntent);
                                }
                            });
                        }else if(item.getType().equals("evaluate")){
                            String activityname=item.getActivityname();
                            final DocumentReference docRef = db.collection("activity").document(activityname);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot snapshot = task.getResult();
                                        String UserID=snapshot.getString("organizerID");
                                        if(snapshot.getString("organizerID").equals(currentUserID)){
                                            String content = "您舉辦的活動"+activityname+"已經結束，點此處對活動成員評價";
                                            if(content.length() > 20){
                                                holder.activity.setText("您舉辦的活動"+activityname+"已結束，點此...");
                                            }else{
                                                holder.activity.setText(content);
                                            }
                                            holder.activity.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    Map<String, Object> a = new HashMap<>();
                                                    a.put("islook","look");
                                                    db.collection("user").document(currentUserID).collection("notification")
                                                            .document(item.getId()).set(a, SetOptions.merge());
                                                    Intent profileIntent = new Intent(holder.itemView.getContext(), evaluateActivity.class);
                                                    profileIntent.putExtra("activityname", activityname);
                                                    holder.itemView.getContext().startActivity(profileIntent);
                                                }
                                            });
                                        }else{
                                            final DocumentReference docRef = db.collection("activity")
                                                    .document(activityname)
                                                    .collection("participant")
                                                    .document(currentUserID);
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot snapshot = task.getResult();
                                                        String content = "您參與的活動"+activityname+"已經結束，點此處對活動舉辦者評價";
                                                        if(content.length() > 20){
                                                            holder.activity.setText("您參與的活動"+activityname+"已經結束，點此...");
                                                        }else{
                                                            holder.activity.setText(content);
                                                        }

                                                        holder.activity.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view)
                                                            {
                                                                Map<String, Object> a = new HashMap<>();
                                                                a.put("islook","look");
                                                                db.collection("user").document(currentUserID).collection("notification")
                                                                        .document(item.getId()).set(a, SetOptions.merge());
                                                                if(snapshot.contains("evaluate_to_organizer")){
                                                                    Toast.makeText(holder.itemView.getContext(), "已經成功對舉辦者評價", Toast.LENGTH_LONG).show();
                                                                }else{
                                                                    Intent profileIntent = new Intent(holder.itemView.getContext(), organizerevaluateActivity.class);
                                                                    profileIntent.putExtra("activityname", activityname);
                                                                    profileIntent.putExtra("UserID", UserID);
                                                                    holder.itemView.getContext().startActivity(profileIntent);
                                                                }

                                                            }
                                                        });

                                                    } else {

                                                    }
                                                }
                                            });
                                        }
                                    } else {

                                    }
                                }
                            });

                        }else {
                            holder.activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    Map<String, Object> a = new HashMap<>();
                                    a.put("islook","look");
                                    db.collection("user").document(currentUserID).collection("notification")
                                            .document(item.getId()).set(a, SetOptions.merge());
                                }
                            });
                        }

                    } else {

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView activity;
        LinearLayout test;
        ViewHolder(View itemView) {
            super(itemView);
            activity = (TextView) itemView.findViewById(R.id.activity);
            test=(LinearLayout) itemView.findViewById(R.id.activityy);
        }
    }
}
