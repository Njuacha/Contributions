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

    private Context context;
    private List<Contribution> contributions;
    public Display_a_list_adapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public A_Contribution_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.a_list,parent,false);
        return new A_Contribution_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull A_Contribution_ViewHolder holder, int position) {
        holder.tvName.setText(contributions.get(position).getName());
        holder.tvAmount.setText(String.valueOf(contributions.get(position).getAmount()));
    }


    @Override
    public int getItemCount() {
        if( contributions == null){
            return 0;
        }
        else return contributions.size();

    }

    public class A_Contribution_ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvAmount;

        public A_Contribution_ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_member);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }

    public void setContributions(List<Contribution> contributions){
        this.contributions = contributions;
        notifyDataSetChanged();
    }


}
