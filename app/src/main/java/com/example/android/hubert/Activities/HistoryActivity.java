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

import com.example.android.hubert.Adapters.HistoryAdapter;
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AMemberInAList;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.ListBasedContribution;
import com.example.android.hubert.DatabaseClasses.History;
import com.example.android.hubert.R;
import com.example.android.hubert.View_model_classes.HistViewModel;
import com.example.android.hubert.View_model_classes.HistViewModelFactory;

import java.util.List;

import static com.example.android.hubert.Activities.Display_a_list.EXTRA_CONTRIB;
import static com.example.android.hubert.Activities.MainActivity.LIST_EXTRA;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.ItemClickListeners {
    public static final String EXTRA_HISTORY = "history extra";
    private Alist mAlist;
    private ListBasedContribution mListBasedContribution;
    private HistoryAdapter mAdapter;
    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        mRv = findViewById(R.id.rv_history);
        mAdapter = new HistoryAdapter(this, this);
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(this));

        mRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


        // Get the list and the memberId from the displayListActivity
        Intent intent = getIntent();
        if(intent.hasExtra(LIST_EXTRA)&& intent.hasExtra(EXTRA_CONTRIB)){
           mAlist = intent.getParcelableExtra(LIST_EXTRA);
           mListBasedContribution = intent.getParcelableExtra(EXTRA_CONTRIB);
        }
        setTitle();
        setUpViewModel();
    }

    private void setTitle() {
        String memberName = mListBasedContribution.getName();
        String listName = mAlist.getName();
        setTitle(memberName + "/" + listName);
    }

    private void setUpViewModel() {

        HistViewModelFactory factory = new HistViewModelFactory(AppDatabase.getDatabaseInstance(this)
                ,mAlist.getListId(), mListBasedContribution.getMemberId());
        HistViewModel viewModel = ViewModelProviders.of(this,factory).get(HistViewModel.class);
        viewModel.getmHistoryList().observe(this, new Observer<List<History>>() {
            @Override
            public void onChanged(@Nullable List<History> histories) {
                mAdapter.setHistoryList(histories);
            }
        });



    }


    @Override
    public void onOptionViewClicked(final History history, View view) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.member_option_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.action_edit:
                        // Start activity of Add ListBasedContribution and parse the History object and the Member's name
                        Intent intent = new Intent(HistoryActivity.this, Add_a_contribution.class);
                        intent.putExtra(EXTRA_HISTORY,history);
                        intent.putExtra(EXTRA_CONTRIB, mListBasedContribution);
                        startActivity(intent);
                        break;
                    case R.id.action_delete:
                        // Delete a History object
                        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase db = AppDatabase.getDatabaseInstance(getApplicationContext());
                                db.historyDoa().delete(history);

                                // Add the opposite of the amount to Member's contribution in list

                                int newAmount = mListBasedContribution.getAmount()-history.getAmount();
                                AMemberInAList aMemberInAList = new AMemberInAList(
                                        mListBasedContribution.getMemberId()
                                        ,mAlist.getListId()
                                        ,newAmount);
                                db.a_member_in_a_list_dao().update_a_member_in_a_list(aMemberInAList);
                            }
                        });
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
