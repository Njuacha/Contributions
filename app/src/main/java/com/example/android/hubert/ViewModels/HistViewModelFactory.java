package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.hubert.DatabaseClasses.AppDatabase;

/**
 * Created by hubert on 8/22/18.
 */

public class HistViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mListId;
    private final int mMemberId;

    public HistViewModelFactory(AppDatabase db, int listId, int memberId){
        mDb = db;
        mListId = listId;
        mMemberId = memberId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HistViewModel(mDb,mListId,mMemberId);
    }
}
