package com.example.android.hubert.View_model_classes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Contribution;

import java.util.List;

/**
 * Created by hubert on 6/19/18.
 */
public class InnerContributionsViewModel extends ViewModel {
    private final LiveData<List<Contribution>> contributions;

    public InnerContributionsViewModel(AppDatabase db, int listId) {
        contributions = db.a_member_in_a_list_dao().loadContributionsInList(listId);
    }


    public LiveData<List<Contribution>> getContributionsInList() {
        return contributions;
    }

}

