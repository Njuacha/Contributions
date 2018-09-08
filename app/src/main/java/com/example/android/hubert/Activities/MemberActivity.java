package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.hubert.Adapters.MemberContributionsAdapter;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.DatabaseClasses.MemberBasedContribution;
import com.example.android.hubert.R;
import com.example.android.hubert.View_model_classes.MemberContribViewModel;
import com.example.android.hubert.View_model_classes.MemberContribViewModelFactory;

import java.util.List;

import static com.example.android.hubert.Activities.MainActivity.EXTRA_MEMBER;

public class MemberActivity extends AppCompatActivity {
    AppDatabase mDb;
    Member mMember;
    MemberContributionsAdapter mAdapter;
    RecyclerView mRv;
    TextView mTvEmty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDb = AppDatabase.getDatabaseInstance(this);
        mAdapter = new MemberContributionsAdapter(this);
        mRv = findViewById(R.id.recycler_view);
        mTvEmty = findViewById(R.id.tv_explain_emptiness);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRv.setAdapter(mAdapter);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_MEMBER)){
           mMember =  intent.getParcelableExtra(EXTRA_MEMBER);
           setUpViewModel();
           setTitle(mMember.getName());
        }



    }

    private void setUpViewModel() {

        MemberContribViewModelFactory factory = new MemberContribViewModelFactory(mDb,mMember.getMemberId());
        MemberContribViewModel viewModel = ViewModelProviders.of(this,factory).get(MemberContribViewModel.class);

        viewModel.getContributionsInList().observe(this, new Observer<List<MemberBasedContribution>>() {
            @Override
            public void onChanged(@Nullable List<MemberBasedContribution> memberBasedContributions) {
                mAdapter.setmMemberBasedContributions(memberBasedContributions);
                if(memberBasedContributions != null && memberBasedContributions.size()>0){
                    mRv.setVisibility(View.VISIBLE);
                    mTvEmty.setVisibility(View.INVISIBLE);
                }
                else {
                    mRv.setVisibility(View.INVISIBLE);
                    mTvEmty.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
