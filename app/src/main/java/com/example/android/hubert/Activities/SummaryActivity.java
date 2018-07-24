package com.example.android.hubert.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.R;
import com.example.android.hubert.View_model_classes.SummaryViewModel;
import com.example.android.hubert.View_model_classes.SummaryViewModelFactory;


public class SummaryActivity extends AppCompatActivity {
    int mListId;
    String mListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getListIdAndNameFromIntent();
        setTitle(mListName);
        setUpSummary();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getListIdAndNameFromIntent() {
        if (getIntent().hasExtra(Display_diff_list.LIST_ID_EXTRA) && getIntent().hasExtra(Display_diff_list.LIST_NAME_EXTRA)) {
            mListId = getIntent().getIntExtra(Display_diff_list.LIST_ID_EXTRA, Display_diff_list.DEFAULT_LIST_ID);
            mListName = getIntent().getStringExtra(Display_diff_list.LIST_NAME_EXTRA);
        }
    }

    private void setUpSummary() {
        AppDatabase mDb = AppDatabase.getDatabaseInstance(this);
        // SetUp SummaryViewModelFactory and SummaryViewModel
        SummaryViewModelFactory factory = new SummaryViewModelFactory(mDb, mListId);
        SummaryViewModel viewModel = ViewModelProviders.of(this, factory).get(SummaryViewModel.class);
        // Get the summaryInfo data from the view model class
        final String[] summaryInfo = viewModel.getSummaryInfo();
        AppExecutors.getsInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                // Use the info data to set up the date, total amount and number of contributions in a list
                TextView tvDateCreated = (TextView) findViewById(R.id.tv_date);
                tvDateCreated.setText(summaryInfo[0]);
                TextView tvNumber = (TextView) findViewById(R.id.tv_number_value);
                tvNumber.setText(summaryInfo[1]);
                TextView tvTotalAmt = (TextView) findViewById(R.id.tv_amount_value);
                tvTotalAmt.setText(summaryInfo[2]);
            }
        });
    }

}
