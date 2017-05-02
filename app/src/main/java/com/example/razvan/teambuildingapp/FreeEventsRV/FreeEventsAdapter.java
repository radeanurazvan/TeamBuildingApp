package com.example.razvan.teambuildingapp.FreeEventsRV;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;
import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventAttendant;
import com.example.razvan.teambuildingapp.EventAttendantsRV.EventAttendantsAdapter;
import com.example.razvan.teambuildingapp.Fragments.FreeEventFragment;
import com.example.razvan.teambuildingapp.NavigationDrawerActivity;
import com.example.razvan.teambuildingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Event mParentEvent;
    private String eventDayDate;
    public FreeEventsAdapter(@NonNull List<EmployeeEvent> dataSet, Event parentEvent, String eventDayDate) {
        mDataSet = dataSet;
        mParentEvent = parentEvent;
        this.eventDayDate = eventDayDate;
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
        final EmployeeEvent employeeEvent = mDataSet.get(position);
        holder.bind(employeeEvent, position);

        navigationDrawerActivity = (NavigationDrawerActivity) holder.tvEventLocation.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationDrawerActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FreeEventFragment.newInstance(mParentEvent.getId(), employeeEvent.getId(), eventDayDate)).commitNow();
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
        @BindView(R.id.tv_free_event_stats)
        TextView tvEventStats;
//        @BindView(R.id.rv_event_attendants)
//        RecyclerView rvEventAttendants;
//        static EventAttendantsAdapter mAdapter;


        FreeEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull EmployeeEvent event, int position) {
            tvEventTitle.setText(event.getTitle());
            tvEventLocation.setText(event.getLocation());
            tvEventTime.setText(event.getTimeRange());

            tvEventStats.setText(event.getStats());
//            initRecyclerView(event);
        }

//        void initRecyclerView(EmployeeEvent event){
//            final List<EventAttendant> mDataSet = new ArrayList<EventAttendant>();
//            String node = "attendants/" + event.getId();
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(node);
//            ref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot eventAttendantSnapshot : dataSnapshot.getChildren()) {
//                        EventAttendant attendant = eventAttendantSnapshot.getValue(EventAttendant.class);
//                        Log.i("asd", "added attendant");
//                        mDataSet.add(attendant);
//                    }
////                pbOverview.setVisibility(View.GONE);
//                    setUpRecyclerView(mDataSet);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Getting EventDay failed, log a message
//
//                    // ...
//                }
//            });
//        }
//
//        void setUpRecyclerView(List<EventAttendant> mDataSet){
//            mAdapter = new EventAttendantsAdapter(mDataSet);
//            rvEventAttendants.setLayoutManager(new LinearLayoutManager(tvEventLocation.getContext(), LinearLayoutManager.HORIZONTAL,false));
//            rvEventAttendants.setAdapter(mAdapter);
//        }
    }
}
