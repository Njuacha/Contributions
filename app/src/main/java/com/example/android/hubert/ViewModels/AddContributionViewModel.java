package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;

import java.util.List;

/**
 * Created by hubert on 8/21/18.
 */

public class AddContributionViewModel extends ViewModel {
    private final LiveData<List<Member>> allMembers;
    private List<Member> memberInList;

    public AddContributionViewModel(final AppDatabase mdb, final int listId){
        allMembers = mdb.member_dao().loadAllMembers();
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                memberInList = mdb.a_member_in_a_list_dao().loadMembersInList(listId);
            }
        });

    }

    public LiveData<List<Member>> getAllMembers(){
        return allMembers;
    }

    public List<Member> getMembersAlreadyInList(){
        return memberInList;
    }
}
