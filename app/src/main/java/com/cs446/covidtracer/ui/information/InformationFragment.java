package com.cs446.covidtracer.ui.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs446.covidtracer.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class InformationFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_information, container, false);
        return root;
    }
}
