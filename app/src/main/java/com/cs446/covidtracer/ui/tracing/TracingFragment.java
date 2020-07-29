package com.cs446.covidtracer.ui.tracing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.covidtracer.R;

import java.text.ParseException;
import java.util.ArrayList;

public class TracingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tracing, container, false);

        // start item list
        ArrayList<TracingItem> tracingItems = new ArrayList<>();
        try {
            tracingItems.add(new TracingItem("abcdef", -50, "20-07-2020 08:00:00 PM", "20-07-2020 08:10:00 PM")); // High
            tracingItems.add(new TracingItem("abcdefg", -60, "20-07-2020 08:00:00 PM", "20-07-2020 08:10:00 PM")); // High
            tracingItems.add(new TracingItem("abcdef", -70, "20-07-2020 08:00:00 PM", "20-07-2020 08:10:00 PM")); // Moderate
            tracingItems.add(new TracingItem("abcdefg", -80, "20-07-2020 08:00:00 PM", "20-07-2020 08:10:00 PM")); // Moderate
            tracingItems.add(new TracingItem("abcdef", -90, "20-07-2020 08:00:00 PM", "20-07-2020 08:10:00 PM")); // Low
            tracingItems.add(new TracingItem("abcdefg", -100, "20-07-2020 08:00:00 PM", "20-07-2020 08:10:00 PM")); // Low
            tracingItems.add(new TracingItem("abcdef", -50, "20-07-2020 08:00:00 PM", "20-07-2020 08:05:00 PM")); // High
            tracingItems.add(new TracingItem("abcdefg", -60, "20-07-2020 08:00:00 PM", "20-07-2020 08:05:00 PM")); // Moderate
            tracingItems.add(new TracingItem("abcdef", -70, "20-07-2020 08:00:00 PM", "20-07-2020 08:05:00 PM")); // Moderate
            tracingItems.add(new TracingItem("abcdefg", -80, "20-07-2020 08:00:00 PM", "20-07-2020 08:05:00 PM")); // Low
            tracingItems.add(new TracingItem("abcdef", -90, "20-07-2020 08:00:00 PM", "20-07-2020 08:05:00 PM")); // Low
            tracingItems.add(new TracingItem("abcdefg", -100, "20-07-2020 08:00:00 PM", "20-07-2020 08:05:00 PM")); // Low
            tracingItems.add(new TracingItem("abcdef", -50, "20-07-2020 08:00:00 PM", "20-07-2020 08:01:00 PM")); // High
            tracingItems.add(new TracingItem("abcdefg", -60, "20-07-2020 08:00:00 PM", "20-07-2020 08:01:00 PM")); // Moderate
            tracingItems.add(new TracingItem("abcdef", -70, "20-07-2020 08:00:00 PM", "20-07-2020 08:01:00 PM")); // low
            tracingItems.add(new TracingItem("abcdefg", -80, "20-07-2020 08:00:00 PM", "20-07-2020 08:01:00 PM")); // low
            tracingItems.add(new TracingItem("abcdef", -90, "20-07-2020 08:00:00 PM", "20-07-2020 08:01:00 PM")); // low
            tracingItems.add(new TracingItem("abcdefg", -100, "20-07-2020 08:00:00 PM", "20-07-2020 08:01:00 PM")); // low
            mRecyclerView = root.findViewById(R.id.tracingRecyclerView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mAdapter = new TracingAdapter(tracingItems);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // end

        return root;
    }
}
