package com.example.android.hubert.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;

import java.util.List;

/**
 * Created by hubert on 8/16/18.
 */

public class MembersViewModel extends AndroidViewModel {
    private final LiveData<List<Member>> members;

    public MembersViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mdb = AppDatabase.getDatabaseInstance(this.getApplication());
        members = mdb.member_dao().loadAllMembers(0);
    }

    public LiveData<List<Member>> getMembers() {
        return members;
    }
}
