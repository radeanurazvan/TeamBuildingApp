package com.example.razvan.teambuildingapp.Entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Razvan on 5/1/2017.
 */

public class User {
    public String firebaseUserUID, email,  name, birthday, gender;

    @SuppressWarnings("unused")
    public User(){

    }

    public User(String firebaseUserUID, String email,  String name, String birthday, String gender){
        this.firebaseUserUID = firebaseUserUID;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseuseruid", firebaseUserUID);
        result.put("email", email);
        result.put("name", name);
        result.put("birthday", birthday);
        result.put("gender", gender);
        return result;
    }
}
