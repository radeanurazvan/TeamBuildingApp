package com.example.razvan.teambuildingapp.EventDaysRV;

import java.util.Date;

/**
 * Created by Razvan on 4/27/2017.
 */

public class EventDay {
    private Date date;
    private String id;

    public  EventDay(String id,Date date){
        this.id = id;
        this.date = date;
    }

    public String getId(){return  this.id;}
    public Date getDate(){
        return this.date;
    }
}
