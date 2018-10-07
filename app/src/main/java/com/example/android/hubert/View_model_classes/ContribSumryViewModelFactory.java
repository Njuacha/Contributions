package com.example.android.hubert.View_model_classes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;

/**
 * Created by hubert on 7/14/18.
 */

public class ContribSumryViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final Alist mAlist;

    public ContribSumryViewModelFactory(AppDatabase db, Alist alist){
        mDb = db;
        mAlist = alist;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ContribSumryViewModel(mDb,mAlist);
    }
}
