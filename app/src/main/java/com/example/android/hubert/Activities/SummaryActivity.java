package com.example.android.hubert.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.R;
import com.example.android.hubert.View_model_classes.SummaryViewModel;
import com.example.android.hubert.View_model_classes.SummaryViewModelFactory;

import static com.example.android.hubert.Activities.MainActivity.LIST_EXTRA;


public class SummaryActivity extends AppCompatActivity {
    private Alist alist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getListIdAndNameFromIntent();
        setTitle(alist.getName());
        setUpSummary();
    }

    private void getListIdAndNameFromIntent() {
        if (getIntent().hasExtra(LIST_EXTRA)) {
            alist = getIntent().getParcelableExtra(LIST_EXTRA);
        }
    }

    private void setUpSummary() {
        AppDatabase mDb = AppDatabase.getDatabaseInstance(this);
        // SetUp SummaryViewModelFactory and SummaryViewModel
        SummaryViewModelFactory factory = new SummaryViewModelFactory(mDb, alist);
        SummaryViewModel viewModel = ViewModelProviders.of(this, factory).get(SummaryViewModel.class);
        // Get the summaryInfo data from the view model class
        final String[] summaryInfo = viewModel.getSummaryInfo();
        AppExecutors.getsInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                // Use the info data to set up the date, total amount and number of contributions in a list
                TextView tvDateCreated = findViewById(R.id.tv_date);
                tvDateCreated.setText(summaryInfo[0]);
                TextView tvNumber = findViewById(R.id.tv_number_value);
                tvNumber.setText(summaryInfo[1]);
                TextView tvTotalAmt = findViewById(R.id.tv_amount_value);
                tvTotalAmt.setText(summaryInfo[2]);
            }
        });
    }

}
