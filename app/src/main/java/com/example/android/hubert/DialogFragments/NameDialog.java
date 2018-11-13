package com.example.android.hubert.DialogFragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.android.hubert.AppExecutors;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;

import java.util.Date;

import static com.example.android.hubert.Activities.MainActivity.CONTRIBUTIONS_TAB;
import static com.example.android.hubert.Activities.MainActivity.EXTRA_TAB;
import static com.example.android.hubert.Activities.MainActivity.MEMBERS_TAB;
import static com.example.android.hubert.PlaceholderFragment.EXTRA_EDIT_ITEM;


/**
 * Created by hubert on 6/15/18.
 */

public class NameDialog extends DialogFragment {

    private String formerName;
    private String tab;
    private EditText editText;
    private Parcelable editItem;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        tab = getArguments().getString(EXTRA_TAB);
        editItem = getArguments().getParcelable(EXTRA_EDIT_ITEM);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Give our dialog a name based on the current tab
        if (tab.equals(MEMBERS_TAB)){
            builder.setTitle(getString(R.string.name_of_member));
        }else if(tab.equals(CONTRIBUTIONS_TAB)){
            builder.setTitle(getString(R.string.nameOfList));
        }
        // Set the formerName to edittext if we are updating a name
        if(editItem != null) {
            if (tab.equals(MEMBERS_TAB)) formerName = ((Member) editItem).getName();
            else if (tab.equals(CONTRIBUTIONS_TAB)) formerName = ((Alist) editItem).getName();
        }


        LayoutInflater inflater = LayoutInflater.from(getContext());         // inflate and set the layout
        builder.setView(inflater.inflate(R.layout.edit_list_name,null));



        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textEntered = editText.getText().toString();
                saveNewName(textEntered);

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        return builder.create();
    }

    private void saveNewName(final String textEntered) {

        // If user didn't enter anything then simple return
        if(TextUtils.isEmpty(textEntered)) return;

        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getDatabaseInstance(getContext());

                if (tab.equals(MEMBERS_TAB)){
                    if (editItem == null){
                        db.member_dao().insertMember(new Member(textEntered));
                    }else{
                        Member member = (Member)editItem;
                        member.setName(textEntered);
                        db.member_dao().updateMember(member);
                    }
                }else if(tab.equals(CONTRIBUTIONS_TAB)){
                    if (editItem == null){
                        db.a_list_dao().insertAList(new Alist(textEntered,new Date()));
                    }else{
                        Alist alist = (Alist)editItem;
                        alist.setName(textEntered);
                        db.a_list_dao().updateAList(alist);

                    }
                }
            }
        });



    }


    @Override
    public void onResume() {
        super.onResume();

        editText = getDialog().findViewById(R.id.et_list_name);
        // If name is not null then set the name
        if( !TextUtils.isEmpty(formerName)){
            editText.setText(formerName);
        }
    }



}
