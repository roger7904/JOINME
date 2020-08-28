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

public class verifyAdapter extends RecyclerView.Adapter<verifyAdapter.ViewHolder> {
    private Context context;
    private List<verify> verifyList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserID,currentUserName;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAoxsFReA:APA91bFrtTvCQxgBDQMTB7MddpMquycE2wOqh4K4_-yHNC2KSxCW0exYbpzx62KmVMNfY8HoZz67HrSc_xbo9NeWPSB13LGBxmAJujI-n90hm3zYLKbZGkqgGo_GIrdFLvcKP77GE5yA";
    final private String contentType = "application/json";


    public verifyAdapter(Context context, List<verify> verifyList){
        this.context = context;
        this.verifyList = verifyList;

    }

    @Override
    public verifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        holder.textActivity.setText(verify.getActivity());

        Glide.with(holder.itemView.getContext())
                .load(verify.getImage())
                .circleCrop()
                .into(holder.circleImageViewid);

        holder.btnaccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference docRef = db.collection("join_act_request").document(verify.getActivity())
                            .collection("UserID").document(verify.getId());
                    docRef.delete().addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                holder.btnaccept.setVisibility(View.INVISIBLE);
                                holder.btncancel.setVisibility(View.INVISIBLE);
                                Toast.makeText(holder.itemView.getContext(), "接受申請", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                    Map<String, Object> participant = new HashMap<>();
                    participant.put("UserID", verify.getId());
                    Map<String, Object> a = new HashMap<>();
                    a.put("activity", verify.getActivity());
                    db.collection("activity").document(verify.getActivity()).
                            collection("participant").document(verify.getId()).set(participant)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                    db.collection("chat").document(verify.getActivity()).
                            collection("participant").document(verify.getId()).set(participant)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                    db.collection("user").document(verify.getId()).
                            collection("activity").document(verify.getActivity()).set(a)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                    String saveCurrentTime, saveCurrentDate;

                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                    saveCurrentDate = currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                    saveCurrentTime = currentTime.format(calendar.getTime());

                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();

                    HashMap<String, String> chatNotificationMap = new HashMap<>();
                    chatNotificationMap.put("from", currentUserID);
                    chatNotificationMap.put("type", "act_accept");
                    chatNotificationMap.put("time", saveCurrentTime);
                    chatNotificationMap.put("date", saveCurrentDate);
                    chatNotificationMap.put("millisecond", ts);

                    db.collection("user").document(verify.getId()).
                            collection("notification").
                            document().
                            set(chatNotificationMap);

                    db.collection("user").document(verify.getId()).
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
                                                notifcationBody.put("title", "入團成功");
                                                notifcationBody.put("message", currentUserName + "接受了您的入團申請");
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
            });

        holder.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DocumentReference docRef = db.collection("join_act_request").document(verify.getActivity())
                        .collection("UserID").document(verify.getId());
                docRef.delete().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            holder.btnaccept.setVisibility(View.INVISIBLE);
                            holder.btncancel.setVisibility(View.INVISIBLE);
                            Toast.makeText(holder.itemView.getContext(), "拒絕申請", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

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
        TextView textGender, textName,textAge,textPhone,textActivity;
        Button btnaccept;
        Button btncancel;
        ViewHolder(View itemView) {
            super(itemView);
            circleImageViewid= (ImageView) itemView.findViewById(R.id.users_profile_image);
            textName = (TextView) itemView.findViewById(R.id.userName);
            textActivity = (TextView) itemView.findViewById(R.id.activityname);
            textGender = (TextView) itemView.findViewById(R.id.userGender);
            textAge = (TextView) itemView.findViewById(R.id.userAge);
            textPhone = (TextView) itemView.findViewById(R.id.userPhone);
            btnaccept=(Button) itemView.findViewById(R.id.btn_accept);
            btncancel=(Button) itemView.findViewById(R.id.btn_reject);
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
