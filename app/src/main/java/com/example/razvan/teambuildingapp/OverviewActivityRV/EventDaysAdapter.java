package com.example.razvan.teambuildingapp.OverviewActivityRV;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.razvan.teambuildingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Razvan on 4/27/2017.
 */

public class EventDaysAdapter extends RecyclerView.Adapter<EventDaysAdapter.EventDayViewHolder> {

    private List<EventDay> mDataSet;

    public EventDaysAdapter(@NonNull List<EventDay> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_day, parent, false);
        return new EventDayViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EventDayViewHolder holder, int position) {
        holder.bind(mDataSet.get(position), position);
    }

    // Return the size of the dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // Provide a reference to the views for each data item
    static class EventDayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_day)
        public Button btnDay;

        private Context context;

        EventDayViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull EventDay eventDay, int position) {
            Resources res = this.context.getResources();
            position++;
            String dayText = String.format(res.getString(R.string.btn_day_text), Integer.toString(position));
            btnDay.setText(dayText);
        }
    }

}
