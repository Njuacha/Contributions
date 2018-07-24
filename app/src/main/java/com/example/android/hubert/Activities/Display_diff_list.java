package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.android.hubert.Adapters.Display_diff_list_adapter;
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DialogFragments.Listname_dialog;
import com.example.android.hubert.View_model_classes.Main_view_model;
import com.example.android.hubert.R;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class Display_diff_list extends AppCompatActivity implements Display_diff_list_adapter.ItemClickListerner, Display_diff_list_adapter.ItemLongClickListerner, Display_diff_list_adapter.OptionTextViewClickListerner {
    private static final String TAG = Display_diff_list.class.getSimpleName();
    public static final String LIST_ID_EXTRA = "list Id";
    public static final String LIST_NAME_EXTRA = "list name";
    private static final String DEFAULT_LIST_NAME = "No Name";
    Display_diff_list_adapter adapter;
    RecyclerView recyclerView;
    TextView tvEmpty;
    AppDatabase mdb;
    public final static int DEFAULT_LIST_ID = -1;
    Context mContext;
    Display_diff_list_adapter.ItemClickListerner mClickListerner;
    Display_diff_list_adapter.ItemLongClickListerner mLongClickListerner;
    Display_diff_list_adapter.OptionTextViewClickListerner mOptionTvClickListerner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAssociationName();
        mContext = this;
        mClickListerner = this;
        mLongClickListerner = this;
        mOptionTvClickListerner = this;
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
        Intent addMemberIntent = new Intent(Display_diff_list.this,AddMember.class);
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
        Main_view_model view_model = ViewModelProviders.of(this).get(Main_view_model.class);
        view_model.getLists().observe(this, new Observer<List<A_list>>() {
            @Override
            public void onChanged(@Nullable List<A_list> a_lists) {
                if (a_lists.size() == 0){
                    setContentView(R.layout.empty);
                    tvEmpty = findViewById(R.id.tv_explain_emptiness);
                    tvEmpty.setText(R.string.no_list_available);
                }else{
                    setContentView(R.layout.activity_display_diff_list);
                    recyclerView = findViewById(R.id.rv_all_list);
                    DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
                    recyclerView.addItemDecoration(decoration);
                    adapter = new Display_diff_list_adapter(mContext,mClickListerner,mLongClickListerner, mOptionTvClickListerner);
                    mdb = AppDatabase.getDatabaseInstance(mContext);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerView.setAdapter(adapter);
                    /* adding the swipping functionality*/
                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                            AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    A_list a_list = adapter.getListEntries().get(viewHolder.getAdapterPosition());
                                    mdb.a_list_dao().delete_a_list(a_list);
                                }
                            });
                        }

                        @Override
                        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                            // We only want the active item
                            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){
                                if ( viewHolder instanceof Display_diff_list_adapter.ItemTouchHelperViewHolder){
                                    Display_diff_list_adapter.ItemTouchHelperViewHolder
                                            itemViewHolder = (Display_diff_list_adapter.ItemTouchHelperViewHolder) viewHolder;
                                    itemViewHolder.onItemSelected();
                                }
                            }
                            super.onSelectedChanged(viewHolder, actionState);
                        }

                        @Override
                        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                            super.clearView(recyclerView, viewHolder);
                            if ( viewHolder instanceof Display_diff_list_adapter.ItemTouchHelperViewHolder){
                                Display_diff_list_adapter.ItemTouchHelperViewHolder
                                        itemViewHolder = (Display_diff_list_adapter.ItemTouchHelperViewHolder) viewHolder;
                                itemViewHolder.onItemClear();
                            }
                        }

                    }).attachToRecyclerView(recyclerView);
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
