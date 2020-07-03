package com.roger.joinme;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class noticeupdate extends AppCompatActivity {

    public List<item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_noticeupdate);

        itemList = new ArrayList<>();

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("activity")
//                .get()
        itemList.add(new item(1,"1"));
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
