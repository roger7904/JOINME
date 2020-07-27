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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class requestAdapter extends RecyclerView.Adapter<requestAdapter.ViewHolder> {
    private Context context;
    private List<request> requestList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserID,currentUserName;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoxsFReA:APA91bFrtTvCQxgBDQMTB7MddpMquycE2wOqh4K4_-yHNC2KSxCW0exYbpzx62KmVMNfY8HoZz67HrSc_xbo9NeWPSB13LGBxmAJujI-n90hm3zYLKbZGkqgGo_GIrdFLvcKP77GE5yA";
    final private String contentType = "application/json";

    public requestAdapter(Context context, List<request> requestList){
        this.context = context;
        this.requestList = requestList;

    }



    @Override
    public requestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        final DocumentReference docRef = db.collection("user").document(currentUserID).collection("profile")
                .document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {
                        currentUserName=snapshot.getString("name");
                    } else {

                    }
                }
            }
        });

        View view = LayoutInflater.from(context).inflate(R.layout.user_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(requestAdapter.ViewHolder holder, int position) {
        request request = requestList.get(position);
        holder.textName.setText(request.getName());
        holder.textStatus.setText(request.getStatus());

        holder.AcceptButton.setVisibility(View.VISIBLE);
        holder.CancelButton.setVisibility(View.VISIBLE);


        Glide.with(holder.itemView.getContext())
                .load(request.getImage())
                .circleCrop()
                .into(holder.circleImageViewid);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String visit_user_id = request.getID();
                Intent profileIntent = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                profileIntent.putExtra("visit_user_id", visit_user_id);
                holder.itemView.getContext().startActivity(profileIntent);
            }
        });

        holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Map<String, Object> receiverdata = new HashMap<>();
                receiverdata.put("UserID", request.getID());
                Map<String, Object> senderdata = new HashMap<>();
                senderdata.put("UserID", currentUserID);
                db.collection("user").document(currentUserID).
                        collection("friends").document( request.getID()).set(receiverdata)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    db.collection("user").document( request.getID()).
                                            collection("friends").document(currentUserID).set(senderdata)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentReference docRef = db.collection("add_friend_request").document(currentUserID)
                                                                .collection("UserID").document( request.getID());

                                                        Map<String,Object> updates = new HashMap<>();
                                                        updates.put("request_type", FieldValue.delete());

                                                        docRef.delete().addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {

                                                                    db.collection("add_friend_request").document( request.getID()).
                                                                            collection("UserID").
                                                                            document(currentUserID).delete()
                                                                            .addOnCompleteListener(new OnCompleteListener() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        holder.AcceptButton.setVisibility(View.INVISIBLE);
                                                                                        holder.CancelButton.setVisibility(View.INVISIBLE);
                                                                                        Toast.makeText(holder.itemView.getContext(), "接受邀請", Toast.LENGTH_LONG).show();

                                                                                        String saveCurrentTime, saveCurrentDate;

                                                                                        Calendar calendar = Calendar.getInstance();

                                                                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                                                        saveCurrentDate = currentDate.format(calendar.getTime());

                                                                                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                                                                        saveCurrentTime = currentTime.format(calendar.getTime());

                                                                                        Long tsLong = System.currentTimeMillis()/1000;
                                                                                        String ts = tsLong.toString();

                                                                                        HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                                                        chatNotificationMap.put("from", currentUserID);
                                                                                        chatNotificationMap.put("type", "accept");
                                                                                        chatNotificationMap.put("time", saveCurrentTime);
                                                                                        chatNotificationMap.put("date", saveCurrentDate);
                                                                                        chatNotificationMap.put("millisecond", ts);

                                                                                        db.collection("user").document( request.getID()).
                                                                                                collection("notification").
                                                                                                document().
                                                                                                set(chatNotificationMap);

                                                                                        db.collection("user").document( request.getID()).
                                                                                                get().
                                                                                                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            DocumentSnapshot document = task.getResult();
                                                                                                            if (document.exists()) {
                                                                                                                String RECEIVER_DEVICE = document.getString("device_token");
                                                                                                                JSONObject notification = new JSONObject();
                                                                                                                JSONObject notifcationBody = new JSONObject();
                                                                                                                try {
                                                                                                                    notifcationBody.put("title", "您有新的好友");
                                                                                                                    notifcationBody.put("message", currentUserName+"接受了您的交友邀請");
                                                                                                                    notification.put("to", RECEIVER_DEVICE);
                                                                                                                    notification.put("data", notifcationBody);
                                                                                                                } catch (JSONException e) {
                                                                                                                }
                                                                                                                sendNotification(notification);
                                                                                                            } else {
                                                                                                            }
                                                                                                        } else {
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                }
                            }
                        });

                db.collection("message").document(currentUserID).
                        collection("UserID").document(request.getID())
                        .set(receiverdata)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    db.collection("message").document(request.getID()).
                                            collection("UserID").document(currentUserID).set(senderdata)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {

                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        holder.CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DocumentReference docRef = db.collection("add_friend_request").document(currentUserID)
                        .collection("UserID").document(request.getID());

                docRef.delete().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            db.collection("add_friend_request").document(request.getID()).collection("UserID").
                                    document(currentUserID).delete()
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                holder.AcceptButton.setVisibility(View.INVISIBLE);
                                                holder.CancelButton.setVisibility(View.INVISIBLE);
                                                Toast.makeText(holder.itemView.getContext(), "拒絕邀請", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImageViewid;
        ImageView useronlineimage;
        TextView textStatus, textName;
        Button AcceptButton;
        Button CancelButton;
        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.users_profile_image);
            useronlineimage = (ImageView) itemView.findViewById(R.id.user_online_status);
            textStatus = (TextView) itemView.findViewById(R.id.user_status);
            textName = (TextView) itemView.findViewById(R.id.user_profile_name);
            AcceptButton=(Button) itemView.findViewById(R.id.request_accept_btn);
            CancelButton=(Button) itemView.findViewById(R.id.request_cancel_btn);
        }
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
