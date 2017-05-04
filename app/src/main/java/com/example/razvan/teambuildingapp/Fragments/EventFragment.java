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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;
import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.FreeEventsRV.SampleData;
import com.example.razvan.teambuildingapp.FreeEventsRV.FreeEventsAdapter;
import com.example.razvan.teambuildingapp.NavigationDrawerActivity;
import com.example.razvan.teambuildingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EVENTKEY = "eventkey";
    private static final String ARG_EVENTDAYKEY = "eventdaykey";
    private static final String ARG_EVENTDATE = "eventdate";
    private static final String TAG = "EVENTFRAGMENT";

    // TODO: Rename and change types of parameters
    private String eventKey;
    private String eventDate;
    private String eventDayKey;

    private FirebaseDatabase mDatabase;

    private Event mEvent;


    private FreeEventsAdapter mAdapter;

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

    @OnClick(R.id.btn_create_event)
    public void openCreateEventFragment(){
        NavigationDrawerActivity navigationDrawerActivity = (NavigationDrawerActivity) getContext();
        navigationDrawerActivity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, CreateEmployeeEventFragment.newInstance(mEvent.getId())).addToBackStack(null).commit();
    }

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventKey Event unique key.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String eventDayKey, String eventKey, String eventDate) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENTDAYKEY, eventDayKey);
        args.putString(ARG_EVENTKEY, eventKey);
        args.putString(ARG_EVENTDATE, eventDate);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        if (getArguments() != null) {
            eventKey = getArguments().getString(ARG_EVENTKEY);
            eventDayKey = getArguments().getString(ARG_EVENTDAYKEY);
            eventDate = getArguments().getString(ARG_EVENTDATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);
        getEventData();
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

    private void getEventData() {
        final Context activityContext = this.getContext();

        DatabaseReference ref = mDatabase.getReference("events/" + eventDayKey + "/" + eventKey);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: handle the
                mEvent = dataSnapshot.getValue(Event.class);
                if(mEvent != null){
                    setViews();
                }
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

    private void setViews(){
        String eventType = mEvent.isMandatory()?"Mandatory":"Free Event";
        tvEventType.setText(eventType);
        tvEventTitle.setText(mEvent.getTitle());

        String eventTimeLocation = "";


        eventTimeLocation += getArguments().getString(ARG_EVENTDATE) + ", " + mEvent.getTimeRange() +", " + mEvent.getLocation();

        tvEventTimeLocation.setText(eventTimeLocation);
        tvEventDescription.setText(mEvent.getDescription());
        if(!mEvent.isMandatory()){
            initRecyclerView();
        }
    }

    private void initRecyclerView(){
        final List<EmployeeEvent> mDataSet = new ArrayList<>();
        DatabaseReference ref = mDatabase.getReference("employeeEvents/" + eventKey);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataSet.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    EmployeeEvent event = eventSnapshot.getValue(EmployeeEvent.class);
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

    private void setUpRecyclerView(List<EmployeeEvent> mDataSet) {
        mAdapter = new FreeEventsAdapter(mDataSet, mEvent,getArguments().getString(ARG_EVENTDATE));
        recyclerViewFreeEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFreeEvents.setAdapter(mAdapter);
    }


}
