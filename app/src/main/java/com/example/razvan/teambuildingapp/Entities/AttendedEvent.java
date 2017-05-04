package com.example.razvan.teambuildingapp.Entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LexGrup on 5/3/2017.
 */

public class AttendedEvent {
    private EmployeeEvent event;
    private String attendantId;

    public AttendedEvent(){};

    public AttendedEvent(EmployeeEvent event, String attendantId){
        this.event = event;
        this.attendantId=attendantId;
    }

    public EmployeeEvent getEvent(){
        return this.event;
    }

    public String getAttendantId(){
        return this.attendantId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("event", event);
        result.put("attendantId", attendantId);
        return result;
    }
}
