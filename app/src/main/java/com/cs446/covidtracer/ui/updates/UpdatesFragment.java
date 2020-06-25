package com.cs446.covidtracer.ui.updates;

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

public class UpdatesFragment extends Fragment {

    private UpdatesViewModel updatesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        updatesViewModel =
                new ViewModelProvider(this).get(UpdatesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_updates, container, false);
        final TextView textView = root.findViewById(R.id.text_updates);
        updatesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
