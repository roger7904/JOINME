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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

public class favoriteAdapter extends RecyclerView.Adapter<favoriteAdapter.ViewHolder> {
    private Context context;
    private List<favorite> favoriteList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserID,currentUserName;

    public favoriteAdapter(Context context, List<favorite> favoriteList){
        this.context = context;
        this.favoriteList = favoriteList;

    }

    @Override
    public favoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        View view = LayoutInflater.from(context).inflate(R.layout.favorite_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(favoriteAdapter.ViewHolder holder, int position) {
        favorite favorite = favoriteList.get(position);
        holder.textName.setText(favorite.getName());
        holder.textPlace.setText(favorite.getPlace());
        holder.textTime.setText(favorite.getTime());


        Glide.with(holder.itemView.getContext())
                .load(favorite.getImage())
                .into(holder.circleImageViewid);

        holder.btnfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("user").document(currentUserID)
                        .collection("favorite").document(favorite.getName());
                docRef.delete().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            holder.btnfavorite.setVisibility(View.INVISIBLE);
                            Toast.makeText(holder.itemView.getContext(), "取消收藏", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String activitytitle = favorite.getName();
                Intent intent = new Intent(holder.itemView.getContext(), signup.class);
                intent.putExtra("activitytitle", activitytitle);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImageViewid;
        TextView textPlace, textName,textTime;
        Button btnfavorite;

        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.users_profile_image);
            textName = (TextView) itemView.findViewById(R.id.activity_name);
            textPlace = (TextView) itemView.findViewById(R.id.activity_place);
            textTime = (TextView) itemView.findViewById(R.id.activity_time);
            btnfavorite= (Button) itemView.findViewById(R.id.user_online_status);

        }
    }

}
