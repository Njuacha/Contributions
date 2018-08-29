package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AMemberInAList;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Contribution;
import com.example.android.hubert.DatabaseClasses.History;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.DialogFragments.DatePickerFragment;
import com.example.android.hubert.R;
import com.example.android.hubert.View_model_classes.AddContribViewModelFactory;
import com.example.android.hubert.View_model_classes.AddContributionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static com.example.android.hubert.Activities.Display_a_list.EXTRA_CONTRIB;
import static com.example.android.hubert.Activities.Display_a_list.EXTRA_LIST_ID;
import static com.example.android.hubert.Activities.Display_a_list.EXTRA_SUB;
import static com.example.android.hubert.Activities.MainActivity.LIST_EXTRA;

public class Add_a_contribution extends AppCompatActivity implements DatePickerFragment.DateSetListener {

    private Spinner memberSpinner;
    private EditText et_amount;
    private TextView tv_empty;
    private TextView tv_date;
    private Button button;
    private AppDatabase mdb;
    private AMemberInAList AMemberInA_list;
    private Alist mAlist;

    private int listId;
    private Contribution contribution;
    private boolean isNew = true;
    private boolean isSubtract = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdb = AppDatabase.getDatabaseInstance(this);
        getListIdAndNameFromIntent();
        Intent intent  = getIntent();

        if (intent.hasExtra(EXTRA_LIST_ID) && intent.hasExtra(EXTRA_CONTRIB)){
            listId = intent.getIntExtra(EXTRA_LIST_ID,0);
            contribution = intent.getParcelableExtra(EXTRA_CONTRIB);
            // We are adding or subtracting to an older contribution so it is not new
            isNew = false;
            // We populate the UI with name and set it to be fixed. Also we set the button to subtract if it is a subtraction case
            populate(intent.hasExtra(EXTRA_SUB));
        }else {
            setUpViewModel();
        }

    }

    private void populate(boolean isSubtact) {
        this.isSubtract = isSubtact;
        setContentView(R.layout.activity_add_a_contribution);
        instantiateSomeViews();
        button = findViewById(R.id.bt_add);

        List<Member> members = new ArrayList<>();

        members.add(new Member(contribution.getMemberId()
                ,contribution.getName()));

        memberSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext()
                , android.R.layout.simple_spinner_item, members));

        if (isSubtact){
            Log.d("Tag0",String.valueOf(isSubtact));
            button.setText("SUBTRACT");
        }


    }

    private void setUpViewModel() {

        AddContribViewModelFactory factory = new AddContribViewModelFactory(mdb, mAlist.getId());
        final AddContributionViewModel viewModel = ViewModelProviders.of(this, factory).get(AddContributionViewModel.class);

        viewModel.getAllMembers().observe(this, new Observer<List<Member>>() {
            @Override
            public void onChanged(@Nullable List<Member> members) {
                assert members != null;
                members.removeAll(viewModel.getMembersAlreadyInList());
                if (members.size() == 0) {
                    setContentView(R.layout.empty);
                    tv_empty = findViewById(R.id.tv_explain_emptiness);
                    tv_empty.setText(R.string.add_contribution_empty);
                } else {
                    setContentView(R.layout.activity_add_a_contribution);
                    // TODO: put this four lines into a method since it is reused
                   instantiateSomeViews();
                    memberSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext()
                            , android.R.layout.simple_spinner_item, members));
                }
            }
        });
    }

    private void instantiateSomeViews() {
        memberSpinner = findViewById(R.id.sp_members);
        et_amount = findViewById(R.id.et_amount);
        tv_date = findViewById(R.id.tv_date);
        tv_date.setText(getDate());
    }

    public void add(View view) {

        final String amt = et_amount.getText().toString();
        // If the user doesn't enter an amount then the return false after showing a toast
        if (TextUtils.isEmpty(amt)) {
            Toast.makeText(this, "Enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add contribution in a background thread
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                Member member = (Member) memberSpinner.getSelectedItem();
                int amount = Integer.parseInt(amt.toString());
                int memberId = member.getMemberId();

                if (isNew){
                    AMemberInA_list = new AMemberInAList(memberId, mAlist.getId(), amount);
                    mdb.a_member_in_a_list_dao().insert_a_member_in_a_list(AMemberInA_list);

                    // Write a record of a contribution in History table
                    mdb.historyDoa().insertContributionWithDate(new History(mAlist.getId()
                            , memberId, tv_date.getText().toString(), amount));

                }else{
                    int amt = isSubtract?-amount:amount;
                    int newAmount = contribution.getAmount() + (amt);
                    Log.d("Tag",String.valueOf(isSubtract));
                    AMemberInA_list = new AMemberInAList(memberId,listId,newAmount);
                    mdb.a_member_in_a_list_dao().update_a_member_in_a_list(AMemberInA_list);


                    // Write a record of a contribution in History table
                    mdb.historyDoa().insertContributionWithDate(new History(listId
                            , memberId, tv_date.getText().toString(), amt));

                }



            }
        });

        finish();
    }

    private void getListIdAndNameFromIntent() {
        if (getIntent().hasExtra(LIST_EXTRA)) {
            mAlist = getIntent().getParcelableExtra(LIST_EXTRA);
        }
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
        tv_date.setText(String.format("%02d/%02d/%d", dayOfMonth,month,year));
    }
}
