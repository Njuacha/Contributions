package com.example.android.hubert.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

import java.util.List;

/**
 * Created by hubert on 8/5/18.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {
    private List<Member> mMemberList;
    private final Context mContext;
    private final ItemClickListeners itemClickListeners;

    public MembersAdapter(Context context, ItemClickListeners itemClickListeners){
        mContext = context;
        this.itemClickListeners = itemClickListeners;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list,parent,false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.memberTv.setText(mMemberList.get(position).getName());
    }


    @Override
    public int getItemCount() {

        if(mMemberList == null){
            return 0;
        }else return mMemberList.size();

    }

    public void setMembers(List<Member> members){
        mMemberList = members;
        notifyDataSetChanged();
    }

    public interface ItemClickListeners{
        void onMemberOptionViewClicked(Member member, View view);
        void onMemberClicked(Member member);
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView memberTv;
        final TextView tvOptions;

        public MemberViewHolder(View itemView) {
            super(itemView);
            memberTv = itemView.findViewById(R.id.tv_list_name);
            tvOptions = itemView.findViewById(R.id.tv_options);

            itemView.setOnClickListener(this);

            tvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Parse the member when the option view is clicked
                    itemClickListeners.onMemberOptionViewClicked(mMemberList.get(getAdapterPosition()), tvOptions);
                }
            });
        }

        @Override
        public void onClick(View v) {
            itemClickListeners.onMemberClicked(mMemberList.get(getAdapterPosition()));
        }
    }
}
