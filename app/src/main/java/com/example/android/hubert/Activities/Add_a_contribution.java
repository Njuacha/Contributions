package com.example.android.hubert.Activities;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_member_in_a_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

import java.util.ArrayList;
import java.util.List;

public class Add_a_contribution extends AppCompatActivity {
    Spinner memberSpinner;
    EditText et_amount;
    TextView tv_empty;
    private AppDatabase mdb;
    A_member_in_a_list a_member_in_a_list;
    int mListId;
    String mListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdb = AppDatabase.getDatabaseInstance(this);
        getListIdAndNameFromIntent();
        setUpViewModel();
    }

    private void setUpViewModel() {
      AppExecutors.getsInstance().diskIO().execute(new Runnable() {
          @Override
          public void run() {
              List<Member> allMembers = mdb.member_dao().loadAllMembers();
              List<Member> membersInlist = mdb.a_member_in_a_list_dao().loadMembersInList(mListId);
              allMembers.removeAll(membersInlist);

              final List<Member> members = allMembers;
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      if(members.size() == 0){
                          setContentView(R.layout.empty);
                          tv_empty = findViewById(R.id.tv_explain_emptiness);
                          tv_empty.setText(R.string.add_contribution_empty);
                      }else{
                          setContentView(R.layout.activity_add_a_contribution);
                          memberSpinner = findViewById(R.id.sp_members);
                          et_amount = findViewById(R.id.et_amount);
                          memberSpinner.setAdapter(new ArrayAdapter<Member>(getApplicationContext()
                                  , android.R.layout.simple_spinner_item,members));
                      }

                  }
              });
          }
      });
    }

    public void add(View view) {
        add_a_member_in_a_list();
        finish();
    }

    private void getListIdAndNameFromIntent() {
        if (getIntent().hasExtra(Display_diff_list.LIST_ID_EXTRA) && getIntent().hasExtra(Display_diff_list.LIST_NAME_EXTRA)) {
            mListId = getIntent().getIntExtra(Display_diff_list.LIST_ID_EXTRA, Display_diff_list.DEFAULT_LIST_ID);
            mListName = getIntent().getStringExtra(Display_diff_list.LIST_NAME_EXTRA);
        }
    }


    private void add_a_member_in_a_list() {
        Member member = (Member) memberSpinner.getSelectedItem();
        int amount = Integer.parseInt(et_amount.getText().toString());
        a_member_in_a_list = new A_member_in_a_list(member.getMemberId(), mListId, amount);

        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mdb.a_member_in_a_list_dao().insert_a_member_in_a_list(a_member_in_a_list);
            }
        });
    }

}
