package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.MemberBasedContribution;

import java.util.List;

/**
 * Created by hubert on 9/6/18.
 */

public class MemberContribViewModel extends ViewModel {
    private final LiveData<List<MemberBasedContribution>> contributions;

    public MemberContribViewModel(AppDatabase db, int memberId) {
        contributions = db.aMemberInAListDao().loadContributionOfMember(memberId);
    }


    public LiveData<List<MemberBasedContribution>> getContributionsInList() {
        return contributions;
    }

}
