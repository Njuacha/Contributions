package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.AppDatabase;

public class MembSumryViewModel extends ViewModel {
    MutableLiveData<int[]> summaryInfo = new MutableLiveData<>() ;

    public MembSumryViewModel(final AppDatabase db, final int membId){


        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Get all the contributions of a member
                int[] amounts = db.a_member_in_a_list_dao().loadAllAmtOfMember(membId);
                // Iterate through the array of contributions in order to sum it all up
                int total = 0;
                int numberOfContrib = 0;

                if ( amounts != null){
                    // Sum up the amounts
                    for(int amount:amounts){
                        total += amount;
                    }
                    numberOfContrib = amounts.length;

                }

                summaryInfo.postValue(new int[]{numberOfContrib, total});


            }
        });


    }

    public LiveData<int[]> getSummaryInfo() {
        return summaryInfo;
    }
}
