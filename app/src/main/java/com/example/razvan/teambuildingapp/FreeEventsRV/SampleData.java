package com.example.razvan.teambuildingapp.FreeEventsRV;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Razvan on 5/1/2017.
 */

public class SampleData {
    public static List<FreeEvent> generateSampleFreeEventsList(){
        final List<FreeEvent> freeEventsList = new ArrayList<>();

        freeEventsList.add(new FreeEvent("1", "Breakfast", "Hotel Restaurant", "Awesome description", "2015-02-09 08:00", "2015-02-09 10:00"));
        freeEventsList.add(new FreeEvent("2", "Capture the flag", "", "Awesome description", "2015-02-09 10:00", "2015-02-09 12:00"));
        freeEventsList.add(new FreeEvent("3", "Lunch Break", "Hotel Restaurant", "Awesome description", "2015-02-09 13:00", "2015-02-09 15:00"));
        freeEventsList.add(new FreeEvent("4", "Company Update", "Conference Room 101", "Awesome description", "2015-02-09 15:00", "2015-02-09 17:00"));
        freeEventsList.add(new FreeEvent("5", "Funny Stuff", "Outside Garden", "Awesome description", "2015-02-09 17:00", "2015-02-09 20:00"));

        return freeEventsList;
    }
}
