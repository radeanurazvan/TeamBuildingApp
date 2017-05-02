package com.example.razvan.teambuildingapp.EventsRV;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.Fragments.EventFragment;
import com.example.razvan.teambuildingapp.NavigationDrawerActivity;
import com.example.razvan.teambuildingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Razvan on 4/30/2017.
 */

public class EventsAdapter  extends RecyclerView.Adapter<EventsAdapter.EventViewHolder>{
    private List<Event> mDataSet;
    private NavigationDrawerActivity navigationDrawerActivity;
    private EventDay mEventDay;

    public EventsAdapter(@NonNull List<Event> dataSet,EventDay parentEventDay) {
        mDataSet = dataSet;

        this.mEventDay = parentEventDay;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new EventViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        final Event event = mDataSet.get(position);
        holder.bind(event, position);
        navigationDrawerActivity = (NavigationDrawerActivity) holder.tvEventLocation.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationDrawerActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EventFragment.newInstance(mEventDay.getId(),event.getId(), mEventDay.getStringDate())).commitNow();
            }
        });
    }

    // Return the size of the dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // Provide a reference to the views for each data item
    static class EventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_event_title)
        TextView tvEventTitle;
        @BindView(R.id.tv_event_location)
        TextView tvEventLocation;
        @BindView(R.id.tv_event_time)
        TextView tvEventTime;


        EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull Event event, int position) {
            tvEventTitle.setText(event.getTitle());
            tvEventLocation.setText(event.getLocation());

            String eventStartHour = new SimpleDateFormat("k").format(event.getStartTime());
            String eventEndHour = new SimpleDateFormat("k aaa").format(event.getEndTime());
            tvEventTime.setText(eventStartHour + " - " + eventEndHour);
        }
    }
}
