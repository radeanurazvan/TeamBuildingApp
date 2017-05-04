package com.example.razvan.teambuildingapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.EventDaysRV.EventDaysAdapter;
import com.example.razvan.teambuildingapp.EventDaysRV.SampleData;
import com.example.razvan.teambuildingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "OVERVIEW_FRAGMENT";

    private EventDaysAdapter mAdapter;

    @BindView(R.id.pb_overview)
    ProgressBar pbOverview;
    @BindView(R.id.rv_event_days)
    RecyclerView recyclerViewEventDays;

    public OverviewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);

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


    private void setUpRecyclerView(List<EventDay> mDataSet) {
        mAdapter = new EventDaysAdapter(mDataSet);
        recyclerViewEventDays.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewEventDays.setAdapter(mAdapter);
    }

    private void initRecyclerView(){
        final List<EventDay> mDataSet = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("eventDays");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataSet.clear();
                for (DataSnapshot eventDaySnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the
                    EventDay eventDay = eventDaySnapshot.getValue(EventDay.class);
                    mDataSet.add(eventDay);
                }
                pbOverview.setVisibility(View.GONE);
                setUpRecyclerView(mDataSet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
}
