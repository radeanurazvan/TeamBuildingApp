package com.example.razvan.teambuildingapp.OverviewActivityRV;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Razvan on 4/27/2017.
 */

public class SampleData {

    public static List<EventDay> generateSampleEventDaysList() {
        final List<EventDay> eventDayList = new ArrayList<>();

        eventDayList.add(new EventDay(new Date()));
        eventDayList.add(new EventDay(new Date()));
        eventDayList.add(new EventDay(new Date()));
        eventDayList.add(new EventDay(new Date()));
        eventDayList.add(new EventDay(new Date()));
        eventDayList.add(new EventDay(new Date()));
        eventDayList.add(new EventDay(new Date()));
        eventDayList.add(new EventDay(new Date()));

        return eventDayList;
    }

}
