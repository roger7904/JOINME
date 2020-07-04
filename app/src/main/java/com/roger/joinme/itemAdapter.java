package com.roger.joinme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> {
    private Context context;
    private List<item> itemList;

    public itemAdapter(Context context, List<item> itemList){
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public itemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(itemAdapter.ViewHolder holder, int position) {
        item item = itemList.get(position);
//        holder.
        holder.activity.setText(item.getContent());
        holder.activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                Intent intent = new Intent();
//                intent.setClass(context.this,verify.class);
//                startActivity(intent);
                System.out.println("1");
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView activity;
        ViewHolder(View itemView) {
            super(itemView);
            activity = (TextView) itemView.findViewById(R.id.activity);
        }
    }
}
