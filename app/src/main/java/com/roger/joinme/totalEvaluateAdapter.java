package com.roger.joinme;

import android.content.Context;
import android.graphics.Color;
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

public class totalEvaluateAdapter extends RecyclerView.Adapter<totalEvaluateAdapter.ViewHolder> {
    private Context context;
    private List<totalEvaluate> personalEvaluateList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserID,currentUserName;

    public totalEvaluateAdapter(Context context, List<totalEvaluate> personalEvaluateList){
        this.context = context;
        this.personalEvaluateList = personalEvaluateList;
    }

    public totalEvaluateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        View view = LayoutInflater.from(context).inflate(R.layout.item_totalevaluate, parent, false);
        return new totalEvaluateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull totalEvaluateAdapter.ViewHolder holder, int position) {
        totalEvaluate totalEvaluate = personalEvaluateList.get(position);
        holder.userName.setText(totalEvaluate.getName());
        if(totalEvaluate.getActivityContent().equals("")){
            holder.actEva.setText("此人未填寫評論內容喔");
            holder.actEva.setTextColor(Color.LTGRAY);
        }else{
            holder.actEva.setText(totalEvaluate.getActivityContent());
        }
        holder.actName.setText("活動名稱: " + totalEvaluate.getActivityname());
        if(totalEvaluate.getStar() >= 5){
            holder.first2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.second2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.third2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.forth2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.fifth2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
        }else if(totalEvaluate.getStar() >= 4 && totalEvaluate.getStar()< 5){
            holder.first2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.second2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.third2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.forth2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.fifth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
        }else if(totalEvaluate.getStar() >= 3 && totalEvaluate.getStar()< 4){
            holder.first2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.second2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.third2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.forth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.fifth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
        }else if(totalEvaluate.getStar() >= 2 && totalEvaluate.getStar()< 3){
            holder.first2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.second2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.third2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.forth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.fifth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
        }else if(totalEvaluate.getStar() >= 1 && totalEvaluate.getStar()< 2){
            holder.first2.setBackground(context.getResources().getDrawable(R.drawable.brightstar));
            holder.second2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.third2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.forth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.fifth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
        }else{
            holder.first2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.second2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.third2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.fifth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
            holder.fifth2.setBackground(context.getResources().getDrawable(R.drawable.darkstar));
        }
        Glide.with(holder.itemView.getContext())
                .load(totalEvaluate.getImage())
//                .circleCrop()
                .into(holder.circleImageViewid);
    }

    @Override
    public int getItemCount() {
        return personalEvaluateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImageViewid;
        TextView userName;
        TextView actEva;
        TextView actName;
        TextView first2,second2,third2,forth2,fifth2;
        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.friendImg2);
            userName = (TextView) itemView.findViewById(R.id.Name);
            actEva = (TextView) itemView.findViewById(R.id.actEva);
            actName = (TextView) itemView.findViewById(R.id.actName);
            first2 = (TextView) itemView.findViewById(R.id.first2);
            second2 = (TextView) itemView.findViewById(R.id.second2);
            third2  = (TextView) itemView.findViewById(R.id.third2);
            forth2 = (TextView) itemView.findViewById(R.id.forth2);
            fifth2 = (TextView) itemView.findViewById(R.id.fifth2);
        }
    }

}
