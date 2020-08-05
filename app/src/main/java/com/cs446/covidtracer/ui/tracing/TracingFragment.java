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
    private ArrayList<TracingItem> tracingItems;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tracing, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUi();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        updateUi();
//    }

    private void updateUi() {
        final TracingDbHelper db = new TracingDbHelper(getActivity().getApplicationContext());
        ArrayList<TracingItem> tracingItems = db.getAllTracingItems();
        tracingItems = db.getAllTracingItems();

        mRecyclerView = getView().findViewById(R.id.tracingRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new TracingAdapter(tracingItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}
