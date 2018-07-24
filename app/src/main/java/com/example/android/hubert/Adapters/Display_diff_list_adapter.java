package com.example.android.hubert.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.R;


import java.util.List;

/**
 * Created by hubert on 6/15/18.
 */

public class Display_diff_list_adapter extends RecyclerView.Adapter<Display_diff_list_adapter.A_list_ViewHolder> {

    private Context mContext;
    private List<A_list> mListEntries;
    private final ItemClickListerner mItemClickListerner;
    private final ItemLongClickListerner mItemLongClickListerner;
    private final OptionTextViewClickListerner mOptionTextViewClickListerner;

    public Display_diff_list_adapter(Context context, ItemClickListerner listerner, ItemLongClickListerner mItemLongClickListerner, OptionTextViewClickListerner mOptionTextViewClickListerner){
        this.mContext = context;
        mItemClickListerner = listerner;
        this.mItemLongClickListerner = mItemLongClickListerner;
        this.mOptionTextViewClickListerner = mOptionTextViewClickListerner;
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

    public interface ItemClickListerner{
        void onItemCLicked(int itemId, String name);
    }

    public interface ItemLongClickListerner{
        void onItemLongClicked(int itemId, String name);
    }

    public interface ItemTouchHelperViewHolder{
        void onItemSelected();
        void onItemClear();
    }

    public interface OptionTextViewClickListerner{
        void onOptionTextViewClicked(int itemId,String name, View view);
    }

    public class A_list_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, ItemTouchHelperViewHolder{

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
                    mOptionTextViewClickListerner.onOptionTextViewClicked(itemId, name,tvOptions);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            A_list a_list = mListEntries.get(position);
            int itemId = a_list.getId();
            String name = a_list.getName();
            mItemClickListerner.onItemCLicked(itemId, name);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            A_list a_list = mListEntries.get(position);
            int itemId = a_list.getId();
            String name = a_list.getName();
            mItemLongClickListerner.onItemLongClicked(itemId, name);
            return true;
        }

        @Override
        public void onItemSelected() {
            int color = mContext.getResources().getColor(R.color.colorPrimaryLight);
            itemView.setBackgroundColor(color);
        }

        @Override
        public void onItemClear() {

            itemView.setBackgroundColor(0);
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
