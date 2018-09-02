package com.example.android.hubert.DialogFragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.R;

import java.util.Date;

import static com.example.android.hubert.Activities.MainActivity.LIST_EXTRA;


/**
 * Created by hubert on 6/15/18.
 */

public class Listname_dialog extends DialogFragment {

    private AppDatabase mdb;
    private Alist formerList;
    private EditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mdb = AppDatabase.getDatabaseInstance(getContext()); // instantiate database

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.create_list_dialog);   // Give our dailog a name

        LayoutInflater inflater = LayoutInflater.from(getContext());         // inflate and set the layout
        builder.setView(inflater.inflate(R.layout.edit_list_name,null));



        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveName();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        return builder.create();
    }

    private void populateUI(){
        editText.setText(formerList.getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        editText = getDialog().findViewById(R.id.et_list_name);

        formerList = getArguments().getParcelable(LIST_EXTRA);
        if( formerList != null){ // put former name of list if we are modifying an old list
            populateUI();
        }
    }

    private void saveName(){

        final String listName = editText.getText().toString();
        if(TextUtils.isEmpty(listName)) return;

            AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (formerList == null){
                        mdb.a_list_dao().insert_a_list(new Alist(listName,new Date()));
                    }else {
                        formerList.setName(listName);
                        mdb.a_list_dao().update_a_list(formerList);
                    }
                }
            });
    }


}
