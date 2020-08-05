package com.cs446.covidtracer.ui.tracing;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.covidtracer.R;
import com.cs446.covidtracer.ui.tracing.data.TracingDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class TracingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tracing, container, false);

        // start item list
        TracingDbHelper db = new TracingDbHelper(getActivity().getApplicationContext());
        ArrayList<TracingItem> tracingItems = db.getAllTracingItems();

//            tracingItems.add(new TracingItem("abcdef", -50, 1596174793, 1596175393)); // High
//            tracingItems.add(new TracingItem("abcdefg", -60, 1596575855, 1596576455)); // High
//            tracingItems.add(new TracingItem("abcdef", -70, 1596575855, 1596576455)); // Moderate
//            tracingItems.add(new TracingItem("abcdefg", -80, 1596575855, 1596576455)); // Moderate
//            tracingItems.add(new TracingItem("abcdef", -90, 1596575855, 1596576455)); // Low
//            tracingItems.add(new TracingItem("abcdefg", -100, 1596575855, 1596576455)); // Low
//            tracingItems.add(new TracingItem("abcdef", -50, 1596575855, 1596576155)); // High
//            tracingItems.add(new TracingItem("abcdefg", -60, 1596575855, 1596576155)); // Moderate
//            tracingItems.add(new TracingItem("abcdef", -70, 1596575855, 1596576155)); // Moderate
//            tracingItems.add(new TracingItem("abcdefg", -80, 1596575855, 1596576155)); // Low
//            tracingItems.add(new TracingItem("abcdef", -90, 1596575855, 1596576155)); // Low
//            tracingItems.add(new TracingItem("abcdefg", -100, 1596575855, 1596576155)); // Low
//            tracingItems.add(new TracingItem("abcdef", -50, 1596575855, 1596575915)); // High
//            tracingItems.add(new TracingItem("abcdefg", -60, 1596575855, 1596575915)); // Moderate
//            tracingItems.add(new TracingItem("abcdef", -70, 1596575855, 1596575915)); // low
//            tracingItems.add(new TracingItem("abcdefg", -80, 1596575855, 1596575915)); // low
//            tracingItems.add(new TracingItem("abcdef", -90, 1596575855, 1596575915)); // low
//            tracingItems.add(new TracingItem("abcdefg", -100, 1596575855, 1596575915)); // low
        mRecyclerView = root.findViewById(R.id.tracingRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new TracingAdapter(tracingItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        // end

        return root;
    }
}
