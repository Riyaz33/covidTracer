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
import android.util.Pair;
import android.widget.Toast;

import com.cs446.covidtracer.bluetooth.ClientService;
import com.cs446.covidtracer.bluetooth.PeripheralService;
import com.cs446.covidtracer.ui.tracing.data.PositiveDbHelper;
import com.cs446.covidtracer.ui.tracing.data.TracingDbHelper;
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

    // Migrate to strings.xml and update values <- TODO
    private String DEFAULT_COVID_STATUS = "Negative";
    private String ID = "userID";
    private String DEFAULT = "Users";
    private String STATUS = "userStatus";
    private String TIMESTAMP = "timestamp";
    private String NEW_USER = "negative";
    public static final String bluetoothID = "BluetoothID";
    public static final String regPref = "RegistrationPref";
    public static final String regStat = "RegistrationStatus";
    public static final String DISPLAY_ADDRESS = "Your bluetooth address is: ";
    public static final String CONSENT = " \nThis address will be encrypted and stored " +
            "on our servers and will be associated to your Covid-19 Test Results. " +
            "Click Continue to provide your consent and proceed. ";
    private static final String TAG = "Firebase Firestore:";

    Boolean changed = false;
    private FirebaseFirestore db;
    private DatabaseReference doc;
    SharedPreferences sharedPreferences;

    // Set up Encryption <- TODO
    public static final String secretKey = "";

    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_updates, R.id.navigation_tracing,
                R.id.navigation_tester, R.id.navigation_information)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController,
                appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            // We need to enforce that location permissions are enabled.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        // Get Firestore Started
        db = FirebaseFirestore.getInstance();

        // generate dummy mac address to work around bluetooth dev restrictions
        final String macAddr = getRandomMacAddress();

        // Check Registration Status using shared preferences, create new if necessary <- Begin
        sharedPreferences = getSharedPreferences(regPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (NEW_USER == sharedPreferences.getString(regStat, NEW_USER)) {
            // new user -> Show Registration Alert Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Welcome to Covid-Tracer");
            builder.setMessage(DISPLAY_ADDRESS + macAddr + CONSENT);
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
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
        // successful in adding new user, save to shared preferences
        if (changed) {
            editor.putString(bluetoothID, macAddr);
            editor.putString(regStat, "positive");
            editor.commit();
        }

        // Check Registration Status using shared preferences, create new if necessary <- End

        // Add Listener to get new Positive users in real time <- Begin
        db.collection(DEFAULT).whereEqualTo(STATUS, "Positive")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        // SQLite Helper Class for storing new Positive users locally
                        final PositiveDbHelper positiveDb =
                                new PositiveDbHelper(getApplicationContext());
                        final TracingDbHelper tracingDb = new TracingDbHelper(getApplicationContext());
                        ArrayList<Pair<String, Long>> list = new ArrayList<Pair<String, Long>>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get(ID) != null && doc.get(ID) != macAddr) {
                                String id = doc.getString(ID);
                                String timestamp = doc.getString(TIMESTAMP);
                                Long time = Long.parseLong("0");
                                if (timestamp != null) {
                                    time = Long.parseLong(timestamp);
                                }
                                Pair<String, Long> User = new Pair<String, Long>(id, time);
                                list.add(User);
                            }
                        }
                        // Add to local db
                        positiveDb.addDevice(list);
                        tracingDb.checkAndAddTracingItems(list, getApplicationContext());
                        Log.d(TAG, list.size() + " new users with Positive Status");
                    }
                });

        // Add Listener to get new Positive users in real time <- End


    }

    // Method to send registration to Firebase database
    private void sendRegistrationToServer(String macAddress) {
        Map<String, Object> data = new HashMap<>();
        data.put(STATUS, DEFAULT_COVID_STATUS);
        data.put(ID, macAddress);
        db.collection(DEFAULT).document(macAddress).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,
                        " Registration Successful! ", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                " Registration Failed! ", Toast.LENGTH_LONG).show();
                        Log.d("ID-REGISTRATION-FAILURE", e.getMessage());
                    }
                });
    }

    // Function to generate Random MAC Address
    public static String getRandomMacAddress() {
        String mac = "";
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            int n = r.nextInt(255);
            mac += String.format("%02x:", n);
        }
        mac = mac.substring(0, mac.length() - 1);
        // return "42:FC:A4:80:82:8D"; // testing purpose. Do not remove.
        return mac.toUpperCase(); //real use. Do not remove.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
    permissions, @NonNull int[] grantResults) {
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

