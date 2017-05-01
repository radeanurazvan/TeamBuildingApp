package com.example.razvan.teambuildingapp.FreeEventsRV;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;
import com.example.razvan.teambuildingapp.Fragments.FreeEventFragment;
import com.example.razvan.teambuildingapp.NavigationDrawerActivity;
import com.example.razvan.teambuildingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Razvan on 5/1/2017.
 */

public class FreeEventsAdapter extends RecyclerView.Adapter<FreeEventsAdapter.FreeEventViewHolder>{
    private List<EmployeeEvent> mDataSet;
    private NavigationDrawerActivity navigationDrawerActivity;

    public FreeEventsAdapter(@NonNull List<EmployeeEvent> dataSet, Context context) {
        mDataSet = dataSet;
        navigationDrawerActivity = (NavigationDrawerActivity) context;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public FreeEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_free_event, parent, false);

        return new FreeEventViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FreeEventViewHolder holder, int position) {
        EmployeeEvent employeeEvent = mDataSet.get(position);
        holder.bind(employeeEvent, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationDrawerActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FreeEventFragment.newInstance("","")).commitNow();
            }
        });
    }

    // Return the size of the dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // Provide a reference to the views for each data item
    static class FreeEventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_free_event_title)
        TextView tvEventTitle;
        @BindView(R.id.tv_free_event_location)
        TextView tvEventLocation;
        @BindView(R.id.tv_free_event_time)
        TextView tvEventTime;


        FreeEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull EmployeeEvent event, int position) {
            Date startHour= new Date(),endHour = new Date();
            tvEventTitle.setText(event.getTitle());
            tvEventLocation.setText(event.getLocation());
            String oldstring = "2011-01-18 00:00:00.0";
            SimpleDateFormat dt = new SimpleDateFormat("k");
//            try {
//                startHour = dt.parse(event.getStarttime());
//                endHour = dt.parse(event.getEndTime());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


            tvEventTime.setText("8 - 10am");
        }
    }
}
