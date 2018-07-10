package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.android.hubert.Adapters.Display_diff_list_adapter;
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.Listname_dialog;
import com.example.android.hubert.View_models.Main_view_model;
import com.example.android.hubert.R;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class Display_diff_list extends AppCompatActivity implements Display_diff_list_adapter.ItemClickListerner, Display_diff_list_adapter.ItemLongClickListerner {
    private static final String TAG = Display_diff_list.class.getSimpleName();
    public static final String LIST_ID_EXTRA = "list Id";
    Display_diff_list_adapter adapter;
    RecyclerView recyclerView;
    AppDatabase mdb;
    public final static int DEFAULT_LIST_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_diff_list);
        recyclerView = findViewById(R.id.rv_all_list);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);
        adapter = new Display_diff_list_adapter(this,this,this);
        mdb = AppDatabase.getDatabaseInstance(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        setupViewModel();
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
                open_edit_list_name_dialog(DEFAULT_LIST_ID);
                break;
            case R.id.add_member:
                openAddMemberActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAddMemberActivity() {
        Intent addMemberIntent = new Intent(Display_diff_list.this,AddMember.class);
        startActivity(addMemberIntent);
    }

    private void open_edit_list_name_dialog(int listId){

        Listname_dialog dialog = new Listname_dialog();
        Bundle bundle = new Bundle();
        bundle.putInt("listId",listId);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "edit_list_name_dialog");
    }



    @Override
    public void onItemCLickListerner(int itemId) {
        Intent intent = new Intent(Display_diff_list.this, Display_a_list.class);
        intent.putExtra(LIST_ID_EXTRA, itemId);
        startActivity(intent);
    }



    private void setupViewModel(){
        Main_view_model view_model = ViewModelProviders.of(this).get(Main_view_model.class);
        view_model.getLists().observe(this, new Observer<List<A_list>>() {
            @Override
            public void onChanged(@Nullable List<A_list> a_lists) {

                adapter.setListEntries(a_lists);
            }
        });
    }

    @Override
    public void onItemLongClickListerner(int itemId) {
        open_edit_list_name_dialog(itemId);
    }
}
