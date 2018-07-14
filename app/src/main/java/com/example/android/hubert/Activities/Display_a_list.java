package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.hubert.Adapters.Display_a_list_adapter;
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_member_in_a_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Contribution;
import com.example.android.hubert.DialogFragments.Add_amount_dialog;
import com.example.android.hubert.View_model_classes.Main2ViewModelFactory;
import com.example.android.hubert.View_model_classes.Main2_view_model;
import com.example.android.hubert.R;

import java.util.ArrayList;
import java.util.List;

public class Display_a_list extends AppCompatActivity implements Display_a_list_adapter.OptionTextViewClickListerner, Add_amount_dialog.Add_amount_dialog_listener{
    RecyclerView mRv;
    Display_a_list_adapter mAdapter;
    int mListId;
    String mListName;
    Contribution mContribution;
    AppDatabase mDb;
    boolean mIsAdd;
    private int mOptionMenuItemClickedId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_a_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRv = findViewById(R.id.rv_contributions);
        mAdapter = new Display_a_list_adapter(this,this);
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(this));

        // instantiate the database variable
        mDb = AppDatabase.getDatabaseInstance(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(Display_a_list.this,Add_a_contribution.class);
                addIntent.putExtra(Display_diff_list.LIST_ID_EXTRA, mListId);
                startActivity(addIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getListIdAndNameFromIntent();
        setupViewModel();
        setTitle(mListName);
    }

    private void getListIdAndNameFromIntent() {
        if (getIntent().hasExtra(Display_diff_list.LIST_ID_EXTRA) && getIntent().hasExtra(Display_diff_list.LIST_NAME_EXTRA)) {
            mListId = getIntent().getIntExtra(Display_diff_list.LIST_ID_EXTRA, Display_diff_list.DEFAULT_LIST_ID);
            mListName = getIntent().getStringExtra(Display_diff_list.LIST_NAME_EXTRA);
        }
    }

    private void setupViewModel() {
        Main2ViewModelFactory factory = new Main2ViewModelFactory(mDb,mListId);
        Main2_view_model view_model2 = ViewModelProviders.of(this,factory).get(Main2_view_model.class);

        view_model2.getMembersInList().observe(this, new Observer<List<A_member_in_a_list>>() {
            @Override
            public void onChanged(@Nullable List<A_member_in_a_list> a_member_in_a_lists) {
                extractContributions(a_member_in_a_lists);
            }
        });
    }

    private void extractContributions(final List<A_member_in_a_list> a_member_in_a_lists) {
        final List<Contribution> contributions = new ArrayList<>();
        final AppDatabase mdb = AppDatabase.getDatabaseInstance(this);
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (A_member_in_a_list a_member_in_a_list: a_member_in_a_lists){
                    String name = mdb.member_dao().loadAMemberName(a_member_in_a_list.getMemberId());
                    int amount = a_member_in_a_list.getAmount();
                    Contribution contribution = new Contribution(name, amount);
                    contributions.add(contribution);

                }
                Log.d("Tag2",""+contributions.size()+"contributions available");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setmContributions(contributions);
                    }
                });

            }
        });
    }

    // Opens up a popup menu when the three vertical dot(optionView) is clicked
    @Override
    public void onOptionTextViewClicked(Contribution contribution, View view) {
        mContribution = contribution;
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.add_amount);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            // Opens up the same dialog when either the add or subtract option is clicked
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Add_amount_dialog dialog = new Add_amount_dialog();
                dialog.show(getSupportFragmentManager(),"edit amount dialog fragment");
                // Try to record whether it was add or subtract that was selected in a boolean
                switch(item.getItemId()){
                    case R.id.action_add:
                        mIsAdd = true;
                        break;
                    case R.id.action_subtract:
                        mIsAdd = false;
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }


    @Override
    public void onPositiveButtonClicked(final int amount) {
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
               int memberId = mDb.member_dao().loadAMemberId(mContribution.getName());
               int newAmount =  mContribution.getAmount() + (mIsAdd?amount:-amount);
               A_member_in_a_list a_member_in_a_list = new A_member_in_a_list(memberId,mListId
                       ,newAmount);
               mDb.a_member_in_a_list_dao().update_a_member_in_a_list(a_member_in_a_list);
            }
        });
    }
}
