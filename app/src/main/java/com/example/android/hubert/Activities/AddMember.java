package com.example.android.hubert.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

public class AddMember extends AppCompatActivity {
    EditText et_member_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        et_member_name = findViewById(R.id.et_member_name);
    }

    public void add(View view) {
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getDatabaseInstance(getApplicationContext()).member_dao().insertMember(new Member(et_member_name.getText().toString()));
            }
        });
        finish();
    }
}
