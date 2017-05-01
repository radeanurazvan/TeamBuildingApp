package com.example.razvan.teambuildingapp.FreeEventsRV;


import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Razvan on 5/1/2017.
 */

public class SampleData {
    public static List<EmployeeEvent> generateSampleFreeEventsList(){
        final List<EmployeeEvent> employeeEventsList = new ArrayList<>();

        employeeEventsList.add(new EmployeeEvent("1", "Breakfast", "Hotel Restaurant", "Awesome description", new Date(), new Date()));
//        employeeEventsList.add(new EmployeeEvent("2", "Capture the flag", "", "Awesome description", "2015-02-09 10:00", "2015-02-09 12:00"));
//        employeeEventsList.add(new EmployeeEvent("3", "Lunch Break", "Hotel Restaurant", "Awesome description", "2015-02-09 13:00", "2015-02-09 15:00"));
//        employeeEventsList.add(new EmployeeEvent("4", "Company Update", "Conference Room 101", "Awesome description", "2015-02-09 15:00", "2015-02-09 17:00"));
//        employeeEventsList.add(new EmployeeEvent("5", "Funny Stuff", "Outside Garden", "Awesome description", "2015-02-09 17:00", "2015-02-09 20:00"));

        return employeeEventsList;
    }
}
