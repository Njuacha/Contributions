package com.example.android.hubert.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.R;


import java.util.List;

/**
 * Created by hubert on 6/15/18.
 */

public class Display_diff_list_adapter extends RecyclerView.Adapter<Display_diff_list_adapter.A_list_ViewHolder> {

    private Context context;
    private List<A_list> listEntries;
    private final ItemClickListerner itemClickListerner;
    private final ItemLongClickListerner itemLongClickListerner;

    public Display_diff_list_adapter(Context context, ItemClickListerner listerner, ItemLongClickListerner itemLongClickListerner){
        this.context = context;
        itemClickListerner = listerner;
        this.itemLongClickListerner = itemLongClickListerner;
    }

    @NonNull
    @Override
    public A_list_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list,parent,false);
        return new A_list_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull A_list_ViewHolder holder, int position) {
        A_list a_list = listEntries.get(position);
        holder.tv_list_name.setText(a_list.getName());
    }

    @Override
    public int getItemCount() {
        if(listEntries == null){
            return 0;
        }
        else
        return listEntries.size();
    }

    public interface ItemClickListerner{
        void onItemCLickListerner(int itemId);
    }

    public interface ItemLongClickListerner{
        void onItemLongClickListerner(int itemId);
    }

    public interface ItemTouchHelperViewHolder{
        void onItemSelected();
        void onItemClear();
    }

    public class A_list_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, ItemTouchHelperViewHolder{

        TextView tv_list_name;

        public A_list_ViewHolder(View itemView) {
            super(itemView);
            tv_list_name = itemView.findViewById(R.id.tv_list_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemId = listEntries.get(getAdapterPosition()).getId();
            itemClickListerner.onItemCLickListerner(itemId);
        }

        @Override
        public boolean onLongClick(View v) {
            int itemId = listEntries.get(getAdapterPosition()).getId();
            itemLongClickListerner.onItemLongClickListerner(itemId);
            return true;
        }

        @Override
        public void onItemSelected() {
            int color = context.getResources().getColor(R.color.colorPrimaryLight);
            itemView.setBackgroundColor(color);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public void setListEntries(List<A_list> listEntries){
        this.listEntries = listEntries;
        notifyDataSetChanged();
    }

    public List<A_list> getListEntries(){
        return listEntries;
    }
}
