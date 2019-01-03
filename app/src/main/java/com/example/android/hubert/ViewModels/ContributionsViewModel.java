package com.example.android.hubert.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;

import java.util.List;

/**
 * Created by hubert on 6/18/18.
 */

public class ContributionsViewModel extends AndroidViewModel {
    private final LiveData<List<Alist>> lists;
    private static final String TAG = ContributionsViewModel.class.getSimpleName();

    public ContributionsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mdb = AppDatabase.getDatabaseInstance(this.getApplication());
        Log.d(TAG, "Actively retreiving all listNames from database");
        lists = mdb.aListDao().loadAllListNames(0);
    }

    public LiveData<List<Alist>> getLists() {
        return lists;
    }
}

