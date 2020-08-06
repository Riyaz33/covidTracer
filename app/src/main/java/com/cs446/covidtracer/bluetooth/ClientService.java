package com.cs446.covidtracer.bluetooth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.cs446.covidtracer.MainActivity;
import com.cs446.covidtracer.R;
import com.cs446.covidtracer.bluetooth.data.BluetoothDbHelper;
import com.cs446.covidtracer.bluetooth.data.DeviceContract;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ClientService extends Service {
    private static final String TAG = "ClientService";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private SparseArray<BluetoothDevice> mDevices;
    private SparseArray<DeviceDetected> mDevicesDetected;

    private BluetoothGatt mConnectedGatt;

    private Handler mHandler = new Handler();

    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "covid tracer";
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(channelId);
        notificationChannel.setSound(null, null);

        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Covid Tracer")
                .setContentText("Scanning nearby devices...")
                .setContentIntent(pendingIntent).build();

        startForeground(1000, notification);

        /*
         * Bluetooth in Android 4.3+ is accessed via the BluetoothManager, rather than
         * the old static BluetoothAdapter.getInstance()
         */
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mDevices = new SparseArray<BluetoothDevice>();
        mDevicesDetected = new SparseArray<DeviceDetected>();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        /*
         * We need to enforce that Bluetooth is first enabled, and take the
         * user to settings to enable it if they have not done so.
         */
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            //Bluetooth is disabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableBtIntent);
            return;
        }

        /*
         * Check for Bluetooth LE Support.  In production, our manifest entry will keep this
         * from installing on these devices, but this will allow test devices or other
         * sideloads to report whether or not the feature exists.
         */
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE Support.", Toast.LENGTH_SHORT).show();
            return;
        }

        discoverBLEDevices();
    }

    private void discoverBLEDevices() {
        startTimedScan.run();
    }

    private Runnable startTimedScan = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(stopTimedScan, 10000);
            startScan();
        }
    };

    private Runnable stopTimedScan = new Runnable() {
        @Override
        public void run() {
            stopScan();
            for(int i = 0; i < mDevicesDetected.size(); i++){
                int hashCode = mDevicesDetected.keyAt(i);
                DeviceDetected deviceDetected = mDevicesDetected.valueAt(i);
                if (mDevices.get(hashCode) == null) {
                    Log.i(TAG, "Saving to database: " + deviceDetected.device.getAddress() + " " + deviceDetected.firstDetection + " " + deviceDetected.rssis);
                    saveToDatabase(deviceDetected);
                    mDevicesDetected.remove(hashCode);
                }
            }
            mDevices.clear();
            mHandler.postDelayed(startTimedScan, 10);
        }
    };

    private void saveToDatabase(DeviceDetected deviceDetected) {
        BluetoothDbHelper dbHelper = new BluetoothDbHelper(ClientService.this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues entry = new ContentValues();
        double rssiSum = 0;
        for(int rssi : deviceDetected.rssis) {
            rssiSum += rssi;
        }
        // entry.put(DeviceContract.DeviceEntry.DEVICE_ADDRESS, "42:FC:A4:80:82:8D"); // testing purpose. Do not remove.
        entry.put(DeviceContract.DeviceEntry.DEVICE_ADDRESS, deviceDetected.device.getAddress()); // real use. Do not remove.
        entry.put(DeviceContract.DeviceEntry.START_TIME, deviceDetected.firstDetection.getEpochSecond());
        entry.put(DeviceContract.DeviceEntry.END_TIME, Instant.now().getEpochSecond());
        entry.put(DeviceContract.DeviceEntry.AVERAGE_RSSI, rssiSum / deviceDetected.rssis.size());
        db.insert(DeviceContract.DeviceEntry.TABLE_NAME, null, entry);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Stop any active scans
        stopScan();
        //Disconnect from any active connection
        if (mConnectedGatt != null) {
            mConnectedGatt.disconnect();
            mConnectedGatt = null;
        }
    }

    /*
     * Begin a scan for new servers that advertise our
     * matching service.
     */
    private void startScan() {
        //Scan for devices advertising our custom service
        ScanFilter scanFilter = new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(DeviceProfile.SERVICE_UUID))
                .build();
        ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(scanFilter);

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .build();
        mBluetoothAdapter.getBluetoothLeScanner().startScan(filters, settings, mScanCallback);
    }

    /*
     * Terminate any active scans
     */
    private void stopScan() {
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
    }

    /*
     * Callback handles results from new devices that appear
     * during a scan. Batch results appear when scan delay
     * filters are enabled.
     */
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d(TAG, "onScanResult");

            processResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.d(TAG, "onBatchScanResults: "+results.size()+" results");

            for (ScanResult result : results) {
                processResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.w(TAG, "LE Scan Failed: "+errorCode);
        }

        private void processResult(ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.i(TAG, Instant.now() + " New LE Device: " + device.getAddress() + " @ " + result.getRssi());
            //Add it to the collection
            mDevices.put(device.hashCode(), device);

            DeviceDetected deviceDetected = mDevicesDetected.get(device.hashCode());
            if (deviceDetected == null) {
                mDevicesDetected.put(device.hashCode(), new DeviceDetected(Instant.now(), device, result.getRssi()));
            } else {
                deviceDetected.rssis.add(result.getRssi());
            }
        }
    };
}

class DeviceDetected {
    public Instant firstDetection;
    public BluetoothDevice device;
    public ArrayList<Integer> rssis = new ArrayList<>();

    DeviceDetected(Instant firstDetection, BluetoothDevice device, int rssi) {
        this.firstDetection = firstDetection;
        this.device = device;
        this.rssis.add(rssi);
    }
}