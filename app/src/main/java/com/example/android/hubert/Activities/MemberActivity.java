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
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.History;
import com.example.android.hubert.DatabaseClasses.ListBasedContribution;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.DatabaseClasses.MemberBasedContribution;
import com.example.android.hubert.R;
import com.example.android.hubert.View_model_classes.MemberContribViewModel;
import com.example.android.hubert.View_model_classes.MemberContribViewModelFactory;

import java.util.Date;
import java.util.List;

import static com.example.android.hubert.Activities.Display_a_list.EXTRA_CONTRIB;
import static com.example.android.hubert.Activities.MainActivity.EXTRA_MEMBER;
import static com.example.android.hubert.Activities.MainActivity.LIST_EXTRA;

public class MemberActivity extends AppCompatActivity implements MemberContributionsAdapter.OnMemberContrClicklisteners {
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
        mAdapter = new MemberContributionsAdapter(this,this);
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

    @Override
    public void onOptionClicked(MemberBasedContribution contrib) {

    }

    @Override
    public void onMmContrbClicked(MemberBasedContribution contrib) {
        Alist alist = new Alist(contrib.getListId(),contrib.getName(),new Date());
        ListBasedContribution contribution = new ListBasedContribution(mMember.getMemberId()
                                                              ,mMember.getName()
                                                              ,contrib.getAmount());
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra(LIST_EXTRA,alist);
        intent.putExtra(EXTRA_CONTRIB,contribution);

        startActivity(intent);
    }
}
