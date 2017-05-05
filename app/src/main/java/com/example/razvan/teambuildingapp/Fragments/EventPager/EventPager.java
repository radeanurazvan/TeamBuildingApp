package com.example.razvan.teambuildingapp.Fragments.EventPager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventDay;
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

/**
 * Created by LexGrup on 5/4/2017.
 */

public class EventPager extends Fragment implements EventFragment.OnButtonClickListener{
    private static final String ARG_JSON_EVENT_DAY = "json_event_day";
    private static final String ARG_EVENT_POSITION = "event_position";
    private static final String TAG = "EVENT_PAGER";

    private FirebaseDatabase mDatabase;
    private static Gson mGson;

    private EventDay mParentDay;
    private int eventPosition;

    @BindView(R.id.pager_event)
    ViewPager pagerEvent;

    public EventPager() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EventPager.
     */
    // TODO: Rename and change types and number of parameters
    public static EventPager newInstance(EventDay mParentDay,int position) {
        EventPager fragment = new EventPager();
        Bundle args = new Bundle();
//        args.putString(ARG_DAYKEY, dayKey);
        mGson = new Gson();
        String jsonEventDay = mGson.toJson(mParentDay);
        args.putString(ARG_JSON_EVENT_DAY, jsonEventDay);
        args.putInt(ARG_EVENT_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mGson = new Gson();
        if (getArguments() != null) {
            mParentDay = mGson.fromJson(getArguments().getString(ARG_JSON_EVENT_DAY), EventDay.class);
            eventPosition = getArguments().getInt(ARG_EVENT_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pager_event, container, false);
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

    private PagerAdapter buildAdapter(List<Event> mDataSet) {
        return(new EventPagerAdapter(mParentDay ,mDataSet, getChildFragmentManager()));
    }

    private void initPager(){
        final List<Event> mDataSet = new ArrayList<>();
        Query ref = mDatabase.getReference("events/" + mParentDay.getId()).orderByChild("startTime/hours");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataSet.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the
                    Event event = eventSnapshot.getValue(Event.class);
                    mDataSet.add(event);
                }
                pagerEvent.setAdapter(buildAdapter(mDataSet));
                pagerEvent.setCurrentItem(eventPosition);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Event failed, log a message
                Log.w(TAG, "loadEvent:onCancelled", databaseError.toException());
                // ...
            }
        });


    }

    @Override
    public void onButtonClicked(View view){
        int currPos=pagerEvent.getCurrentItem();

        switch(view.getId()){

            case R.id.iv_next_event:
                //handle currPos is zero
                pagerEvent.setCurrentItem(currPos+1);
                break;

            case R.id.iv_previous_event:
                //handle currPos is zero
                pagerEvent.setCurrentItem(currPos-1);
                break;
        }
    }
}
