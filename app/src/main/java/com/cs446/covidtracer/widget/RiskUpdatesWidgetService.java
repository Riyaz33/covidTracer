package com.cs446.covidtracer.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.cs446.covidtracer.R;
import com.cs446.covidtracer.ui.tracing.TracingItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class RiskUpdatesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private List<TracingItem> tracingItems = new ArrayList<>();

    public ListRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        try{
            tracingItems.add(new TracingItem("abcdef", -50, 1596174793, 1596175393, -60)); // High
            tracingItems.add(new TracingItem("abcdefg", -60, 1596174793, 1596175393, -60)); // High
            tracingItems.add(new TracingItem("abcdef", -70, 1596174793, 1596175393, -70)); // Moderate
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataSetChanged() {

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
        rv.setTextViewText(R.id.dateFactor, tracingItems.get(position).getDaysAgo());
        rv.setTextViewText(R.id.durationFactor, tracingItems.get(position).getDurationString());
        rv.setInt(R.id.tracingCardWidget, "setBackgroundColor", tracingItems.get(position).getBackgroundColor());

//        Bundle extras = new Bundle();
//        extras.putInt(RiskUpdatesWidgetProvider.EXTRA_ITEM, position);
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//        rv.setOnClickFillInIntent(R.id.tracingCardWidget, fillInIntent);

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
