package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

import java.util.List;

import static com.example.android.hubert.Activities.MainActivity.DEFAULT_GROUP_ID;

/**
 * Created by hubert on 8/21/18.
 */

public class AddContributionViewModel extends ViewModel {
    private final LiveData<List<Member>> allMembers;
    private List<Member> memberInList;

    public AddContributionViewModel(final Context context, final int listId){
        final AppDatabase mdb = AppDatabase.getDatabaseInstance(context);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int groupId = sharedPreferences.getInt(context.getString(R.string.group_id),DEFAULT_GROUP_ID);
        allMembers = mdb.memberDao().loadAllMembers(groupId);
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                memberInList = mdb.aMemberInAListDao().loadMembersInList(listId);
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
