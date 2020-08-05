package com.roger.joinme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
//        holder.
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
                            holder.activity.setText(fromname+"接受了你的交友邀請");

                        }else if(item.getType().equals("request")){
                            holder.activity.setText(fromname+"對你寄出了交友邀請");
                            holder.activity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    System.out.println("qaq");
//                    String visit_user_id = request.getID();
                                    Intent profileIntent = new Intent(holder.itemView.getContext(), friend_request.class);
//                    profileIntent.putExtra("visit_user_id", visit_user_id);
                                    holder.itemView.getContext().startActivity(profileIntent);
                                }
                            });
                        }else if(item.getType().equals("act_accept")){
                            holder.activity.setText(fromname+"核准了您的入團申請");
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
        ViewHolder(View itemView) {
            super(itemView);
            activity = (TextView) itemView.findViewById(R.id.activity);
        }
    }
}
