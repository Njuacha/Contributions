package com.example.android.hubert.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

import static com.example.android.hubert.Activities.MainActivity.EXTRA_MEMBER;

public class AddMember extends AppCompatActivity {
    private EditText et_member_name;
    private boolean isOldMember = false;
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        et_member_name = findViewById(R.id.et_member_name);

        Intent intent = getIntent();
        if( intent != null && intent.hasExtra(EXTRA_MEMBER)){
            member = intent.getParcelableExtra("member");
            et_member_name.setText(member.getName());
            isOldMember = true;
        }
    }

    public void add(View view) {
        final String name = et_member_name.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Enter member's name",Toast.LENGTH_SHORT).show();
            return ;
        }

        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase mDb= AppDatabase.getDatabaseInstance(getApplicationContext());
                // Get the name in edit text

                if(isOldMember){
                    // Set the new name to member object
                    member.setName(name);
                    mDb.member_dao().updateMember(member);
                }else{
                    mDb.member_dao().insertMember(new Member(name));
                }
            }
        });
        finish();
    }
}
