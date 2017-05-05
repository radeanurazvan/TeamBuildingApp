package com.example.razvan.teambuildingapp.Entities;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
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
    private String host;
    private String hostId;
    private Date startTime;
    private Date endTime;

    private int numberOfAttendants;

    @SuppressWarnings("unused")
    public EmployeeEvent(){}

    public EmployeeEvent(String id, String title, String location, String description, String host, String hostId,Date startTime, Date endTime){
        this.id = id;
        this.title = title;
        this.location = location;
        this.host = host;
        this.hostId = hostId;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfAttendants = 0;
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

    public Date getStartTime(){
        return this.startTime;
    }

    public Date getEndTime(){
        return this.endTime;
    }

    public String getHost() {
        return this.host;
    }

    public String getHostId(){
        return this.hostId;
    }

    public int getNumberOfAttendants(){return this.numberOfAttendants;}

    public String getTimeRange(){
        String startTimeformat,endTimeFormat;
        startTimeformat = endTimeFormat = "k";
        if(getStartTime().getMinutes() != 0){
            startTimeformat = "k:mm";
        }
        if(getEndTime().getMinutes() != 0){
            endTimeFormat = "k:mm";
        }

        String eventStartHour = new SimpleDateFormat(startTimeformat).format(getStartTime());
        String eventEndHour = new SimpleDateFormat(endTimeFormat).format(getEndTime());
        return eventStartHour + " - " + eventEndHour;
    }

    public String getStats(){
        return numberOfAttendants + " attendants, hosted by " + host ;
    }



    public void setNumberOfAttendants(int number){
        this.numberOfAttendants = number;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("location", location);
        result.put("host", host);
        result.put("hostId", hostId);
        result.put("description", description);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        return result;
    }
}
