package com.example.android.hubert.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.ListBasedContribution;
import com.example.android.hubert.R;

import java.util.List;


/**
 * Created by hubert on 6/19/18.
 */

public class ListContributionsAdapter extends RecyclerView.Adapter<ListContributionsAdapter.A_Contribution_ViewHolder>{

    private final Context mContext;
    private List<ListBasedContribution> mListBasedContributions;
    private final OnCLickListeners mOnCLickListeners;
    private int longestNameLength = 0;
    private int totalAmount = 0;

    public ListContributionsAdapter(Context mContext, OnCLickListeners onCLickListeners){
        this.mContext = mContext;
        mOnCLickListeners = onCLickListeners;
    }

    @NonNull
    @Override
    public A_Contribution_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.a_list,parent,false);
        return new A_Contribution_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull A_Contribution_ViewHolder holder, int position) {

        String name = mListBasedContributions.get(position).getName();
        if ( name.length()> longestNameLength){
            longestNameLength = name.length();
        }
        holder.tvName.setText(name);
        int amount = mListBasedContributions.get(position).getAmount();
        totalAmount += amount;
        String amount_s = String.format("%,d",amount);
        holder.tvAmount.setText(amount_s);

    }


    @Override
    public int getItemCount() {
        if( mListBasedContributions == null){
            return 0;
        }
        else return mListBasedContributions.size();

    }
    public interface OnCLickListeners {
        void onOptionTextViewClicked(ListBasedContribution listBasedContribution, View view);
        void onItemClicked(ListBasedContribution listBasedContribution);
    }

    public class A_Contribution_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tvName;
        final TextView tvAmount;
        final ImageView ivOptions;
        //CardView cardView;

        A_Contribution_ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_member);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            ivOptions = itemView.findViewById(R.id.tv_options);
            //cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(this);
            ivOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ListBasedContribution listBasedContribution = mListBasedContributions.get(position);
                    mOnCLickListeners.onOptionTextViewClicked(listBasedContribution, ivOptions);
                }
            });
        }

        @Override
        public void onClick(View v) {
            // Get contribution at current adapter position
            ListBasedContribution contribtn = mListBasedContributions.get(getAdapterPosition());

            mOnCLickListeners.onItemClicked(contribtn);
        }
    }

    public void setmListBasedContributions(List<ListBasedContribution> mListBasedContributions){
        this.mListBasedContributions = mListBasedContributions;
        longestNameLength = 0;
        totalAmount = 0;
        notifyDataSetChanged();
    }

    public List<ListBasedContribution> getmListBasedContributions(){
        return mListBasedContributions;
    }

    public int getLongestNameLength(){
        return longestNameLength;
    }

    public int getTotalAmount(){
        return totalAmount;
    }


}
