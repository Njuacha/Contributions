package com.example.android.hubert.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.android.hubert.R;

/**
 * Created by hubert on 7/13/18.
 */

public class Add_amount_dialog extends DialogFragment {

    private Add_amount_dialog_listener mListener;

    public interface Add_amount_dialog_listener{
        void onPositiveButtonClicked(int amount);
    }
    // Instantiates the listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try{
            mListener = (Add_amount_dialog_listener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(getActivity().toString()+" must implement Add_amount_dialog_listerner");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the title of dialog to "Enter Amount"
        builder.setTitle("Enter Amount");
        // Prepare a layout inflater to inflate the layout needed
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // Use layout inflater above to inflate the layout view for editing amount
        builder.setView(inflater.inflate(R.layout.edit_amount,null));
        // Set action for positive button
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etAmount = (EditText) getDialog().findViewById(R.id.et_amount);
                // Call the call back method while passing in the amount entered
                try{
                    int amount = Integer.parseInt(etAmount.getText().toString());
                    mListener.onPositiveButtonClicked(amount);
                }catch(IllegalArgumentException e){
                    // I am not interested in handling this exception for now. Because most users would not put in wrong input
                }
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
