package com.example.android.hubert.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.R;

import java.util.List;

import static com.example.android.hubert.Activities.MainActivity.DEFAULT_GROUP_ID;

/**
 * Created by hubert on 6/18/18.
 */

public class ContributionsViewModel extends AndroidViewModel {
    private final LiveData<List<Alist>> lists;
    private static final String TAG = ContributionsViewModel.class.getSimpleName();

    public ContributionsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mdb = AppDatabase.getDatabaseInstance(this.getApplication());
        Context context = application.getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int groupId = sharedPreferences.getInt(context.getString(R.string.group_id),DEFAULT_GROUP_ID);
        lists = mdb.aListDao().loadAllListNames(groupId);
    }

    public LiveData<List<Alist>> getLists() {
        return lists;
    }
}

