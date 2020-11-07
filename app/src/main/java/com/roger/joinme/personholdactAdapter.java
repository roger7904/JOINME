package com.roger.joinme;

import android.content.Context;
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

public class personholdactAdapter extends RecyclerView.Adapter<personholdactAdapter.ViewHolder> {
    private Context context;
    private List<personal> personalHoldActList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserID,currentUserName;

    public personholdactAdapter(Context context, List<personal> personalHoldActList){
        this.context = context;
        this.personalHoldActList = personalHoldActList;
    }

    public personholdactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        View view = LayoutInflater.from(context).inflate(R.layout.item_act, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull personholdactAdapter.ViewHolder holder, int position) {
        personal personal = personalHoldActList.get(position);
        holder.textName.setText(personal.getActivityname());
        holder.actLocation.setText(personal.getActivityLocation());
        Glide.with(holder.itemView.getContext())
                .load(personal.getImage())
//                .circleCrop()
                .into(holder.circleImageViewid);
    }

    @Override
    public int getItemCount() {
        return personalHoldActList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImageViewid;
        TextView textName;
        TextView actLocation;
        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.activityphoto4);
            textName = (TextView) itemView.findViewById(R.id.Name);
            actLocation = (TextView) itemView.findViewById(R.id.Content);
        }
    }
}
