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

public class verifyAdapter extends RecyclerView.Adapter<verifyAdapter.ViewHolder> {
    private Context context;
    private List<verify> verifyList;


    public verifyAdapter(Context context, List<verify> verifyList){
        this.context = context;
        this.verifyList = verifyList;

    }

    @Override
    public verifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.verify_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(verifyAdapter.ViewHolder holder, int position) {
        verify verify = verifyList.get(position);
        holder.textName.setText("姓名:"+verify.getName());
        holder.textGender.setText("性別:"+verify.getGender());
        holder.textAge.setText("年齡:"+verify.getAge());
        holder.textPhone.setText("手機:"+verify.getPhone());

        Glide.with(holder.itemView.getContext())
                .load(verify.getImage())
                .circleCrop()
                .into(holder.circleImageViewid);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                if(userprofile.getActivity().equals("find_friend") || userprofile.getActivity().equals("friend")){
//                    String visit_user_id = userprofile.getID();
//                    Intent profileIntent = new Intent(holder.itemView.getContext(), ProfileActivity.class);
//                    profileIntent.putExtra("visit_user_id", visit_user_id);
//                    holder.itemView.getContext().startActivity(profileIntent);
//                }else if(userprofile.getActivity().equals("chat")){
//                    Intent chatIntent = new Intent(holder.itemView.getContext(), ChatActivity.class);
//                    chatIntent.putExtra("visit_user_id", userprofile.getID());
//                    chatIntent.putExtra("visit_user_name", userprofile.getName());
//                    chatIntent.putExtra("visit_image", userprofile.getImage());
//                    holder.itemView.getContext().startActivity(chatIntent);
//                }
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return verifyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImageViewid;
        TextView textGender, textName,textAge,textPhone;
        Button btnaccept;
        Button btncancel;
        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.users_profile_image);
            textName = (TextView) itemView.findViewById(R.id.userName);
            textGender = (TextView) itemView.findViewById(R.id.userGender);
            textAge = (TextView) itemView.findViewById(R.id.userAge);
            textPhone = (TextView) itemView.findViewById(R.id.userPhone);
            btnaccept=(Button) itemView.findViewById(R.id.btn_accept);
            btncancel=(Button) itemView.findViewById(R.id.btn_reject);
        }
    }
}
