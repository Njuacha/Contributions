package com.example.android.hubert.Activities;

import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Group;
import com.example.android.hubert.R;


public class GroupActivity extends AppCompatActivity {

    EditText mGroupNameText;
    Button mCreateGroupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mGroupNameText = findViewById(R.id.group_name);
        mCreateGroupBtn = findViewById(R.id.create_group);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mCreateGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mGroupNameText.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(GroupActivity.this,"Enter the group name",Toast.LENGTH_LONG).show();
                } else {
                    new AsyncTask<Void,Void,Void>(){

                        @Override
                        protected Void doInBackground(Void... voids) {
                            // Insert the new Group Created
                            AppDatabase db = AppDatabase.getDatabaseInstance(getApplicationContext());
                            db.groupDoa().insertGroup(new Group(name));
                            // Update the preference of groupId and groupName
                            int groupId = db.groupDoa().getNumberOfGroups();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.groupName),name);
                            editor.putInt(getString(R.string.group_id),groupId);
                            editor.commit();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            startActivity(new Intent(GroupActivity.this,MainActivity.class).putExtra(Intent.EXTRA_TEXT,name));
                            finish();
                            super.onPostExecute(aVoid);
                        }
                    }.execute();



                }

            }
        });

    }
}
