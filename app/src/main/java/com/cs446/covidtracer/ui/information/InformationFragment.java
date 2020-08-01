package com.cs446.covidtracer.ui.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs446.covidtracer.R;
import com.cs446.covidtracer.ui.tester.TesterViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class InformationFragment extends Fragment {
    private InformationViewModel informationViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        informationViewModel =
                new ViewModelProvider(this).get(InformationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_information, container, false);

        return root;
    }
}
