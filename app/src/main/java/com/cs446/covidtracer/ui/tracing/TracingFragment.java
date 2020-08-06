package com.cs446.covidtracer.ui.tracing;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager.LoaderCallbacks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.covidtracer.R;
import com.cs446.covidtracer.ui.tracing.data.TracingDbHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TracingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView emptyStateTextView;
    private TextView dummyDataTextView;
    private boolean haveDummyData;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tracing, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        emptyStateTextView = getView().findViewById(R.id.empty_risk);
        dummyDataTextView = getView().findViewById(R.id.dummy_data_note);
        haveDummyData = true;
        updateUi();

    }

    private void updateUi() {
        final TracingDbHelper db = new TracingDbHelper(getActivity().getApplicationContext());
        ArrayList<TracingItem> tracingItems = db.getAllTracingItems();
        tracingItems = db.getAllTracingItems();

        // To remove dummy data if real data appears
        if(haveDummyData && tracingItems.size() > 3) {
            db.removeFakeData();
            dummyDataTextView.setText("");
            haveDummyData = false;
        }

        // Handling no cards
        if(tracingItems.size() == 0){
            emptyStateTextView.setText(R.string.no_risk_found);
        }
        mRecyclerView = getView().findViewById(R.id.tracingRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new TracingAdapter(tracingItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}
