package com.example.android.hubert.View_model_classes;

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

public class SummaryViewModel extends ViewModel {
    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";
    // Date formatter
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    private String[] summaryInfo = new String[3];

    public SummaryViewModel(AppDatabase database, final Alist alist){
        final AppDatabase mDb;
        mDb = database;


        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Get date from list
                Date date = alist.getDate();
                summaryInfo[0] = dateFormat.format(date);

                // Query database with listId to get amounts contributed
                int[] amounts = mDb.a_member_in_a_list_dao().loadAllAmountInlist(alist.getId());
                // Iterate through the amounts to sum them all into totalAmt variable
                int totalAmt = 0;
                for( int amount:amounts){
                    totalAmt+= amount;
                }
                summaryInfo[1] =  String.valueOf(amounts.length);
                summaryInfo[2] =  String.valueOf(totalAmt);
            }
        });

    }

    public String[] getSummaryInfo() {
        return summaryInfo;
    }
}
