package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AMemberInAList;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.ListBasedContribution;
import com.example.android.hubert.DatabaseClasses.History;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.DialogFragments.DatePickerFragment;
import com.example.android.hubert.R;
import com.example.android.hubert.Utils.MyMobileAds;
import com.example.android.hubert.ViewModels.AddContribViewModelFactory;
import com.example.android.hubert.ViewModels.AddContributionViewModel;
import com.google.android.gms.ads.AdView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static com.example.android.hubert.Activities.DisplayAList.EXTRA_CONTRIB;
import static com.example.android.hubert.Activities.DisplayAList.EXTRA_LIST_ID;
import static com.example.android.hubert.Activities.DisplayAList.EXTRA_SUB;
import static com.example.android.hubert.Activities.HistoryActivity.EXTRA_HISTORY;
import static com.example.android.hubert.Activities.MemberActivity.EXTRA_LIST_NAME;
import static com.example.android.hubert.PlaceholderFragment.LIST_EXTRA;

public class AddAContribution extends AppCompatActivity implements DatePickerFragment.DateSetListener {

    private final int NEW_CONTR = 1;
    private final int ADD_CONTR = 2;
    private final int EDIT_CONTR = 3;

    private SearchableSpinner memberSpinner;
    private EditText etAmount;
    private TextView tvEmpty;
    private TextView tvDate;
    private Button button;
    private AppDatabase mdb;
    private AMemberInAList memberInAList;
    private Alist mAlist;

    private int mListId;
    private ListBasedContribution mListBasedContribution;
    private History history;
    private boolean isSubtract = false;
    private int mAction = NEW_CONTR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdb = AppDatabase.getDatabaseInstance(this);
        Intent intent  = getIntent();

        if (intent.hasExtra(EXTRA_CONTRIB) && intent.hasExtra(EXTRA_LIST_NAME) ){

            setTitle(intent.getStringExtra(EXTRA_LIST_NAME));

            if (intent.hasExtra(EXTRA_LIST_ID)){// This is the case of Adding a new ListBasedContribution on an already existing one
                mListId = intent.getIntExtra(EXTRA_LIST_ID,0);
                mListBasedContribution = intent.getParcelableExtra(EXTRA_CONTRIB);
                // We are adding or subtracting to an older mListBasedContribution so it is not new
                mAction = ADD_CONTR;
                // We populate the UI with name and set it to be fixed. Also we set the button to subtract if it is a subtraction case
                populate1(intent.hasExtra(EXTRA_SUB));
            }else if (intent.hasExtra(EXTRA_HISTORY)){// This is the case when editing formerly added mListBasedContribution
                history = intent.getParcelableExtra(EXTRA_HISTORY);
                mListBasedContribution = intent.getParcelableExtra(EXTRA_CONTRIB);
                mAction = EDIT_CONTR;
                populate2();
            }

        }else if (intent.hasExtra(LIST_EXTRA)){ // This is the case of adding new contribution in a list
            mAlist = intent.getParcelableExtra(LIST_EXTRA);
            setTitle(mAlist.getName());
            setUpViewModel();
        }


    }

    // This is called to populate the UI with name, date, amount button name(can be add or subtract)
    private void populate2() {
        setContentView(R.layout.activity_add_a_contribution);
        instantiateSomeViews();
        button = findViewById(R.id.bt_add);


        List<Member> members = new ArrayList<>();

        members.add(new Member(history.getMemberId()
                , mListBasedContribution.getName(),0));

        setItemsOnSpinner(members);

        tvDate.setText(history.getDate());

        int amount = history.getAmount();
        etAmount.setText(String.valueOf(Math.abs(amount)));

        if(amount<0){
            isSubtract = true;
            button.setText(R.string.subtract);
        }



    }

    // This is called to populate the UI with the name and a default date
    private void populate1(boolean isSubtact) {

        setContentView(R.layout.activity_add_a_contribution);
        instantiateSomeViews();
        tvDate.setText(getDate());

        button = findViewById(R.id.bt_add);

        List<Member> members = new ArrayList<>();

        members.add(new Member(mListBasedContribution.getMemberId()
                , mListBasedContribution.getName(),0));


        setItemsOnSpinner(members);

        if (isSubtact){
            this.isSubtract = isSubtact;
            button.setText(R.string.subtract);
        }


    }

    private void setUpViewModel() {

        AddContribViewModelFactory factory = new AddContribViewModelFactory(mdb, mAlist.getListId());
        final AddContributionViewModel viewModel = ViewModelProviders.of(this, factory).get(AddContributionViewModel.class);

        viewModel.getAllMembers().observe(this, new Observer<List<Member>>() {
            @Override
            public void onChanged(@Nullable List<Member> members) {
                assert members != null;
                members.removeAll(viewModel.getMembersAlreadyInList());
                if (members.size() == 0) {
                    setContentView(R.layout.empty);
                    tvEmpty = findViewById(R.id.tv_explain_emptiness);
                    tvEmpty.setText(R.string.add_contribution_empty);
                } else {
                    setContentView(R.layout.activity_add_a_contribution);
                    instantiateSomeViews();
                    tvDate.setText(getDate());
                    if (!members.contains(new Member(getString(R.string.choose_a_name), 0))){
                        members.add(0,new Member(getString(R.string.choose_a_name), 0));
                    }
                    setItemsOnSpinner(members);
                }
            }
        });
    }

    private void instantiateSomeViews() {
        memberSpinner = findViewById(R.id.sp_members);
        etAmount = findViewById(R.id.et_amount);
        tvDate = findViewById(R.id.tv_date);
        // Initialize MyMobileAds
        AdView adView = findViewById(R.id.adView5);
        MyMobileAds.loadAdIntoAdView(this,adView);
    }

    public void add(View view) {

        final String amt = etAmount.getText().toString();
        // If the user doesn't enter an amount then the return after showing a toast
        if (TextUtils.isEmpty(amt)) {
            Toast.makeText(this, R.string.enter_an_amount, Toast.LENGTH_SHORT).show();
            return;
        }
        // If the user doesn't choose a name then return after showing a toast
        if (((Member)memberSpinner.getSelectedItem()).getName().equals(getString(R.string.choose_a_name))){
            Toast.makeText(this, R.string.choose_a_name, Toast.LENGTH_SHORT).show();
            return;
        }

        // Add mListBasedContribution in a background thread
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                Member member = (Member) memberSpinner.getSelectedItem();
                int memberId = member.getMemberId();            // Get the memberId
                String date = tvDate.getText().toString();     // Get the date
                int amount = Integer.parseInt(amt);  // Get the amount
                int amt = isSubtract?-amount:amount;            // Get amount to be added or subtracted

                switch (mAction){
                    case NEW_CONTR:
                        memberInAList = new AMemberInAList(memberId, mAlist.getListId(), amount);
                        mdb.a_member_in_a_list_dao().insert_a_member_in_a_list(memberInAList);

                        // Write a record of a mListBasedContribution in History table
                        mdb.historyDoa().insertContributionWithDate(new History(mAlist.getListId()
                                , memberId, date, amount));
                        break;
                    case ADD_CONTR:
                        int newAmount = mListBasedContribution.getAmount() + amt;

                        memberInAList = new AMemberInAList(memberId, mListId,newAmount);
                        mdb.a_member_in_a_list_dao().update_a_member_in_a_list(memberInAList);

                        // Write a record of a mListBasedContribution in History table
                        mdb.historyDoa().insertContributionWithDate(new History(mListId
                                , memberId, date, amt));
                        break;
                    case EDIT_CONTR:
                        /*
                           Update ListBasedContribution
                           if the amount is changed
                        */
                        if(history.getAmount() != amt){
                            // First obtain current amount
                            int currentAmount = mListBasedContribution.getAmount();
                            // Undo previous AddContrib effect
                            int previousAmount = currentAmount - history.getAmount();
                            // Consider now the AddContrib effect
                            int amountAfterEdit = previousAmount + amt;

                            memberInAList = new AMemberInAList(history.getMemberId(),history.getListId(),amountAfterEdit);
                            mdb.a_member_in_a_list_dao().update_a_member_in_a_list(memberInAList);
                        }



                        // Update History
                        history.setAmount(amt);
                        history.setDate(date);
                        mdb.historyDoa().updateHistory(history);

                        break;

                }

            }
        });

        finish();
    }

    private String getDate(){
        Calendar c = Calendar.getInstance();
        return  String.format("%02d/%02d/%d"
                ,c.get(Calendar.DAY_OF_MONTH)
                ,c.get(Calendar.MONTH) +1
                ,c.get(Calendar.YEAR));
    }


    public void showDatePicker(View view) {
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.setDateSetListener(this);
        datePickerDialog.show(getSupportFragmentManager(),"datePicker");
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        tvDate.setText(String.format("%02d/%02d/%d", dayOfMonth,month,year));
    }

    private void setItemsOnSpinner(final List<Member> members){

        ArrayAdapter arrayAdapter;

        arrayAdapter = new ArrayAdapter<Member>(this
                , android.R.layout.simple_spinner_item, members);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        memberSpinner.setAdapter(arrayAdapter);

    }




}
