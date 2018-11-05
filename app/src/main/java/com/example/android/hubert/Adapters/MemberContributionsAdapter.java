package com.example.android.hubert.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.MemberBasedContribution;
import com.example.android.hubert.R;

import java.util.List;

/**
 * Created by hubert on 9/6/18.
 */

public class MemberContributionsAdapter extends RecyclerView.Adapter<MemberContributionsAdapter.MContributionViewHolder> {
    private Context mContext;
    private List<MemberBasedContribution> mMemberBasedContributions;
    private OnMemberContrClicklisteners mClicklisteners;


    public MemberContributionsAdapter(Context context, OnMemberContrClicklisteners clicklisteners) {
        mContext = context;
        mClicklisteners = clicklisteners;
    }

    @NonNull
    @Override
    public MContributionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.a_list,parent,false);
        return new MContributionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MContributionViewHolder holder, int position) {
        MemberBasedContribution contribution = mMemberBasedContributions.get(position);
        holder.tvName.setText(contribution.getName());
        holder.tvAmount.setText(String.format("%,d",contribution.getAmount()));
    }

    @Override
    public int getItemCount() {
        if(mMemberBasedContributions != null){
            return mMemberBasedContributions.size();
        }
        else return 0;
    }

    public void setmMemberBasedContributions(List<MemberBasedContribution> list){
        mMemberBasedContributions = list;
        notifyDataSetChanged();
    }

    public interface OnMemberContrClicklisteners{
        void onOptionClicked(View view,MemberBasedContribution contrib);
        void onMmContrbClicked(MemberBasedContribution contrib);
    }

    public class MContributionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName;
        TextView tvAmount;
        ImageView ivOptions;

        public MContributionViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_member);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            ivOptions = itemView.findViewById(R.id.tv_options);

            ivOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClicklisteners.onOptionClicked(v,mMemberBasedContributions.get(getAdapterPosition()));
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClicklisteners.onMmContrbClicked(mMemberBasedContributions.get(getAdapterPosition()));
        }
    }
}
