package com.example.razvan.teambuildingapp.Entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Razvan on 5/1/2017.
 */

public class User {
    private String firebaseUserUID, email,  name, birthday, gender, photoUrl;

    @SuppressWarnings("unused")
    public User(){

    }

    public User(String firebaseUserUID, String email,  String name, String birthday, String gender, String photoUrl){
        this.firebaseUserUID = firebaseUserUID;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.photoUrl = photoUrl;
    }

    public String getEmail(){
        return this.email;
    }

    public String getBirthday(){
        return this.birthday;
    }

    public String getGender(){
        return this.gender;
    }

    public String getName(){
        return this.name;
    }

    public String getPhotoUrl(){
        return  this.photoUrl;
    }

    public String getFirebaseUserUID(){
        return  this.firebaseUserUID;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseUserUID", firebaseUserUID);
        result.put("email", email);
        result.put("name", name);
        result.put("birthday", birthday);
        result.put("gender", gender);
        result.put("photoUrl", photoUrl);
        return result;
    }
}
