package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;
import com.example.android.hubert.ViewModels.MembSumryViewModel;
import com.example.android.hubert.ViewModels.MembSumryViewModelFactory;

import static com.example.android.hubert.Activities.MainActivity.EXTRA_MEMBER;

public class MemberSummaryActivity extends AppCompatActivity {

    private Member mMember;
    private TextView mTvNumbOfContrib;
    private TextView mTvAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_summary);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTvNumbOfContrib = findViewById(R.id.tv_numb_of_contrbutns);
        mTvAmount = findViewById(R.id.tv_amount_value);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MEMBER)){
            mMember = intent.getParcelableExtra(EXTRA_MEMBER);
            setUpViewModel();
        }

    }

    private void setUpViewModel() {
        MembSumryViewModelFactory factory = new MembSumryViewModelFactory(AppDatabase.getDatabaseInstance(this)
                ,mMember.getMemberId());
        MembSumryViewModel viewModel = ViewModelProviders.of(this,factory).get(MembSumryViewModel.class);

        viewModel.getSummaryInfo().observe(this, new Observer<int[]>() {
            @Override
            public void onChanged(@Nullable int[] ints) {
                mTvNumbOfContrib.setText(String.valueOf(ints[0]));
                mTvAmount.setText(String.valueOf(ints[1]));
            }
        });
    }


}
