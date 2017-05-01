package com.example.razvan.teambuildingapp.EventsRV;

import com.example.razvan.teambuildingapp.Entities.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Razvan on 4/30/2017.
 */

public class SampleData {

    public static List<Event> generateSampleEventList() {
        final List<Event> eventsList = new ArrayList<>();

        eventsList.add(new Event("1", "Breakfast", "Hotel Restaurant", "Awesome description", new Date(1493600410), new Date(1493600410),false));
//        eventsList.add(new Event("2", "Capture the flag", "", "Awesome description", "2015-02-09 10:00", "2015-02-09 12:00",false));
//        eventsList.add(new Event("3", "Lunch Break", "Hotel Restaurant", "Awesome description", "2015-02-09 13:00", "2015-02-09 15:00",false));
//        eventsList.add(new Event("4", "Company Update", "Conference Room 101", "Awesome description", "2015-02-09 15:00", "2015-02-09 17:00",false));
//        eventsList.add(new Event("5", "Funny Stuff", "Outside Garden", "Awesome description", "2015-02-09 17:00", "2015-02-09 20:00",false));

        return eventsList;
    }
    
}
