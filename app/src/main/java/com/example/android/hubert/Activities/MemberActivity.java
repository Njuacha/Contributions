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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.hubert.Adapters.MemberContributionsAdapter;
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.ListBasedContribution;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.DatabaseClasses.MemberBasedContribution;
import com.example.android.hubert.R;
import com.example.android.hubert.Utils.MyMobileAds;
import com.example.android.hubert.ViewModels.MemberContribViewModel;
import com.example.android.hubert.ViewModels.MemberContribViewModelFactory;
import com.google.android.gms.ads.AdView;

import java.util.Date;
import java.util.List;

import static com.example.android.hubert.Activities.DisplayAList.EXTRA_CONTRIB;
import static com.example.android.hubert.Activities.DisplayAList.EXTRA_LIST_ID;
import static com.example.android.hubert.Activities.DisplayAList.EXTRA_SUB;
import static com.example.android.hubert.PlaceholderFragment.EXTRA_MEMBER;
import static com.example.android.hubert.PlaceholderFragment.LIST_EXTRA;

public class MemberActivity extends AppCompatActivity implements MemberContributionsAdapter.OnMemberContrClicklisteners {
    public static final String EXTRA_LIST_NAME = "list name";
    private AppDatabase mDb;
    private Member mMember;
    private MemberContributionsAdapter mAdapter;
    private RecyclerView mRv;
    private TextView mTvEmty;

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


        // Initialize MyMobileAds
        AdView adView = findViewById(R.id.adView2);
        MyMobileAds.loadAdIntoAdView(this,adView);
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
    public void onOptionClicked(View view,final MemberBasedContribution contrib) {
        final ListBasedContribution listBasedContribution = new ListBasedContribution(
                                                            mMember.getMemberId()
                                                           ,mMember.getName()
                                                           ,contrib.getAmount());

        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.add_amount);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            // Opens up the same dialog when either the add or subtract option is clicked
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                // For the case of add or subtract we open up add listBasedContribution activity
                if( (id == R.id.action_add) || (id == R.id.action_subtract) ){
                    Intent intent = new Intent(MemberActivity.this,AddAContribution.class);
                    intent.putExtra(EXTRA_LIST_ID,contrib.getListId());
                    intent.putExtra(EXTRA_LIST_NAME,contrib.getName());
                    intent.putExtra(EXTRA_CONTRIB, listBasedContribution);
                    if(id == R.id.action_subtract){
                        intent.putExtra(EXTRA_SUB,true);
                    }

                    startActivity(intent);

                }else if(id == R.id.action_delete){ // For the case of delete we try to get the list Id and memberId to remove the listBasedContribution
                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.a_member_in_a_list_dao().deleteAContribution(listBasedContribution.getMemberId(),contrib.getListId());
                            mDb.historyDoa().delete(contrib.getListId(), listBasedContribution.getMemberId());
                        }
                    });
                }

                return false;
            }
        });

        popupMenu.show();

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
