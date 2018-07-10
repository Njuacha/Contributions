package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_member_in_a_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;
import com.example.android.hubert.View_models.Main3_view_model;

import java.util.List;

public class Add_a_contribution extends AppCompatActivity {
    Spinner memberSpinner;
    EditText et_amount;
    private AppDatabase mdb;
    A_member_in_a_list a_member_in_a_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_contribution);
        memberSpinner = findViewById(R.id.sp_members);
        et_amount = findViewById(R.id.et_amount);
        mdb = AppDatabase.getDatabaseInstance(this);
        setUpViewModel();
    }

    public void add(View view) {
        add_a_member_in_a_list();
        finish();
    }

    private void setUpViewModel()
    {
        Main3_view_model view_model;
        view_model = ViewModelProviders.of(this).get(Main3_view_model.class);
        view_model.getAllMembers().observe(this, new Observer<List<Member>>() {
            @Override
            public void onChanged(@Nullable List<Member> members) {
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,members);
                memberSpinner.setAdapter(adapter);
            }
        });
    }

    private void add_a_member_in_a_list(){
        Member member = (Member)memberSpinner.getSelectedItem();
        if (member == null){
            Toast.makeText(this,"No member yet",Toast.LENGTH_LONG);
            return;
        }
        int listId = getIntent().getIntExtra(Display_diff_list.LIST_ID_EXTRA,1);
        int amount = Integer.parseInt(et_amount.getText().toString());
        a_member_in_a_list = new A_member_in_a_list(member.getId(),listId,amount);

        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mdb.a_member_in_a_list_dao().insert_a_member_in_a_list(a_member_in_a_list);
            }
        });
    }

}
