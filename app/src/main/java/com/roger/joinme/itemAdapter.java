package com.roger.joinme;

import android.content.Context;
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
        View view = LayoutInflater.from(context).inflate(R.layout.content_noticeupdate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(itemAdapter.ViewHolder holder, int position) {
        item item = itemList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("222");
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
