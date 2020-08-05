package com.cs446.covidtracer.ui.tester;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cs446.covidtracer.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.AlertDialog.*;

public class CovidStatusDialog extends DialogFragment {

    int position = 0;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private String macAddr;
    public static final String bluetoothID = "BluetoothID";
    public static final String regPref = "RegistrationPref";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity());
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(regPref,Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        macAddr = sharedPreferences.getString(bluetoothID,"");
        db = FirebaseFirestore.getInstance();
        String[] statusArray = getResources().getStringArray(R.array.covid_status_update_choices);
        String defaultValue = statusArray[0];
        String statusValue = sharedPref.getString(getString(R.string.covid_status_shared_pref), defaultValue);
        int checked = 0;
        if(statusValue.equals(statusArray[1])){
            checked = 1;
        }

        final String[] statusList = getActivity().getResources().getStringArray(R.array.covid_status_update_choices);
        builder.setTitle(R.string.covid_status_dialog_title)
                .setSingleChoiceItems(R.array.covid_status_update_choices, checked, new DialogInterface.OnClickListener() {
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
                        Map<String,Object> data = new HashMap<>();
                        long epochTime = System.currentTimeMillis();
                        String time = ""+epochTime;
                        data.put("userStatus",statusList[position]);
                        data.put("timestamp",time);
                        DocumentReference doc = db.collection("Users").document( macAddr);
                        doc.update(data);
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
