package com.example.android.hubert.Activities;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hubert.Adapters.Display_a_list_adapter;
import com.example.android.hubert.Adapters.Display_diff_list_adapter;
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_member_in_a_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Contribution;
import com.example.android.hubert.DialogFragments.Add_amount_dialog;
import com.example.android.hubert.View_model_classes.Main2ViewModelFactory;
import com.example.android.hubert.View_model_classes.Main2_view_model;
import com.example.android.hubert.R;

import java.util.List;

public class Display_a_list extends AppCompatActivity implements Display_a_list_adapter.OptionTextViewClickListerner, Add_amount_dialog.Add_amount_dialog_listener{
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    RecyclerView mRv;
    Display_a_list_adapter mAdapter;
    int mListId;
    String mListName;
    Contribution mContribution;
    AppDatabase mDb;
    boolean mIsAdd;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_a_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRv = findViewById(R.id.rv_contributions);
        textView = findViewById(R.id.tv_explain_emptiness);
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
                addIntent.putExtra(Display_diff_list.LIST_NAME_EXTRA,mListName);
                startActivity(addIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getListIdAndNameFromIntent();

        setupViewModel();
        setTitle(mListName);

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
                        Contribution contribution = mAdapter.getmContributions().get(viewHolder.getAdapterPosition());
                        A_member_in_a_list a_member = new A_member_in_a_list(contribution.getMemberId(),mListId,contribution.getAmount());
                        mDb.a_member_in_a_list_dao().deleteAContribution(a_member);
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

        }).attachToRecyclerView(mRv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String share = getResources().getString(R.string.share);
        menu.add(0,1,1,share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1){

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                   share();
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    share();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"Cann't share until you grant permission to write to external storage"
                            ,Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }

    }

    private void share(){
        String fileName = mListName + Share.FILE_EXTENSION;
        Share share = new Share(fileName,getAllListInfo(mListName),this);
        share.show();
        }

    private String getAllListInfo(String listName){
        StringBuilder allInfo = new StringBuilder();
        String a_Part;
        // Putting the heading which includes the listName and the date
        a_Part = String.format("Status of \"%s\" as of %s%n%n",listName,Share.date);
        allInfo.append(a_Part);
        // Putting all the contributions
        writeContributions(allInfo);
        // Putting all the totalAmount
        a_Part = String.format("%nTotal Amount = %,d%n%nCreated from:NkapJik",mAdapter.getTotalAmount());
        allInfo.append(a_Part);
        return allInfo.toString();
    }


    private void writeContributions(StringBuilder allInfo) {

        int longestNameLength = mAdapter.getLongestNameLength();
        String name_label = getString(R.string.name_label);

        int spacing = (name_label.length()>longestNameLength?name_label.length():longestNameLength) + 2 ;

        allInfo.append(String.format("%-" + spacing + "s%s%n",name_label,getString(R.string.amount_label)));

        for (Contribution contribution: mAdapter.getmContributions()){
            String name_amount = String.format("%-" + spacing + "s%,d%n"
                    ,contribution.getName(),contribution.getAmount());
            allInfo.append(name_amount);
        }
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
        view_model2.getContributionsInList().observe(this, new Observer<List<Contribution>>() {
            @Override
            public void onChanged(@Nullable List<Contribution> contributions) {

                if (contributions.size() == 0){
                    textView.setText("No contributions in list");
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.INVISIBLE);
                    mAdapter.setmContributions(contributions);
                }
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
               int memberId = mContribution.getMemberId();
               int newAmount =  mContribution.getAmount() + (mIsAdd?amount:-amount);
               A_member_in_a_list a_member_in_a_list = new A_member_in_a_list(memberId,mListId
                       ,newAmount);
               mDb.a_member_in_a_list_dao().update_a_member_in_a_list(a_member_in_a_list);
            }
        });
    }


}
