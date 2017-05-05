package com.example.razvan.teambuildingapp.Fragments.EventPager;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;
import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.Fragments.CreateEmployeeEventFragment;
import com.example.razvan.teambuildingapp.FreeEventsRV.FreeEventsAdapter;
import com.example.razvan.teambuildingapp.NavigationDrawerActivity;
import com.example.razvan.teambuildingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_JSON_EVENT = "json_event";
    private static final String ARG_JSON_EVENT_DAY = "json_event_day";
    private static final String ARG_EVENT_POSITION = "event_position";
    private static final String ARG_LAST_EVENT_POSITION = "last_event_position";

    private static final String TAG = "EVENTFRAGMENT";

    // TODO: Rename and change types of parameters
    private Event mEvent;
    private EventDay mEventDay;
    private int mEventPosition, mLastEventPosition;

    private FirebaseDatabase mDatabase;
    private OnButtonClickListener mOnButtonClickListener;

    private FreeEventsAdapter mAdapter;

    @BindView(R.id.iv_next_event)
    ImageView ivNextEvent;
    @BindView(R.id.iv_previous_event)
    ImageView ivPreviousEvent;
    @BindView(R.id.tv_event_type)
    TextView tvEventType;
    @BindView(R.id.tv_event_title)
    TextView tvEventTitle;
    @BindView(R.id.tv_event_time_location)
    TextView tvEventTimeLocation;
    @BindView(R.id.tv_event_description)
    TextView tvEventDescription;
    @BindView(R.id.rv_free_events)
    RecyclerView recyclerViewFreeEvents;
    @BindView(R.id.btn_create_event)
    Button btnCreateEvent;
    @BindView(R.id.pb_free_events_loading)
    ProgressBar pbFreeEventsLoading;


    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(String jsonEvent, String jsonEventDay, int eventPosition, int lastEventPosition) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JSON_EVENT, jsonEvent);
        args.putString(ARG_JSON_EVENT_DAY, jsonEventDay);
        args.putInt(ARG_EVENT_POSITION, eventPosition);
        args.putInt(ARG_LAST_EVENT_POSITION, lastEventPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        Gson mGson = new Gson();
        if (getArguments() != null) {
            String jsonEvent = getArguments().getString(ARG_JSON_EVENT);
            String jsonEventDay = getArguments().getString(ARG_JSON_EVENT_DAY);
            mEvent = mGson.fromJson(jsonEvent, Event.class);
            mEventDay = mGson.fromJson(jsonEventDay, EventDay.class);
            mEventPosition = getArguments().getInt(ARG_EVENT_POSITION);
            mLastEventPosition = getArguments().getInt(ARG_LAST_EVENT_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);
        setViews();
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
            mOnButtonClickListener = (OnButtonClickListener) fm.findFragmentByTag("PAGER_EVENT");
        } catch (ClassCastException e) {
            throw new ClassCastException(((Activity) context).getLocalClassName()
                    + " must implement OnButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setViews(){
        String eventType = mEvent.isMandatory()?"Mandatory":"Free Event";
        tvEventType.setText(eventType);
        tvEventTitle.setText(mEvent.getTitle());

        String eventTimeLocation = "";


        eventTimeLocation += mEventDay.getStringDate() + ", " + mEvent.getTimeRange() +", " + mEvent.getLocation();

        tvEventTimeLocation.setText(eventTimeLocation);
        tvEventDescription.setText(mEvent.getDescription());

        Log.i("eventFragment", Integer.toString(mEventPosition)+"  "+Integer.toString(mLastEventPosition));
        if(mEventPosition == 0){
            ivPreviousEvent.setVisibility(View.GONE);
        }
        if(mEventPosition == mLastEventPosition-1){
            ivNextEvent.setVisibility(View.GONE);
        }

        if(!mEvent.isMandatory()){
            setCreateEventListener();
            initRecyclerView();
        } else {
            btnCreateEvent.setVisibility(View.GONE);
            pbFreeEventsLoading.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView(){
        final List<EmployeeEvent> mDataSet = new ArrayList<>();
        Query ref = mDatabase.getReference("employeeEvents/" + mEvent.getId()).orderByChild("startTime/hours");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataSet.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    EmployeeEvent event = eventSnapshot.getValue(EmployeeEvent.class);
                    mDataSet.add(event);
                }
                pbFreeEventsLoading.setVisibility(View.GONE);
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

    private void setUpRecyclerView(List<EmployeeEvent> mDataSet) {
        mAdapter = new FreeEventsAdapter(mDataSet, mEvent, mEventDay.getStringDate());
        recyclerViewFreeEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFreeEvents.setAdapter(mAdapter);
    }

    private void initListeners(){
        ivNextEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onButtonClicked(v);
            }
        });
        ivPreviousEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onButtonClicked(v);
            }
        });
    }

    private void setCreateEventListener(){
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) getContext();
                navigationDrawerActivity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, CreateEmployeeEventFragment.newInstance(mEvent)).addToBackStack(null).commit();
            }
        });
    }
}
