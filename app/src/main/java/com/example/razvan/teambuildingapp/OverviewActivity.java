package com.example.razvan.teambuildingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.razvan.teambuildingapp.OverviewActivityRV.EventDaysAdapter;
import com.example.razvan.teambuildingapp.OverviewActivityRV.SampleData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewActivity extends AppCompatActivity {

    private EventDaysAdapter mAdapter;

    @BindView(R.id.rv_event_days)
    RecyclerView recyclerViewEventDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ButterKnife.bind(this);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mAdapter = new EventDaysAdapter(SampleData.generateSampleEventDaysList());
        recyclerViewEventDays.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEventDays.setAdapter(mAdapter);
    }
}
