package com.example.informatik.geoloacation;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Informatik on 02.03.2017.
 */

public class FireMissilesDialogFragment extends DialogFragment {
    public static final boolean DEBUG = true;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_fire_missiles)
                .setTitle(R.string.missile_title)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(DEBUG) {
                            Log.d("Dialog", "FIRE");
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(DEBUG){
                            Log.d("Dialog", "Cancel");
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}