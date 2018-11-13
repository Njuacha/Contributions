package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.R;
import com.example.android.hubert.ViewModels.ContribSumryViewModel;
import com.example.android.hubert.ViewModels.ContribSumryViewModelFactory;

import static com.example.android.hubert.PlaceholderFragment.LIST_EXTRA;


public class ContributionSummaryActivity extends AppCompatActivity {
    private Alist alist;
    private TextView mTvDateCreated;
    private TextView mTvNumber ;
    private TextView mTvTotalAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTvDateCreated = findViewById(R.id.tv_date);
        mTvNumber = findViewById(R.id.tv_number_value);
        mTvTotalAmt = findViewById(R.id.tv_amount_value);

        if (getIntent().hasExtra(LIST_EXTRA)) {
            alist = getIntent().getParcelableExtra(LIST_EXTRA);
            setTitle(alist.getName());
        }

        setUpSummary();
    }


    private void setUpSummary() {
        AppDatabase mDb = AppDatabase.getDatabaseInstance(this);
        // SetUp ContribSumryViewModelFactory and ContribSumryViewModel
        ContribSumryViewModelFactory factory = new ContribSumryViewModelFactory(mDb, alist);
        ContribSumryViewModel viewModel = ViewModelProviders.of(this, factory).get(ContribSumryViewModel.class);

        viewModel.getSummaryInfo().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(@Nullable String[] strings) {
                // Use the info data to set up the date, total amount and number of contributions in a list

                mTvDateCreated.setText(strings[0]);
                mTvNumber.setText(strings[1]);
                String amountAsString = String.format("%,d",Integer.parseInt(strings[2]));
                mTvTotalAmt.setText(amountAsString);
            }
        });

    }

}
