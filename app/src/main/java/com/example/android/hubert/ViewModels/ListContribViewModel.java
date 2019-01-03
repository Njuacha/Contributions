package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.ListBasedContribution;

import java.util.List;

/**
 * Created by hubert on 6/19/18.
 */
public class ListContribViewModel extends ViewModel {
    private final LiveData<List<ListBasedContribution>> contributions;

    public ListContribViewModel(AppDatabase db, int listId) {
        contributions = db.aMemberInAListDao().loadContributionsInList(listId);
    }


    public LiveData<List<ListBasedContribution>> getContributionsInList() {
        return contributions;
    }

}

