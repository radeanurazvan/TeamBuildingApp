package com.example.razvan.teambuildingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;
import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventAttendant;
import com.example.razvan.teambuildingapp.EventAttendantsRV.EventAttendantsAdapter;
import com.example.razvan.teambuildingapp.R;
import com.example.razvan.teambuildingapp.Utils.EventAttendantsUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FreeEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARENTEVENTKEY = "parenteventkey";
    private static final String ARG_THISEVENTKEY = "thiseventkey";
    private static final String ARG_EVENTDAYDATE = "eventdaydate";
    private static final String TAG = "FREEEVENTFRAGMENT";

    // TODO: Rename and change types of parameters
    private String parentEventKey;
    private String thisEventKey;
    private String eventDayDate;

    private EmployeeEvent thisEvent;

    private FirebaseDatabase mDatabase;

    private EventAttendantsAdapter mAdapter;

    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.tv_event_host)
    TextView tvEventHost;
    @BindView(R.id.tv_event_title)
    TextView tvEventTitle;
    @BindView(R.id.tv_event_time_location)
    TextView tvEventTimeLocation;
    @BindView(R.id.tv_event_description)
    TextView tvEventDescription;
    @BindView(R.id.rv_event_attendants)
    RecyclerView recyclerViewEventAttendants;

    public FreeEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parentEventKey parentEventKey .
     * @param thisEventKey   thisEventKey .
     * @param eventDayDate   eventDayDate .
     * @return A new instance of fragment FreeEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FreeEventFragment newInstance(String parentEventKey, String thisEventKey, String eventDayDate) {
        FreeEventFragment fragment = new FreeEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARENTEVENTKEY, parentEventKey);
        args.putString(ARG_THISEVENTKEY, thisEventKey);
        args.putString(ARG_EVENTDAYDATE, eventDayDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        if (getArguments() != null) {
            parentEventKey = getArguments().getString(ARG_PARENTEVENTKEY);
            thisEventKey = getArguments().getString(ARG_THISEVENTKEY);
            eventDayDate = getArguments().getString(ARG_EVENTDAYDATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_event, container, false);
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        getEventData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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

        String node = "employeeEvents/" + parentEventKey + "/" + thisEventKey;
//        Log.i(TAG, "get event node: "+node);
        DatabaseReference ref = mDatabase.getReference(node);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: handle the
                final EmployeeEvent event = dataSnapshot.getValue(EmployeeEvent.class);
                thisEvent = event;
                if (event != null) {
                    setViews(event);
                    Log.i("eventhostid", event.getHostId() + " == "+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (!TextUtils.equals(event.getHostId(),FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        EventAttendantsUtils.setEventButtonText(event, btnJoin);
                        btnJoin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventAttendantsUtils.joinEmployeeEvent(parentEventKey, event, getContext());
                            }
                        });
                    } else {
                        btnJoin.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting EventDay failed, log a message
                Log.w(TAG, "loadEventDay:onCancelled", databaseError.toException());
//                Toast.makeText(activityContext, "Failed getting data from server.Try again", Toast.LENGTH_SHORT).show();

                // ...
            }
        });

    }

    private void setViews(EmployeeEvent event) {
        tvEventHost.setText("Event hosted by " + event.getHost());
        tvEventTitle.setText(event.getTitle());
        tvEventTimeLocation.setText(eventDayDate + ", " + event.getTimeRange() + ", " + event.getLocation());
        tvEventDescription.setText(event.getDescription());

        initRecyclerView();
    }

    private void initRecyclerView() {
        final List<EventAttendant> mDataSet = new ArrayList<EventAttendant>();
        String node = "attendants/" + thisEventKey;
        DatabaseReference ref = mDatabase.getReference(node);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EventAttendantsUtils.setEventButtonText(thisEvent, btnJoin);
                mDataSet.clear();
                for (DataSnapshot eventAttendantSnapshot : dataSnapshot.getChildren()) {
                    EventAttendant attendant = eventAttendantSnapshot.getValue(EventAttendant.class);
                    mDataSet.add(attendant);
                }
//                pbOverview.setVisibility(View.GONE);
                setUpRecyclerView(mDataSet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting EventDay failed, log a message
                Log.w(TAG, "loadEventDay:onCancelled", databaseError.toException());
//                Toast.makeText(activityContext, "Failed getting data from server.Try again", Toast.LENGTH_SHORT).show();

                // ...
            }
        });
    }

    private void setUpRecyclerView(List<EventAttendant> mDataSet) {
        mAdapter = new EventAttendantsAdapter(mDataSet);
        recyclerViewEventAttendants.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewEventAttendants.setAdapter(mAdapter);
    }
}
