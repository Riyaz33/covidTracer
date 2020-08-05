package com.cs446.covidtracer;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cs446.covidtracer.bluetooth.ClientService;
import com.cs446.covidtracer.bluetooth.PeripheralService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Firebase";
    private FirebaseFirestore db;
    private DatabaseReference doc;
    SharedPreferences sharedPreferences;
    public static final String bluetoothID = "BluetoothID";
    public static final String regPref = "RegistrationPref";
    public static final String regStat = "RegistrationStatus";
    public static final String secretKey = "[B@387c703b";
    private Query mQuery;
    private int DEFAULT_COVID_STATUS = 0;
    private String ID = "userID";
    private String STATUS = "userStatus";
    private boolean isRegistered = false;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_updates, R.id.navigation_tracing, R.id.navigation_tester, R.id.navigation_information)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // We need to enforce that location permissions are enabled.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(regPref,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Boolean changed = false;
        final String macAddr = getRandomMacAddress();
        if ("negative" == sharedPreferences.getString(regStat, "negative")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Registration");
            builder.setMessage("Your bluetooth address is:"+macAddr+" \n Click OK to proceed with registration");

            // add a button
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendRegistrationToServer(macAddr);
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            changed = true;
        }
        if (changed)
        {
            editor.putString(bluetoothID, macAddr);
            editor.putString(regStat, "positive");
            editor.commit();
        }
        // Get the local Bluetooth adapter

        db.collection("Users")
                .whereEqualTo("userStatus", "Positive")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        Map<String,Object> User = new HashMap<>();
                        List<Object> Users = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("userID") != null) {
                               String id = doc.getString("userID");
                               String timestamp = doc.getString("timestamp");
                               User.put(id,timestamp);
                            }
                        }
                        Log.d(TAG, "Current Users that have tested positive: " + User);
                    }
                });




    }

    private void sendRegistrationToServer(String macAddress) {

        Map<String, Object> data = new HashMap<>();
        data.put(STATUS, DEFAULT_COVID_STATUS);
        data.put(ID, macAddress);
        db.collection("Users").document(macAddress).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed",Toast.LENGTH_LONG).show();
                        Log.d("ID-REGISTRATION-FAILURE",e.getMessage());
                    }
                });
    }

    public static String getRandomMacAddress() {
        String mac = "";
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            int n = r.nextInt(255);
            mac += String.format("%02x:", n);

        }
        mac = mac.substring(0, mac.length() - 1);
        return mac.toUpperCase();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent bluetoothClient = new Intent(this, ClientService.class);
                startForegroundService(bluetoothClient);

                Intent bluetoothPeripheral = new Intent(this, PeripheralService.class);
                startForegroundService(bluetoothPeripheral);
            } else {
                Toast.makeText(MainActivity.this, "Contact tracing disabled. Location permissions must be granted to start contact tracing.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

