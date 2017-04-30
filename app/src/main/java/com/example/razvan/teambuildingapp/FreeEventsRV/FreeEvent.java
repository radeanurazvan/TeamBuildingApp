package com.example.razvan.teambuildingapp.FreeEventsRV;

/**
 * Created by Razvan on 5/1/2017.
 */

public class FreeEvent {
    private String id;
    private String title;
    private String location;
    private String description;
    private String startTime;
    private String endTime;

    public FreeEvent(String id, String title, String location, String description, String startTime, String endTime){
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

    public String getStarttime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }
}
