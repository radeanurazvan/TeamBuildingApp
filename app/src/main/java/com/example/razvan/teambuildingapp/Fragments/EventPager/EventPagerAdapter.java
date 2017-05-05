package com.example.razvan.teambuildingapp.Fragments.EventPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by LexGrup on 5/4/2017.
 */

public class EventPagerAdapter extends FragmentPagerAdapter {
    private List<Event> mDataSet;
    private Gson mGson;
    private EventDay mParentDay;

    public EventPagerAdapter(EventDay mParentDay, List<Event> mDataSet, FragmentManager mgr) {
        super(mgr);
        mGson = new Gson();
        this.mDataSet = mDataSet;
        this.mParentDay = mParentDay;
    }

    @Override
    public int getCount() {
        return(mDataSet.size());
    }

    @Override
    public Fragment getItem(int position) {
        String jsonEventDay = mGson.toJson(mParentDay);
        String jsonEvent = mGson.toJson(mDataSet.get(position));
        return(EventFragment.newInstance(jsonEvent, jsonEventDay, position, getCount()));
    }

}
