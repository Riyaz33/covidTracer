package com.cs446.covidtracer.ui.updates;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {}

    public static List<Summary> fetchSummaryData(String requestUrl) {
        // Create an ArrayList that will store summaries of provinces
        ArrayList<Summary> listSummaries = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            // Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(jsonString);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or summaries).
            JSONArray jsonSummaries = baseJsonResponse.getJSONArray("features");

            String name, abbreviation;
            int tests, recovered, activeCases, deaths, total;
            for (int i = 0; i < jsonSummaries.length(); i++) {
                JSONObject currentSummary = jsonSummaries.getJSONObject(i).getJSONObject("attributes");
                name = currentSummary.getString("NAME");
                abbreviation = currentSummary.getString("Abbreviation");
                abbreviation = "(" + abbreviation + ")";
                tests = currentSummary.getInt("Tests");
                recovered = currentSummary.getInt("Recovered");
                activeCases = currentSummary.getInt("ActiveCases");
                deaths = currentSummary.getInt("Deaths");
                total = currentSummary.getInt("Case_Total");

                Summary summary = new Summary(name, abbreviation, tests, recovered, activeCases, deaths, total);
                listSummaries.add(summary);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listSummaries;
    }
}
