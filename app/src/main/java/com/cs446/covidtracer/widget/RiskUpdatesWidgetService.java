package com.cs446.covidtracer.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.cs446.covidtracer.R;
import com.cs446.covidtracer.ui.tracing.TracingItem;
import com.cs446.covidtracer.ui.tracing.data.TracingDbHelper;

import java.util.ArrayList;
import java.util.List;

public class RiskUpdatesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

/*This is where the adapter design pattern is used.
* This class converts TracingItems to a list of views compatible with WidgetProvider*/
class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private List<TracingItem> tracingItems = new ArrayList<>();
    TracingDbHelper db;

    public ListRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        db = new TracingDbHelper(mContext);
    }

    @Override
    public void onDataSetChanged() {
        tracingItems = db.getAllTracingItems();
    }

    @Override
    public void onDestroy() {
        tracingItems.clear();
    }

    @Override
    public int getCount() {
        return tracingItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.tracing_list_item_widget);
        rv.setTextViewText(R.id.riskFactor, tracingItems.get(position).getRisk());
        rv.setTextViewText(R.id.dateFactor, tracingItems.get(position).getDaysAgo() + " Ago");
        rv.setInt(R.id.tracingCardWidget, "setBackgroundColor", tracingItems.get(position).getBackgroundColor());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
