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

    public MembersAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.a_member,parent,false);
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

    public class MemberViewHolder extends RecyclerView.ViewHolder{
        TextView memberTv;
        public MemberViewHolder(View itemView) {
            super(itemView);

            memberTv = itemView.findViewById(R.id.tv_member);

        }
    }
}
