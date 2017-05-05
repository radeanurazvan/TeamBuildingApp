package com.example.razvan.teambuildingapp.Fragments.EventDayPager;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LexGrup on 5/4/2017.
 */

public class EventDayPager extends Fragment implements EventDayFragment.OnButtonClickListener {
    private static final String ARG_DAYPOSITION = "dayposition";
    private static final String TAG = "EVENT_DAY_FRAGMENT";

    private int dayPosition;

    @BindView(R.id.pager_event_day)
    ViewPager pagerEventDay;

    public EventDayPager() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EventDayPager.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDayPager newInstance(int position) {
        EventDayPager fragment = new EventDayPager();
        Bundle args = new Bundle();
//        args.putString(ARG_DAYKEY, dayKey);
        args.putInt(ARG_DAYPOSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayPosition = getArguments().getInt(ARG_DAYPOSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pager_event_day, container, false);
        ButterKnife.bind(this, view);

        initPager();
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

    private PagerAdapter buildAdapter(List<EventDay> mDataSet) {
        return(new EventDayPagerAdapter(mDataSet, getChildFragmentManager()));
    }

    private void initPager(){
        final List<EventDay> mDataSet = new ArrayList<>();
        Query database = FirebaseDatabase.getInstance().getReference("eventDays").orderByChild("date/date");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataSet.clear();
                for (DataSnapshot eventDaySnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the
                    EventDay eventDay = eventDaySnapshot.getValue(EventDay.class);
                    mDataSet.add(eventDay);
                }
                pagerEventDay.setAdapter(buildAdapter(mDataSet));
                pagerEventDay.setCurrentItem(dayPosition);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting eventDay failed, log a message
                Log.w(TAG, "loadEventDay:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    @Override
    public void onButtonClicked(View view){
        int currPos=pagerEventDay.getCurrentItem();

        switch(view.getId()){

            case R.id.iv_next_day:
                //handle currPos is zero
                pagerEventDay.setCurrentItem(currPos+1);
                break;

            case R.id.iv_previous_day:
                //handle currPos is zero
                pagerEventDay.setCurrentItem(currPos-1);
                break;
        }
    }

}
