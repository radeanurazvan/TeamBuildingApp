package com.example.razvan.teambuildingapp.Entities;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Razvan on 5/1/2017.
 */

public class EmployeeEvent {
    private String id;
    private String title;
    private String location;
    private String description;
    private Date startTime;
    private Date endTime;
    private List<EventAttendant> attendants;

    @SuppressWarnings("unused")
    public EmployeeEvent(){}

    public EmployeeEvent(String id, String title, String location, String description, Date startTime, Date endTime){
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getLocation(){
        return this.location;
    }

    public String getDescription(){
        return this.description;
    }

    public Date getStarttime(){
        return this.startTime;
    }

    public Date getEndTime(){
        return this.endTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("location", location);
        result.put("description", description);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("attendants", attendants);
        return result;
    }
}
