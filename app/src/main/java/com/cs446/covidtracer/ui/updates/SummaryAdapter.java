package com.cs446.covidtracer.ui.updates;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs446.covidtracer.R;
import com.cs446.covidtracer.ui.updates.data.SummaryContract.SummaryEntry;
import com.cs446.covidtracer.ui.updates.data.SummaryDbHelper;

import java.util.List;

public class SummaryAdapter extends ArrayAdapter<Summary> {
    private Context callerContext;
    public SummaryAdapter(Context context, List<Summary> listSummaries) {
        super(context, 0, listSummaries);
        this.callerContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.summary_list_item, parent, false);
        }

        // Find the summary at the given position in the list of summaries
        final Summary currentSummary = getItem(position);

        // Find the TextView with view ID name
        TextView nameView = listItemView.findViewById(R.id.name);
        // Display the name of the province in current summary in nameView
        nameView.setText(currentSummary.getName());

        TextView abbreviationView = listItemView.findViewById(R.id.abbreviation);
        abbreviationView.setText(currentSummary.getAbbreviation());

        TextView testsView = listItemView.findViewById(R.id.tests);
        testsView.setText(String.valueOf(currentSummary.getTests()));

        TextView recoveredView = listItemView.findViewById(R.id.recovered);
        recoveredView.setText(String.valueOf(currentSummary.getRecovered()));

        TextView activeCasesView = listItemView.findViewById(R.id.active);
        activeCasesView.setText(String.valueOf(currentSummary.getActiveCases()));

        TextView deathsView = listItemView.findViewById(R.id.deaths);
        deathsView.setText(String.valueOf(currentSummary.getDeaths()));

        TextView totalView = listItemView.findViewById(R.id.total);
        totalView.setText(String.valueOf(currentSummary.getTotal()));

        ImageView deleteIcon = listItemView.findViewById(R.id.delete_province);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String provinceName = currentSummary.getName();
                remove(currentSummary);
                SummaryDbHelper dbHelper = new SummaryDbHelper(SummaryAdapter.this.callerContext);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(SummaryEntry.TABLE_NAME, SummaryEntry.COLUMN_PROVINCE_NAME + "=?", new String[] {provinceName});
                Toast.makeText(SummaryAdapter.this.callerContext, String.format("Deleted Province %s", provinceName), Toast.LENGTH_SHORT).show();
            }
        });

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
