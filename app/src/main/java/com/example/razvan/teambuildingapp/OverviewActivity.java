package com.example.razvan.teambuildingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;
import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventAttendant;
import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.Entities.User;
import com.example.razvan.teambuildingapp.EventDaysRV.EventDaysAdapter;
import com.example.razvan.teambuildingapp.EventDaysRV.SampleData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        FirebaseDatabase firebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = firebaseInstance.getReference("eventDays");

        String key = mFirebaseDatabase.child("eventDays").push().getKey();
        EventDay eventDay = new EventDay(key,new Date());
        Map<String, Object> eventDayValues = eventDay.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key , eventDayValues);

        mFirebaseDatabase.updateChildren(childUpdates);
    }


}
