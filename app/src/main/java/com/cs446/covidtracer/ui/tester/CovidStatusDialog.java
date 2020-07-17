package com.cs446.covidtracer.ui.tester;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cs446.covidtracer.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CovidStatusDialog extends DialogFragment {

    int position = 0;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        final String[] statusList = getActivity().getResources().getStringArray(R.array.covid_status_update_choices);
        builder.setTitle(R.string.covid_status_dialog_title)
                .setSingleChoiceItems(R.array.covid_status_update_choices, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString(getString(R.string.covid_status_shared_pref), statusList[position]);
                        editor.commit();
                        Intent intent = getActivity().getIntent();
                        intent.putExtra("key", true);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 101, intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
