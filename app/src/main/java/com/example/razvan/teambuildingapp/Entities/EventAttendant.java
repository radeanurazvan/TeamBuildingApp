package com.example.razvan.teambuildingapp.Entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Razvan on 5/1/2017.
 */

public class EventAttendant {
    private String id,uid,name,photoUrl;

    @SuppressWarnings("unused")
    public EventAttendant(){}

    public  EventAttendant(String id,String uid, String name, String photoUrl){
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("uid", uid);
        result.put("name", name);
        result.put("photoUrl", photoUrl);
        return result;
    }
}
