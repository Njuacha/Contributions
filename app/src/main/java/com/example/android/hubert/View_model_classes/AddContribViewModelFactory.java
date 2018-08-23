package com.example.android.hubert.View_model_classes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.hubert.DatabaseClasses.AppDatabase;

/**
 * Created by hubert on 8/21/18.
 */

public class AddContribViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mlistId;

    public AddContribViewModelFactory(AppDatabase mdb, int listId){
        mDb = mdb;
        mlistId = listId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddContributionViewModel(mDb,mlistId);
    }
}
