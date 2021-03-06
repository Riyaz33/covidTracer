package com.cs446.covidtracer.ui.tester;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.cs446.covidtracer.R;

public class TesterFragment extends Fragment {

    private Button selfTestStartButton;
    private Button updateStatusButton;
    private String[] statusArray;
    private TextView covid_status_description;
    SharedPreferences sharedPref;
    public static final int DIALOG_FRAGMENT = 1;
    public static final int RESULT_OK = 101;
    Fragment thisFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tester, container, false);
        covid_status_description = root.findViewById(R.id.covid_status_description);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        statusArray = getResources().getStringArray(R.array.covid_status_update_choices);
        String defaultValue = statusArray[0];
        String statusValue = sharedPref.getString(getString(R.string.covid_status_shared_pref), defaultValue);
        covid_status_description.setText(getString(R.string.covid_status_description, statusValue));

        thisFragment = this;
        updateStatusButton = root.findViewById(R.id.update_status);
        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment covidStatusDialog = new CovidStatusDialog();
                covidStatusDialog.setTargetFragment(thisFragment, 1);
                covidStatusDialog.show(getParentFragmentManager(), "Covid Update Dialog");
            }

        });

        selfTestStartButton = (Button) root.findViewById(R.id.selfTestStartButton);
        selfTestStartButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openSelfCheckActivity();
            }
        });
        
        return root;
    }

    public void openSelfCheckActivity(){
        Intent intent = new Intent(getActivity(), SelfCheckActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == RESULT_OK) {
                    boolean check = data.getBooleanExtra("key", true);
                    if(check)
                    {
                        statusArray = getResources().getStringArray(R.array.covid_status_update_choices);
                        String defaultValue = statusArray[1];
                        String statusValue = sharedPref.getString(getString(R.string.covid_status_shared_pref), defaultValue);
                        covid_status_description.setText(getString(R.string.covid_status_description, statusValue));
                    }
                }
                break;
        }
    }
}
