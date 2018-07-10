package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.android.hubert.Adapters.Display_a_list_adapter;
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_member_in_a_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Contribution;
import com.example.android.hubert.View_models.Main2_view_model;
import com.example.android.hubert.R;

import java.util.ArrayList;
import java.util.List;

public class Display_a_list extends AppCompatActivity {
    RecyclerView rv;
    Display_a_list_adapter adapter;
    int listId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_a_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.rv_contributions);
        adapter = new Display_a_list_adapter(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(Display_a_list.this,Add_a_contribution.class);
                addIntent.putExtra(Display_diff_list.LIST_ID_EXTRA,listId);
                startActivity(addIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getListIdFromIntent();
        setupViewModel();
        Log.d("Oncreate","Oncreate method is called");
    }

    private void getListIdFromIntent() {
        if (getIntent().hasExtra(Display_diff_list.LIST_ID_EXTRA)) {
            listId = getIntent().getIntExtra(Display_diff_list.LIST_ID_EXTRA, Display_diff_list.DEFAULT_LIST_ID);
        }
    }

    private void setupViewModel() {
        Main2_view_model view_model2 = ViewModelProviders.of(this).get(Main2_view_model.class);

        view_model2.getMembersInList(listId).observe(this, new Observer<List<A_member_in_a_list>>() {
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
                        adapter.setContributions(contributions);
                    }
                });

            }
        });
    }

}
