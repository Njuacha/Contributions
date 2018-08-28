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
import com.example.android.hubert.View_model_classes.SummaryViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created by hubert on 8/22/18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final Context mContext;
    private List<History> mHistoryList;

    public HistoryAdapter(Context context){
        mContext = context;
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
        Date date = contribution.getDate();
        int amount = contribution.getAmount();
        // Use a formatter already used in Summary activity to convert the date to a string
        String dateAsString = SummaryViewModel.dateFormat.format(date);
        // Change amount to a string
        String amountAsString = String.format("%,d",amount);
        holder.dateTv.setText(dateAsString);
        holder.amountTv.setText(amountAsString);
    }

    @Override
    public int getItemCount() {
        if (mHistoryList == null) return 0;
        else return mHistoryList.size();
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
        }
    }

    public void setHistoryList(List<History> historyList){
        mHistoryList = historyList;
        notifyDataSetChanged();
    }


}
