package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.android.hubert.Adapters.ContributionsAdapter;
import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DialogFragments.Listname_dialog;
import com.example.android.hubert.View_model_classes.ContributionsViewModel;
import com.example.android.hubert.R;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class Display_diff_list extends AppCompatActivity implements ContributionsAdapter.ItemClickListerners {
    private static final String TAG = Display_diff_list.class.getSimpleName();
    public static final String LIST_ID_EXTRA = "list Id";
    public static final String LIST_NAME_EXTRA = "list name";
    private static final String DEFAULT_LIST_NAME = "No Name";
    ContributionsAdapter adapter;
    RecyclerView recyclerView;
    TextView tvEmpty;
    AppDatabase mdb;
    public final static int DEFAULT_LIST_ID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_display_diff_list);
        super.onCreate(savedInstanceState);

        recyclerView = findViewById(R.id.rv_all_list);
        tvEmpty = findViewById(R.id.tv_explain_emptiness);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);

        adapter = new ContributionsAdapter(this,this);

        mdb = AppDatabase.getDatabaseInstance(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        setAssociationName();

        setupViewModel();
    }

    private void setAssociationName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String associationName = sharedPreferences.getString(getString(R.string.pref_association_name_key)
                ,getString(R.string.pref_association_name_label));
        setTitle(associationName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.add_list:
                open_edit_list_name_dialog(DEFAULT_LIST_ID,DEFAULT_LIST_NAME);
                return true;
            case R.id.add_member:
                openAddMemberActivity();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this,MainSettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAddMemberActivity() {
        Intent addMemberIntent = new Intent(Display_diff_list.this,MainActivity.class);
        startActivity(addMemberIntent);
    }

    private void open_edit_list_name_dialog(int listId, String name){

        Listname_dialog dialog = new Listname_dialog();
        Bundle bundle = new Bundle();
        bundle.putInt(LIST_ID_EXTRA,listId);
        bundle.putString(LIST_NAME_EXTRA,name);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "edit_list_name_dialog");
    }

    @Override
    public void onItemCLicked(int itemId, String name) {
        Intent intent = new Intent(Display_diff_list.this, Display_a_list.class);
        intent.putExtra(LIST_NAME_EXTRA, name);
        intent.putExtra(LIST_ID_EXTRA, itemId);
        startActivity(intent);
    }
    
    private void setupViewModel(){
        ContributionsViewModel view_model = ViewModelProviders.of(this).get(ContributionsViewModel.class);
        view_model.getLists().observe(this, new Observer<List<A_list>>() {
            @Override
            public void onChanged(@Nullable List<A_list> a_lists) {
                if (a_lists.size() == 0){
                    setContentView(R.layout.empty);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText(R.string.no_list_available);
                }else{
                    tvEmpty.setVisibility(View.INVISIBLE);
                    adapter.setListEntries(a_lists);
                }
            }
        });
    }

    @Override
    public void onItemLongClicked(int itemId, String name) {
        open_edit_list_name_dialog(itemId, name);
    }

    @Override
    public void onOptionTextViewClicked(int itemId, String name, View view) {
        final int listId = itemId;
        final String listName = name;
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.list);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_details:
                        Intent showSummaryIntent = new Intent(Display_diff_list.this,SummaryActivity.class);
                        showSummaryIntent.putExtra(LIST_ID_EXTRA,listId);
                        showSummaryIntent.putExtra(LIST_NAME_EXTRA,listName);
                        startActivity(showSummaryIntent);
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }

}
