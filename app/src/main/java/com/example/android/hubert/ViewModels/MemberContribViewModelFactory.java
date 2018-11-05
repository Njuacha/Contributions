package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.hubert.DatabaseClasses.AppDatabase;

/**
 * Created by hubert on 9/6/18.
 */

public class MemberContribViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final AppDatabase mDb;
    private final int mMemberId;

    public MemberContribViewModelFactory(AppDatabase db, int memberId){
        mDb = db;
        mMemberId = memberId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MemberContribViewModel(mDb,mMemberId);
    }
}
