package com.cs446.covidtracer.ui.tracing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.covidtracer.R;

import java.util.ArrayList;

public class TracingAdapter extends RecyclerView.Adapter<TracingAdapter.TracingViewHolder>{

    private ArrayList<TracingItem> mTracingList;

    public static class TracingViewHolder extends RecyclerView.ViewHolder {

        public TextView mRiskFactorView;
        public TextView mDateFactorView;
        public TextView mDurationFactorView;
        public CardView mTracingCard;
        public TracingViewHolder(@NonNull View itemView) {
            super(itemView);
            mRiskFactorView = itemView.findViewById(R.id.riskFactor);
            mDateFactorView = itemView.findViewById(R.id.dateFactor);
            mDurationFactorView = itemView.findViewById(R.id.durationFactor);
            mTracingCard = itemView.findViewById((R.id.tracingCard));
        }
    }

    public TracingAdapter(ArrayList<TracingItem> tracingList){
        mTracingList = tracingList;
    }

    @NonNull
    @Override
    public TracingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracing_list_item, parent, false);
        TracingViewHolder tvh = new TracingViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TracingViewHolder holder, int position) {
        TracingItem currentItem = mTracingList.get(position);

        holder.mRiskFactorView.setText(currentItem.getRisk());
        holder.mDateFactorView.setText(currentItem.getDaysAgo());
        holder.mDurationFactorView.setText(currentItem.getDurationString());
        holder.mTracingCard.setCardBackgroundColor(currentItem.getBackgroundColor());
    }

    @Override
    public int getItemCount() {
        return mTracingList.size();
    }
}
