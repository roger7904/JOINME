package com.roger.joinme;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class noticeupdate extends AppCompatActivity {

    public List<item> itemList;
    public int count = 1,x = 0,y = 0;
    public String joineraccount;
    public String[] account = new String[10000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_noticeupdate);

        itemList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(x=0;x<MainActivity.count;x++){
            db.collection("activity")
                    .document(MainActivity.docString[x])
                    .collection("participant")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(!document.getString("account").equals("0")){
//                                        System.out.println(count);
                                        account[count] = document.getString("account");
                                        itemList.add(new item(count,account[count]));
                                        count++;
                                    }
                                }
                            }
                        }
                    });
        }

//        System.out.println(y);
//        for(y=0;y<=count;y++){
//            System.out.println("00000"+account[y]);
//            y++;
//            itemList.add(new item(y,account[y-1]));
//        }

//        System.out.println("test"+itemListt);
        itemList.add(new item(2,"2"));
        itemList.add(new item(3,"3"));
        itemList.add(new item(4,"4"));
        initView();
    }
    public void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new itemAdapter(this,itemList));
    }
}
