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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class chatroomAdapter extends RecyclerView.Adapter<chatroomAdapter.ViewHolder> {
    private Context context;
    private List<chatroom> chatroomList;
    private StorageReference UserProfileImagesRef;
    private StorageReference ImagesRef;


    public chatroomAdapter(Context context, List<chatroom> chatroomList){
        this.context = context;
        this.chatroomList = chatroomList;

    }

    @Override
    public chatroomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        ImagesRef = FirebaseStorage.getInstance().getReference();
        View view = LayoutInflater.from(context).inflate(R.layout.message_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(chatroomAdapter.ViewHolder holder, int position) {
        chatroom chatroom = chatroomList.get(position);
        holder.contentCount.setText(chatroom.getContentcount().toString());
        holder.textName.setText(chatroom.getName());
        holder.textContent.setText(chatroom.getNewestcontent());

        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd");
//        saveCurrentDate = currentDate.format(chatroom.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(Integer.parseInt(chatroom.getTime())*1000);
        holder.textTime.setText(saveCurrentTime);
        System.out.println(Integer.parseInt(chatroom.getTime()));
        System.out.println(saveCurrentTime);

        if(chatroom.getActivity().equals("contact")){
            UserProfileImagesRef.child(chatroom.getId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView.getContext())
                            .load(uri)
                            .circleCrop()
                            .into(holder.circleImageViewid);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    UserProfileImagesRef.child("head.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(holder.itemView.getContext())
                                    .load(uri)
                                    .circleCrop()
                                    .into(holder.circleImageViewid);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            });
        }else if(chatroom.getActivity().equals("group")){
            if(chatroom.getImage().equals("商家優惠")){
                ImagesRef.child("商家優惠.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .circleCrop()
                                .into(holder.circleImageViewid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }else if(chatroom.getImage().equals("KTV")){
                ImagesRef.child("KTV.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .circleCrop()
                                .into(holder.circleImageViewid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }else if(chatroom.getImage().equals("限時")){
                ImagesRef.child("限時.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .circleCrop()
                                .into(holder.circleImageViewid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }else if(chatroom.getImage().equals("球類")){
                ImagesRef.child("球類.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .circleCrop()
                                .into(holder.circleImageViewid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }else{
                ImagesRef.child(chatroom.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .circleCrop()
                                .into(holder.circleImageViewid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

        }





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(chatroom.getActivity().equals("contact")){
                    Intent chatIntent = new Intent(holder.itemView.getContext(), ChatActivity.class);
                    chatIntent.putExtra("visit_user_id", chatroom.getId());
                    chatIntent.putExtra("visit_user_name", chatroom.getName());
                    chatIntent.putExtra("visit_image", chatroom.getImage());
                    holder.itemView.getContext().startActivity(chatIntent);
                }else if(chatroom.getActivity().equals("group")){
                    Intent chatIntent = new Intent(holder.itemView.getContext(), GroupChatActivity.class);
                    chatIntent.putExtra("groupName", chatroom.getId());
                    holder.itemView.getContext().startActivity(chatIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatroomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImageViewid;
        TextView contentCount;
        TextView textContent, textName,textTime;
        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.users_profile_image);
            contentCount = (TextView) itemView.findViewById(R.id.content_count);
            textName = (TextView) itemView.findViewById(R.id.user_profile_name);
            textTime = (TextView) itemView.findViewById(R.id.time);
            textContent = (TextView) itemView.findViewById(R.id.user_status);

        }
    }
}
