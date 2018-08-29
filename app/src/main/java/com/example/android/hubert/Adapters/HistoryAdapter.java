package com.example.android.hubert.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.History;
import com.example.android.hubert.R;

import java.util.List;

/**
 * Created by hubert on 8/22/18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final Context mContext;
    private List<History> mHistoryList;
    private final ItemClickListeners mItemClickListeners;

    public HistoryAdapter(Context context, ItemClickListeners itemClickListeners){
        mContext = context;
        mItemClickListeners = itemClickListeners;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.a_list,parent,false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History contribution = mHistoryList.get(position);
        String date = contribution.getDate();
        int amount = contribution.getAmount();

        // Change amount to a string
        String amountAsString = String.format("%,d",amount);
        holder.dateTv.setText(date);
        holder.amountTv.setText(amountAsString);
    }

    @Override
    public int getItemCount() {
        if (mHistoryList == null) return 0;
        else return mHistoryList.size();
    }

    public interface ItemClickListeners{
        void onOptionViewClicked(History history, View view);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        final TextView dateTv;
        final TextView amountTv;
        final TextView optionsTv;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            dateTv = itemView.findViewById(R.id.tv_member);
            amountTv = itemView.findViewById(R.id.tv_amount);
            optionsTv = itemView.findViewById(R.id.tv_options);

            optionsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListeners.onOptionViewClicked(mHistoryList.get(getAdapterPosition()), v);
                }
            });
        }
    }

    public void setHistoryList(List<History> historyList){
        mHistoryList = historyList;
        notifyDataSetChanged();
    }


}
