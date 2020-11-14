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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class userprofileAdapter extends RecyclerView.Adapter<userprofileAdapter.ViewHolder> {
    private Context context;
    private List<userprofile> userprofileList;

    public userprofileAdapter(Context context, List<userprofile> userprofileList){
        this.context = context;
        this.userprofileList = userprofileList;
    }

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
                if(userprofile.getActivity().equals("find_friend") || userprofile.getActivity().equals("friend")){
                    String visit_user_id = userprofile.getID();
                    Intent profileIntent = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                    profileIntent.putExtra("visit_user_id", visit_user_id);
                    profileIntent.putExtra("fromPersonal","findfriend");
                    holder.itemView.getContext().startActivity(profileIntent);
                }else if(userprofile.getActivity().equals("chat")){
                    Intent chatIntent = new Intent(holder.itemView.getContext(), ChatActivity.class);
                    chatIntent.putExtra("visit_user_id", userprofile.getID());
                    chatIntent.putExtra("visit_user_name", userprofile.getName());
                    chatIntent.putExtra("visit_image", userprofile.getImage());
                    holder.itemView.getContext().startActivity(chatIntent);
                }

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
