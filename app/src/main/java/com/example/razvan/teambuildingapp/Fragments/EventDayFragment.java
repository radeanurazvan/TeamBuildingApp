package com.example.razvan.teambuildingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.EventsRV.EventsAdapter;
import com.example.razvan.teambuildingapp.EventsRV.SampleData;
import com.example.razvan.teambuildingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDayFragment extends Fragment {
    private static final String ARG_DAYKEY = "daykey";
    private static final String ARG_DAYNUMBER = "daynumber";
    private static final String TAG = "EVENT_DAY_FRAGMENT";

    private String dayKey;
    private int dayNumber;

    private EventDay mEventDay;

    private EventsAdapter mAdapter;
    private FirebaseDatabase mDatabase;

    @BindView(R.id.pb_event_day_loading)
    ProgressBar pbEventDayLoading;
    @BindView(R.id.tv_event_day_weekday)
    TextView tvEventDayWeekday;
    @BindView(R.id.tv_event_day_date)
    TextView tvEventDayDate;
    @BindView(R.id.tv_day_count)
    TextView tvDayCount;

    @BindView(R.id.rv_events)
    RecyclerView recyclerViewEvents;

    public EventDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dayKey    Day Unique Key.
     * @param dayNumber Day Number.
     * @return A new instance of fragment EventDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDayFragment newInstance(String dayKey, int dayNumber) {
        EventDayFragment fragment = new EventDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAYKEY, dayKey);
        args.putInt(ARG_DAYNUMBER, dayNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        if (getArguments() != null) {
            dayKey = getArguments().getString(ARG_DAYKEY);
            dayNumber = getArguments().getInt(ARG_DAYNUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_day, container, false);
        ButterKnife.bind(this, view);

        int dayNumber = getArguments().getInt(ARG_DAYNUMBER);
        tvDayCount.setText("Day " + Integer.toString(dayNumber));
        getEventDayData();
        initRecyclerView();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getEventDayData() {
        final Context activityContext = this.getContext();

        DatabaseReference ref = mDatabase.getReference("eventDays/" + dayKey);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    // TODO: handle the
                    mEventDay = dataSnapshot.getValue(EventDay.class);
                    setViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting EventDay failed, log a message
                Log.w(TAG, "loadEventDay:onCancelled", databaseError.toException());
                Toast.makeText(activityContext, "Failed getting data from server.Try again", Toast.LENGTH_SHORT).show();

                // ...
            }
        });

    }


    private void setUpRecyclerView(List<Event> mDataSet) {
        mAdapter = new EventsAdapter(mDataSet,  mEventDay);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewEvents.setAdapter(mAdapter);
    }

    private void initRecyclerView() {
        final List<Event> mDataSet = new ArrayList<>();
        DatabaseReference ref = mDatabase.getReference("events/" + dayKey);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the
                    Event event = eventSnapshot.getValue(Event.class);
                    mDataSet.add(event);
                }
//                pbOverview.setVisibility(View.GONE);
                setUpRecyclerView(mDataSet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting EventDay failed, log a message
                Log.w(TAG, "loadEventDay:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void setViews() {
        pbEventDayLoading.setVisibility(View.GONE);
        String eventDayDate = mEventDay.getStringDate();
        String eventDayWeekday = new SimpleDateFormat("EEEE").format(mEventDay.getDate());
        tvEventDayDate.setText(eventDayDate);
        tvEventDayWeekday.setText(eventDayWeekday);
    }
}
