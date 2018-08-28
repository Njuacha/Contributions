package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AMemberInAList;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.History;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;
import com.example.android.hubert.View_model_classes.AddContribViewModelFactory;
import com.example.android.hubert.View_model_classes.AddContributionViewModel;

import java.util.Date;
import java.util.List;

import static com.example.android.hubert.Activities.MainActivity.LIST_EXTRA;

public class Add_a_contribution extends AppCompatActivity {
    private Spinner memberSpinner;
    private EditText et_amount;
    private TextView tv_empty;
    private AppDatabase mdb;
    private AMemberInAList AMemberInA_list;
    private Alist mAlist;

    //    TODO: Find a way to allow a user chooce between the default time or another time as the time per the contribution
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdb = AppDatabase.getDatabaseInstance(this);
        getListIdAndNameFromIntent();
        setUpViewModel();
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
                    memberSpinner = findViewById(R.id.sp_members);
                    et_amount = findViewById(R.id.et_amount);
                    memberSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext()
                            , android.R.layout.simple_spinner_item, members));
                }
            }
        });
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
                AMemberInA_list = new AMemberInAList(memberId, mAlist.getId(), amount);
                mdb.a_member_in_a_list_dao().insert_a_member_in_a_list(AMemberInA_list);
                // Write a record of a contribution in History table
                mdb.historyDoa().insertContributionWithDate(new History(mAlist.getId(), memberId, new Date(), amount));
            }
        });

        finish();
    }

    private void getListIdAndNameFromIntent() {
        if (getIntent().hasExtra(LIST_EXTRA)) {
            mAlist = getIntent().getParcelableExtra(LIST_EXTRA);
        }
    }


}
