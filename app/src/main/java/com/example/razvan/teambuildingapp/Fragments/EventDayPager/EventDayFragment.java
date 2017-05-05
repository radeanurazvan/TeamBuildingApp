package com.example.razvan.teambuildingapp.Fragments.EventDayPager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.EventsRV.EventsAdapter;
import com.example.razvan.teambuildingapp.NavigationDrawerActivity;
import com.example.razvan.teambuildingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDayFragment extends Fragment {
    private static final String ARG_JSON = "jsonobject";
    private static final String ARG_DAYNUMBER = "daynumber";
    private static final String ARG_LASTDAYNUMBER = "lastdaynumber";
    private static final String TAG = "EVENT_DAY_FRAGMENT";

    private int dayNumber;
    private int lastDayNumber;

    private EventDay mEventDay;
    private Gson mGson;

    private EventsAdapter mAdapter;
    private FirebaseDatabase mDatabase;

    private OnButtonClickListener mOnButtonClickListener;

    @BindView(R.id.iv_previous_day)
    ImageView ivPreviousDay;
    @BindView(R.id.iv_next_day)
    ImageView ivNextDay;
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
     * @param eventDayJSON .
     * @param dayNumber    Day Number.
     * @return A new instance of fragment EventDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDayFragment newInstance(String eventDayJSON, int dayNumber, int lastDayNumber) {
        EventDayFragment fragment = new EventDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JSON, eventDayJSON);
        args.putInt(ARG_DAYNUMBER, dayNumber);
        args.putInt(ARG_LASTDAYNUMBER, lastDayNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mGson = new Gson();
        if (getArguments() != null) {
            String jsonEventDay = getArguments().getString(ARG_JSON);
            mEventDay = mGson.fromJson(jsonEventDay, EventDay.class);
            dayNumber = getArguments().getInt(ARG_DAYNUMBER);
            lastDayNumber = getArguments().getInt(ARG_LASTDAYNUMBER);
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
        setViews();
        initRecyclerView();

        initListeners();
        return view;
    }

    interface OnButtonClickListener {
        void onButtonClicked(View view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) context;
            FragmentManager fm = ((NavigationDrawerActivity) context).getSupportFragmentManager();
            mOnButtonClickListener = (OnButtonClickListener) fm.findFragmentByTag("PAGER_EVENT_DAY");
        } catch (ClassCastException e) {
            throw new ClassCastException(((Activity) context).getLocalClassName()
                    + " must implement OnButtonClickListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setUpRecyclerView(List<Event> mDataSet) {
        mAdapter = new EventsAdapter(mDataSet, mEventDay);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewEvents.setAdapter(mAdapter);
    }

    private void initRecyclerView() {
        final List<Event> mDataSet = new ArrayList<>();
        Query ref = mDatabase.getReference("events/" + mEventDay.getId()).orderByChild("startTime/hours");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataSet.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the
                    Event event = eventSnapshot.getValue(Event.class);
                    mDataSet.add(event);
                }
                pbEventDayLoading.setVisibility(View.GONE);
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
        String eventDayDate = mEventDay.getStringDate();
        String eventDayWeekday = new SimpleDateFormat("EEEE").format(mEventDay.getDate());
        tvEventDayDate.setText(eventDayDate);
        tvEventDayWeekday.setText(eventDayWeekday);

        if (dayNumber == 1) {
            ivPreviousDay.setVisibility(View.GONE);
        }
        if (dayNumber == lastDayNumber) {
            ivNextDay.setVisibility(View.GONE);
        }
    }


    private void initListeners(){
        ivNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onButtonClicked(v);
            }
        });
        ivPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onButtonClicked(v);
            }
        });
    }

}
