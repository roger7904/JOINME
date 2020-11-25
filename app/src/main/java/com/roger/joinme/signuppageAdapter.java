package com.roger.joinme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class signuppageAdapter extends RecyclerView.Adapter<signuppageAdapter.ViewHolder> {
    private Context context;
    private List<signuppage> signuppageList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserID,currentUserName;

    public signuppageAdapter(Context context, List<signuppage> signuppageList){
        this.context = context;
        this.signuppageList = signuppageList;
    }

    public signuppageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        View view = LayoutInflater.from(context).inflate(R.layout.item_signupact, parent, false);
        return new signuppageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull signuppageAdapter.ViewHolder holder, int position) {
        signuppage signuppage = signuppageList.get(position);
        holder.textName.setText(signuppage.getActivityname());
        holder.actLocation.setText(signuppage.getActivityLocation());
        holder.startTime.setText(signuppage.getstartTime());

        holder.circleImageViewid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("activitytitle",signuppage.getActivityname());
                myIntent.putExtras(bundle);
                myIntent.setClass(holder.circleImageViewid.getContext(), signup.class);
                context.startActivity(myIntent);
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(signuppage.getImage())
//                .circleCrop()
                .into(holder.circleImageViewid);
    }

    @Override
    public int getItemCount() {
        return signuppageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImageViewid;
        TextView textName;
        TextView actLocation;
        TextView startTime;
        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.activityphoto2);
            textName = (TextView) itemView.findViewById(R.id.activityName);
            actLocation = (TextView) itemView.findViewById(R.id.textView47);
            startTime = (TextView) itemView.findViewById(R.id.textView33);
        }
    }
}
