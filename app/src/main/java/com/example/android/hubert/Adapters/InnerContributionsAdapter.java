package com.example.android.hubert.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.Contribution;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

import java.util.List;


/**
 * Created by hubert on 6/19/18.
 */

public class InnerContributionsAdapter extends RecyclerView.Adapter<InnerContributionsAdapter.A_Contribution_ViewHolder>{

    private final Context mContext;
    private List<Contribution> mContributions;
    private final OnCLickListeners mOnCLickListeners;
    private int longestNameLength = 0;
    private int totalAmount = 0;

    public InnerContributionsAdapter(Context mContext, OnCLickListeners onCLickListeners){
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
    public interface OnCLickListeners {
        void onOptionTextViewClicked(Contribution contribution, View view);
        void onItemClicked(Member member);
    }

    public class A_Contribution_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tvName;
        final TextView tvAmount;
        final TextView tvOptions;
        //CardView cardView;

        public A_Contribution_ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_member);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvOptions = itemView.findViewById(R.id.tv_options);
            //cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(this);
            tvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Contribution contribution = mContributions.get(position);
                    mOnCLickListeners.onOptionTextViewClicked(contribution,tvOptions);
                }
            });
        }

        @Override
        public void onClick(View v) {
            // Get contribution at current adapter position
            Contribution contribtn = mContributions.get(getAdapterPosition());
            // Get the memberId from the contribution
            int id = contribtn.getMemberId();
            // Get the memberName from the contribution
            String name = contribtn.getName();
            // Recreate the member object and parse to onClick method
            mOnCLickListeners.onItemClicked(new Member(id,name));
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
