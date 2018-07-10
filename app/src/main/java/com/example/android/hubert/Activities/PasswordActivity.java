package com.example.android.hubert.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.DatabaseClasses.A_member_in_a_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        // Temporarily i would skip this
        startActivity(new Intent(PasswordActivity.this,Display_diff_list.class));
        finish();
    }

    public void enter(View view) {
        Intent enter = new Intent(PasswordActivity.this,Display_diff_list.class);
        startActivity(enter);
    }

}
