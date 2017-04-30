package com.example.razvan.teambuildingapp.EventDaysRV;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Razvan on 4/27/2017.
 */

public class SampleData {

    public static List<EventDay> generateSampleEventDaysList() {
        final List<EventDay> eventDayList = new ArrayList<>();

        eventDayList.add(new EventDay("1",new Date()));
        eventDayList.add(new EventDay("2",new Date()));
        eventDayList.add(new EventDay("3",new Date()));
        eventDayList.add(new EventDay("4",new Date()));
        eventDayList.add(new EventDay("5",new Date()));
        eventDayList.add(new EventDay("6",new Date()));
        eventDayList.add(new EventDay("7",new Date()));
        eventDayList.add(new EventDay("8",new Date()));

        return eventDayList;
    }

}
