package com.cs446.covidtracer.ui.tracing;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.covidtracer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class TracingFragment extends Fragment {
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    Button stopScanningButton;
    TextView peripheralTextView;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private TracingViewModel tracingViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tracingViewModel =
                new ViewModelProvider(this).get(TracingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tracing, container, false);

        peripheralTextView = root.findViewById(R.id.text_tracing);
        peripheralTextView.setMovementMethod(new ScrollingMovementMethod());

        startScanningButton = (Button) root.findViewById(R.id.start_scan_button);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });

        stopScanningButton = (Button) root.findViewById(R.id.stop_scan_button);
        stopScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopScanning();
            }
        });
        stopScanningButton.setVisibility(View.INVISIBLE);

        btManager = (BluetoothManager)getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

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

        if (btAdapter != null) {
            btScanner = btAdapter.getBluetoothLeScanner();

            if (btAdapter != null && !btAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
            }

            // Make sure we have access coarse location enabled, if not, prompt the user to enable it
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect peripherals.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        } else {
            peripheralTextView.setText("Bluetooth not supported on this device.");
            startScanningButton.setVisibility(View.INVISIBLE);
        }

        return root;
    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            peripheralTextView.append("Bluetooth Address: " + result.getDevice().getAddress() + " rssi: " + result.getRssi() + "\n");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("coarse location permission granted");
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Functionality limited");
                builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }

                });
                builder.show();
            }
        }
    }

    public void startScanning() {
        System.out.println("start scanning");
        peripheralTextView.setText("");
        startScanningButton.setVisibility(View.INVISIBLE);
        stopScanningButton.setVisibility(View.VISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
        System.out.println("stopping scanning");
        peripheralTextView.append("Stopped Scanning");
        startScanningButton.setVisibility(View.VISIBLE);
        stopScanningButton.setVisibility(View.INVISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });
    }
}
