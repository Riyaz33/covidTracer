package com.cs446.covidtracer.ui.updates;

//import android.content.AsyncTaskLoader;
import androidx.loader.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class SummaryLoader extends AsyncTaskLoader<List<Summary>> {
    private String requestUrl;

    public SummaryLoader(Context context, String requestUrl) {
        super(context);
        this.requestUrl = requestUrl;
    }

    // On Main Thread
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // On Background Thread
    @Override
    public List<Summary> loadInBackground() {
        if (requestUrl == null) {
            return null;
        }
        List<Summary> listSummaries = QueryUtils.fetchSummaryData(requestUrl);
        return listSummaries;
    }
}
