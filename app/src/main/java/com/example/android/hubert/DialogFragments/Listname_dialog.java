package com.example.android.hubert.DialogFragments;


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
import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.R;

import static com.example.android.hubert.Activities.Display_diff_list.LIST_ID_EXTRA;
import static com.example.android.hubert.Activities.Display_diff_list.LIST_NAME_EXTRA;

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


        return builder.create();
    }

    private void populateUI(){
        editText.setText(getArguments().getString(LIST_NAME_EXTRA));
    }

    @Override
    public void onResume() {
        super.onResume();
        editText = getDialog().findViewById(R.id.et_list_name);
        listId = getArguments().getInt(LIST_ID_EXTRA,DEFAULT_LIST_ID);
        if( listId != DEFAULT_LIST_ID){ // put former name of list if we are modifying an old list
            populateUI();
        }
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
