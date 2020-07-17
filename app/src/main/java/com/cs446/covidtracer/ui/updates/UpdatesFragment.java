package com.cs446.covidtracer.ui.updates;

import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;

import android.content.ContentValues;
import android.content.Context;

import androidx.loader.content.Loader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cs446.covidtracer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cs446.covidtracer.ui.updates.data.SummaryDbHelper;
import com.cs446.covidtracer.ui.updates.data.SummaryContract.SummaryEntry;

public class UpdatesFragment extends Fragment implements LoaderCallbacks {

    private UpdatesViewModel updatesViewModel;

    private static final String LOG_TAG = UpdatesFragment.class.getSimpleName();

    private static final String COVID_REQUEST_URL = "https://services9.arcgis.com/pJENMVYPQqZZe20v/arcgis/rest/services/" +
            "Join_Features_to_Enriched_Population_Case_Data_By_Province_Polygon/FeatureServer/0/" +
            "query?where=1%3D1&outFields=NAME,Abbreviation,Recovered,Tests,ActiveCases,Deaths,Case_Total" +
            "&returnGeometry=false&outSR=4326&f=json";

    /**
     * Constant value for the summary loader ID. We can choose any integer.
     * This really only comes into play if multiple loaders are used.
     */
    private static final int SUMMARY_LOADER_ID = 1;

    /**
     * List of summaries for ALL the provinces
     */
    private List<Summary> listSummaries;

    /**
     * Adapter for the list of summaries
     */
    private SummaryAdapter adapter;

    /**
     * List of provinces that user has preference for
     */
    private ArrayList<String> userProvinces;

    /**
     * Array of pre-defined province names for autocomplete suggestions
     */
    private static final String[] PROVINCES = new String[]{
            "Newfoundland and Labrador", "Prince Edward Island", "Nova Scotia", "New Brunswick",
            "Quebec", "Ontario", "Manitoba", "Saskatchewan", "Alberta", "British Columbia", "Yukon",
            "Northwest Territories", "Nunavut", "Repatriated CDN"
    };


    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyStateTextView;

    /**
     * Database helper that will provide us access to the database
     */
    private SummaryDbHelper dbHelper;

    /**
     * AutoCompleteTextView that will capture user's preference of provinces
     */
    private AutoCompleteTextView provinceView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_updates, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity
        dbHelper = new SummaryDbHelper(getActivity());

        provinceView = getView().findViewById(R.id.list_provinces);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, PROVINCES);
        provinceView.setAdapter(provinceAdapter);

        userProvinces = getListUserProvinces();

        Button addProvinceButton = getView().findViewById(R.id.button_add_province);
        addProvinceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProvinces = getListUserProvinces();
                String provinceName = provinceView.getText().toString().trim();
                if (!Arrays.asList(PROVINCES).contains(provinceName)) {
                    Toast.makeText(getActivity(), "Province name is invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userProvinces.contains(provinceName)) {
                    Toast.makeText(getActivity(),
                            String.format("Province %s has already been added before", provinceName), Toast.LENGTH_SHORT).show();
                    return;
                }

                userProvinces.add(provinceName);

                // Gets the database in write mode
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create a ContentValues object where column names are the keys,
                // and province name from AutoCompleteTextView is the values.
                ContentValues values = new ContentValues();
                values.put(SummaryEntry.COLUMN_PROVINCE_NAME, provinceName);

                // Insert a new row for province in the database, returning the ID of that new row.
                long newRowId = db.insert(SummaryEntry.TABLE_NAME, null, values);

                // Show a toast message depending on whether or not the insertion was successful
                if (newRowId == -1) {
                    // If the row ID is -1, then there was an error with insertion.
                    Toast.makeText(getActivity(), "Error with saving province", Toast.LENGTH_SHORT).show();
                } else {
                    // Clear the text if the insertion was successful
                    provinceView.setText("");
                    // Display a toast message showing the province was successfully saved
                    Toast.makeText(getActivity(), "Saved Province " + provinceName, Toast.LENGTH_SHORT).show();
                    // Update the screen to include the newly added Province
                    updateUi();
                }
            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        emptyStateTextView = getView().findViewById(R.id.empty_view);

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getActivity().getSupportLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(SUMMARY_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = getView().findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    private ArrayList<String> getListUserProvinces() {
        ArrayList<String> userProvinces = new ArrayList<>();

        // Gets the database in write mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {SummaryEntry.COLUMN_PROVINCE_NAME,};

        // Perform a query on the pets table
        Cursor cursor = db.query(
                SummaryEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        try {
            int nameColumnIndex = cursor.getColumnIndex(SummaryEntry.COLUMN_PROVINCE_NAME);
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentName = cursor.getString(nameColumnIndex);
                userProvinces.add(currentName);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        return userProvinces;
    }

    private void updateUi() {
        ArrayList<Summary> userSummaries = new ArrayList<>();
        for (Summary summary : listSummaries) {
            if (userProvinces.contains(summary.getName())) {
                userSummaries.add(summary);
            }
        }
        ListView summaryListView = getView().findViewById(R.id.list);
        summaryListView.setEmptyView(emptyStateTextView);
        adapter = new SummaryAdapter(getActivity(), userSummaries);
        summaryListView.setAdapter(adapter);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new SummaryLoader(getActivity(), COVID_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        listSummaries = (List<Summary>) data;
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = getView().findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        emptyStateTextView.setText(R.string.no_summaries);

        // If there is a valid list of Summaries, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (listSummaries != null && !listSummaries.isEmpty()) {
            updateUi();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}
