package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.hubert.DatabaseClasses.AppDatabase;

/**
 * Created by hubert on 8/21/18.
 */

public class AddContribViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context mContext;
    private final int mlistId;


    public AddContribViewModelFactory(Context context, int listId){
        mContext = context;
        mlistId = listId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddContributionViewModel(mContext,mlistId);
    }
}
