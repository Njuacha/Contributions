package com.example.android.hubert;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.android.hubert.Activities.Display_diff_list;
import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;

/**
 * Created by hubert on 6/15/18.
 */

public class Listname_dialog extends DialogFragment {

    private AppDatabase mdb;
    private int listId;
    private EditText editText;
    private final int DEFAULT_LIST_ID = Display_diff_list.DEFAULT_LIST_ID;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mdb = AppDatabase.getDatabaseInstance(getContext()); // instantiate database

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Name of list");   // Give our dailog a name

        LayoutInflater inflater = LayoutInflater.from(getContext());         // inflate and set the layout
        builder.setView(inflater.inflate(R.layout.edit_list_name,null));



        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveName();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        listId = getArguments().getInt("listId",DEFAULT_LIST_ID);

        if( listId != DEFAULT_LIST_ID){ // put former name of list if we are mordifying an old list
            populateUI();
        }
        return builder.create();
    }

    private void populateUI(){
       AppExecutors.getsInstance().diskIO().execute(new Runnable() {
           @Override
           public void run() {
               final String listName = mdb.a_list_dao().load_a_list(listId).getName();
               AppExecutors.getsInstance().mainThread().execute(new Runnable() {
                   @Override
                   public void run() {
                       Log.i("PopulateUI","We are actively retrieving list from database");
                       editText.setText(listName);
                   }
               });
           }
       });
    }

    @Override
    public void onResume() {
        super.onResume();
        editText = getDialog().findViewById(R.id.et_list_name);
    }

    private void saveName(){

        String listName = editText.getText().toString();
        final A_list a_list = new A_list(listName);

            AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (listId == DEFAULT_LIST_ID){
                        mdb.a_list_dao().insert_a_list(a_list);
                    }else {
                        a_list.setId(listId);
                        mdb.a_list_dao().update_a_list(a_list);
                    }
                }
            });
    }


}
