package com.cs446.covidtracer.ui.tracing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cs446.covidtracer.R;

public class TracingFragment extends Fragment {

    private TracingViewModel tracingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tracingViewModel =
                new ViewModelProvider(this).get(TracingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tracing, container, false);
//        final TextView textView = root.findViewById(R.id.text_tracing);
//        tracingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}
