package com.example.android.hubert.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.Contribution;
import com.example.android.hubert.R;

import java.util.List;


/**
 * Created by hubert on 6/19/18.
 */

public class InnerContributionsAdapter extends RecyclerView.Adapter<InnerContributionsAdapter.A_Contribution_ViewHolder>{

    private Context mContext;
    private List<Contribution> mContributions;
    private final OptionTextViewClickListerner mOptionTextViewClickListerner;
    private int longestNameLength = 0;
    private int totalAmount = 0;

    public InnerContributionsAdapter(Context mContext, OptionTextViewClickListerner optionTextViewClickListerner){
        this.mContext = mContext;
        mOptionTextViewClickListerner = optionTextViewClickListerner;
    }

    @NonNull
    @Override
    public A_Contribution_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.a_list,parent,false);
        return new A_Contribution_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull A_Contribution_ViewHolder holder, int position) {

        String name = mContributions.get(position).getName();
        if ( name.length()> longestNameLength){
            longestNameLength = name.length();
        }
        holder.tvName.setText(name);
        int amount = mContributions.get(position).getAmount();
        totalAmount += amount;
        String amount_s = String.format("%,d",amount);
        holder.tvAmount.setText(amount_s);

    }


    @Override
    public int getItemCount() {
        if( mContributions == null){
            return 0;
        }
        else return mContributions.size();

    }
    public interface OptionTextViewClickListerner{
        void onOptionTextViewClicked(Contribution contribution, View view);
    }

    public class A_Contribution_ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvAmount;
        TextView tvOptions;
        CardView cardView;

        public A_Contribution_ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_member);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvOptions = itemView.findViewById(R.id.tv_options);
            cardView = itemView.findViewById(R.id.cardView);

            tvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Contribution contribution = mContributions.get(position);
                    mOptionTextViewClickListerner.onOptionTextViewClicked(contribution,tvOptions);
                }
            });
        }
    }

    public void setmContributions(List<Contribution> mContributions){
        this.mContributions = mContributions;
        longestNameLength = 0;
        totalAmount = 0;
        notifyDataSetChanged();
    }

    public List<Contribution> getmContributions(){
        return mContributions;
    }

    public int getLongestNameLength(){
        return longestNameLength;
    }

    public int getTotalAmount(){
        return totalAmount;
    }


}
