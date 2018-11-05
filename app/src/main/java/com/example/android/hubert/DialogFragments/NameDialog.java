package com.example.android.hubert.DialogFragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

public class NameDialog extends DialogFragment {

    private String name;
    private EditText editText;
    private NameDialogListener nameDialogListener;

    public interface NameDialogListener {
        void onOkSelected(String textEntered);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            nameDialogListener = (NameDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement NameDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(getString(R.string.dialog_title));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);   // Give our dialog a name

        LayoutInflater inflater = LayoutInflater.from(getContext());         // inflate and set the layout
        builder.setView(inflater.inflate(R.layout.edit_list_name,null));



        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String listName = editText.getText().toString();
                if (!listName.isEmpty()){
                    nameDialogListener.onOkSelected(listName);
                }

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Get the name
        name = getArguments().getString("name");

        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        editText = getDialog().findViewById(R.id.et_list_name);
        // If name is not null then set the name
        if( name != null){
            editText.setText(name);
        }
    }



}
