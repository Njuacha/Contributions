package com.example.android.hubert.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class Display_a_list_adapter extends RecyclerView.Adapter<Display_a_list_adapter.A_Contribution_ViewHolder>{

    private Context mContext;
    private List<Contribution> mContributions;
    private final OptionTextViewClickListerner mOptionTextViewClickListerner;

    public Display_a_list_adapter(Context mContext, OptionTextViewClickListerner optionTextViewClickListerner){
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
        holder.tvName.setText(mContributions.get(position).getName());
        holder.tvAmount.setText(String.valueOf(mContributions.get(position).getAmount()));
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

    public class A_Contribution_ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvAmount;
        TextView tvOptions;

        public A_Contribution_ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_member);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvOptions = itemView.findViewById(R.id.tv_options);

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
        notifyDataSetChanged();
    }



}
