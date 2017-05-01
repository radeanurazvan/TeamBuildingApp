package com.example.razvan.teambuildingapp.Entities;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Razvan on 4/27/2017.
 */

public class EventDay {
    private Date date;
    private String id;

    @SuppressWarnings("unused")
    public EventDay(){}

    public  EventDay(String id,Date date){
        this.id = id;
        this.date = date;
    }

    public String getId(){return  this.id;}
    public Date getDate(){
        return this.date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("date", date);
        return result;
    }
}
