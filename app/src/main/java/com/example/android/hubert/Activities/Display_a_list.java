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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hubert.Adapters.ListContributionsAdapter;
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.ListBasedContribution;
import com.example.android.hubert.View_model_classes.LIstContribViewModelFactory;
import com.example.android.hubert.View_model_classes.ListContribViewModel;
import com.example.android.hubert.R;

import java.util.List;

import static com.example.android.hubert.Activities.MainActivity.LIST_EXTRA;

public class Display_a_list extends AppCompatActivity implements ListContributionsAdapter.OnCLickListeners{
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    public static final String EXTRA_LIST_ID = "list Id";
    public static final String EXTRA_CONTRIB = "contribution";
    public static final String EXTRA_SUB = "subtract";
    private RecyclerView mRv;
    private ListContributionsAdapter mAdapter;
    private Alist mAlist;
    private ListBasedContribution mListBasedContribution;
    private AppDatabase mDb;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_a_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRv = findViewById(R.id.rv_contributions);
        textView = findViewById(R.id.tv_explain_emptiness);
        mAdapter = new ListContributionsAdapter(this,this);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRv.setAdapter(mAdapter);
        // instantiate the database variable
        mDb = AppDatabase.getDatabaseInstance(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(Display_a_list.this,Add_a_contribution.class);
                addIntent.putExtra(LIST_EXTRA,mAlist);
                startActivity(addIntent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getListFromIntent();
        setupViewModel();
        setTitle(mAlist.getName());

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
        String fileName = mAlist.getName() + Share.FILE_EXTENSION;
        String info = getAllListInfo(mAlist.getName());
        Share share = new Share(fileName,info,this);
        share.show();
    }

    private String getAllListInfo(String listName){
        StringBuilder allInfo = new StringBuilder();
        String a_Part;
        // Putting the heading which includes the listName and the date
        a_Part = String.format("Status of \"%s\" as of %s%n%n",listName,Share.date);
        allInfo.append(a_Part);
        // Putting all the contributions inside allInfo
        writeContributions(allInfo);
        // Putting all the totalAmount
        a_Part = String.format("%nTotal Amount = %,d%n%nCreated from:ContributionsApp",mAdapter.getTotalAmount());
        allInfo.append(a_Part);
        return allInfo.toString();
    }


    private void writeContributions(StringBuilder allInfo) {

        int longestNameLength = mAdapter.getLongestNameLength();
        String name_label = getString(R.string.name_label);

        int spacing = (name_label.length()>longestNameLength?name_label.length():longestNameLength) + 2 ;

        allInfo.append(String.format("%-" + spacing + "s%s%n",name_label,getString(R.string.amount_label)));
        // If there are no contributions return in a list then return false
        if(mAdapter.getmListBasedContributions() == null){
            return;
        }
        for (ListBasedContribution listBasedContribution : mAdapter.getmListBasedContributions()){
            String name_amount = String.format("%-" + spacing + "s%,d%n"
                    , listBasedContribution.getName(), listBasedContribution.getAmount());
            allInfo.append(name_amount);
        }
    }

    private void getListFromIntent() {
        if (getIntent().hasExtra(LIST_EXTRA)) {
            mAlist = getIntent().getParcelableExtra(LIST_EXTRA);
        }
    }

    private void setupViewModel() {
        LIstContribViewModelFactory factory = new LIstContribViewModelFactory(mDb,mAlist.getListId());
        ListContribViewModel view_model2 = ViewModelProviders.of(this,factory).get(ListContribViewModel.class);
        view_model2.getContributionsInList().observe(this, new Observer<List<ListBasedContribution>>() {
            @Override
            public void onChanged(@Nullable List<ListBasedContribution> listBasedContributions) {
                mAdapter.setmListBasedContributions(listBasedContributions);
                if ((listBasedContributions != null ? listBasedContributions.size() : 0) == 0){
                    textView.setText(R.string.add_member_contrib);
                    textView.setVisibility(View.VISIBLE);
                    mRv.setVisibility(View.INVISIBLE);
                }else{
                    mRv.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // Opens up a popup menu when the three vertical dot(optionView) is clicked
    @Override
    public void onOptionTextViewClicked(final ListBasedContribution listBasedContribution, View view) {
        mListBasedContribution = listBasedContribution;
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.add_amount);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            // Opens up the same dialog when either the add or subtract option is clicked
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
               // For the case of add or subtract we open up add listBasedContribution activity
                if( (id == R.id.action_add) || (id == R.id.action_subtract) ){
                    Intent intent = new Intent(Display_a_list.this,Add_a_contribution.class);
                    intent.putExtra(EXTRA_LIST_ID,mAlist.getListId());
                    intent.putExtra(EXTRA_CONTRIB, listBasedContribution);
                    if(id == R.id.action_subtract){
                        intent.putExtra(EXTRA_SUB,true);
                    }

                    startActivity(intent);

                }else if(id == R.id.action_delete){ // For the case of delete we try to get the list Id and memberId to remove the listBasedContribution
                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.a_member_in_a_list_dao().deleteAContribution(mListBasedContribution.getMemberId(),mAlist.getListId());
                            mDb.historyDoa().delete(mAlist.getListId(), mListBasedContribution.getMemberId());
                        }
                    });
                }

                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public void onItemClicked(ListBasedContribution contrib) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra(LIST_EXTRA,mAlist);
        intent.putExtra(EXTRA_CONTRIB,contrib);
        startActivity(intent);
    }



}
