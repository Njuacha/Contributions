package com.example.android.hubert.Adapters;

import android.content.Context;
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

public class ContributionsAdapter extends RecyclerView.Adapter<ContributionsAdapter.A_list_ViewHolder> {

    private Context mContext;
    private List<A_list> mListEntries;
    private final ItemClickListerners mItemClickListerners;

    public ContributionsAdapter(Context context, ItemClickListerners listerner ){
        this.mContext = context;
        mItemClickListerners = listerner;
    }

    @NonNull
    @Override
    public A_list_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list,parent,false);
        return new A_list_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final A_list_ViewHolder holder, int position) {
        A_list a_list = mListEntries.get(position);
        holder.tvListName.setText(a_list.getName());
    }

    @Override
    public int getItemCount() {
        if(mListEntries == null){
            return 0;
        }
        else
        return mListEntries.size();
    }

    public interface ItemClickListerners {
        void onItemCLicked(int itemId, String name);
        void onItemLongClicked(int itemId, String name);
        void onOptionTextViewClicked(int itemId,String name, View view);
    }


    public class A_list_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView tvListName;
        TextView tvOptions;

        public A_list_ViewHolder(View itemView) {
            super(itemView);
            tvListName = itemView.findViewById(R.id.tv_list_name);
            tvOptions = itemView.findViewById(R.id.tv_options);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            tvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    A_list a_list = mListEntries.get(position);
                    int itemId = a_list.getId();
                    String name = a_list.getName();
                    mItemClickListerners.onOptionTextViewClicked(itemId, name,tvOptions);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            A_list a_list = mListEntries.get(position);
            int itemId = a_list.getId();
            String name = a_list.getName();
            mItemClickListerners.onItemCLicked(itemId, name);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            A_list a_list = mListEntries.get(position);
            int itemId = a_list.getId();
            String name = a_list.getName();
            mItemClickListerners.onItemLongClicked(itemId, name);
            return true;
        }


    }

    public void setListEntries(List<A_list> listEntries){
        this.mListEntries = listEntries;
        notifyDataSetChanged();
    }

    public List<A_list> getListEntries(){
        return mListEntries;
    }


}
