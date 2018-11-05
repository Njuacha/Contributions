package com.example.android.hubert.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by hubert on 7/14/18.
 */

public class ContribSumryViewModel extends ViewModel {
    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";
    // Date formatter
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    private MutableLiveData<String[]> summaryInfo = new MutableLiveData<>();

    public ContribSumryViewModel(AppDatabase database, final Alist alist) {
        final AppDatabase mDb;
        mDb = database;


        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Get date from list
                Date date = alist.getDate();

                // Query database with listId to get amounts contributed
                int[] amounts = mDb.a_member_in_a_list_dao().loadAllAmountInlist(alist.getListId());
                // Iterate through the amounts to sum them all into totalAmt variable
                int totalAmt = 0;
                for (int amount : amounts) {
                    totalAmt += amount;
                }


                summaryInfo.postValue(new String[]{
                        dateFormat.format(date)
                        , String.valueOf(amounts.length)
                        , String.valueOf(totalAmt)
                });
            }
        });

    }

    public LiveData<String[]> getSummaryInfo() {
        return summaryInfo;
    }
}
