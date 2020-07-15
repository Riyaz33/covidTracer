package com.cs446.covidtracer.ui.tester;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.cs446.covidtracer.R;

public class TesterFragment extends Fragment {

    private TesterViewModel testerViewModel;
    private Button selfTestStartButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        testerViewModel =
                new ViewModelProvider(this).get(TesterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tester, container, false);
        final TextView text_tester = root.findViewById(R.id.text_tester);

        testerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                text_tester.setText(s);
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

}
