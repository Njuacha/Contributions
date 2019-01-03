package com.example.android.hubert.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

import java.util.List;

import static com.example.android.hubert.Activities.MainActivity.DEFAULT_GROUP_ID;

/**
 * Created by hubert on 8/16/18.
 */

public class MembersViewModel extends AndroidViewModel {
    private final LiveData<List<Member>> members;

    public MembersViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mdb = AppDatabase.getDatabaseInstance(this.getApplication());
        Context context = application.getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int groupId = sharedPreferences.getInt(context.getString(R.string.group_id),DEFAULT_GROUP_ID);
        members = mdb.memberDao().loadAllMembers(groupId);

    }

    public LiveData<List<Member>> getMembers() {
        return members;
    }
}
