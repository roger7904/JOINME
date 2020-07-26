package com.roger.joinme;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class userprofileAdapter extends RecyclerView.Adapter<userprofileAdapter.ViewHolder> {
    private Context context;
    private List<userprofile> userprofileList;


    public userprofileAdapter(Context context, List<userprofile> userprofileList){
        this.context = context;
        this.userprofileList = userprofileList;

    }

//    public userprofileAdapter(FindFriendsActivity findFriendsActivity, List<userprofile> userprofileList) {
//    }

    @Override
    public userprofileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(userprofileAdapter.ViewHolder holder, int position) {
        userprofile userprofile = userprofileList.get(position);
        holder.textName.setText(userprofile.getName());
        holder.textStatus.setText(userprofile.getStatus());

        Glide.with(holder.itemView.getContext())
                .load(userprofile.getImage())
                .circleCrop()
                .into(holder.circleImageViewid);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String visit_user_id = userprofile.getID();
                Intent profileIntent = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                profileIntent.putExtra("visit_user_id", visit_user_id);
                holder.itemView.getContext().startActivity(profileIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userprofileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImageViewid;
        ImageView useronlineimage;
        TextView textStatus, textName;
        Button btnaccept;
        Button btncancel;
        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.users_profile_image);
            useronlineimage = (ImageView) itemView.findViewById(R.id.user_online_status);
            textStatus = (TextView) itemView.findViewById(R.id.user_status);
            textName = (TextView) itemView.findViewById(R.id.user_profile_name);
            btnaccept=(Button) itemView.findViewById(R.id.request_accept_btn);
            btncancel=(Button) itemView.findViewById(R.id.request_cancel_btn);
        }
    }
}