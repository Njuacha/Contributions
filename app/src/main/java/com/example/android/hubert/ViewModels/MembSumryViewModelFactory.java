package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.hubert.DatabaseClasses.AppDatabase;

public class MembSumryViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    AppDatabase db;
    int memberId;

    public MembSumryViewModelFactory(AppDatabase db, int memberId){
        this.db = db;
        this.memberId = memberId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MembSumryViewModel(db,memberId);
    }
}
