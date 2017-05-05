package com.example.razvan.teambuildingapp.Fragments.EventDayPager;


import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.R;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

/**
 * Created by LexGrup on 5/4/2017.
 */

public class EventDayPagerAdapter extends FragmentPagerAdapter {
    List<EventDay> mDataSet;
    private Gson mGson;

    public EventDayPagerAdapter(List<EventDay> mDataSet, FragmentManager mgr) {
        super(mgr);
        mGson = new Gson();
        this.mDataSet = mDataSet;
    }

    @Override
    public int getCount() {
        return(mDataSet.size());
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("pagerEventDay", Integer.toString(position));
        String jsonEventDay = mGson.toJson(mDataSet.get(position));
        return(EventDayFragment.newInstance(jsonEventDay,position+1, mDataSet.size()));
    }

    @Override
    public String getPageTitle(int position) {
        return"";
    }
}
