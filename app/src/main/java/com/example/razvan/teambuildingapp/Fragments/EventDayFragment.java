package com.example.razvan.teambuildingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.razvan.teambuildingapp.EventsRV.EventsAdapter;
import com.example.razvan.teambuildingapp.EventsRV.SampleData;
import com.example.razvan.teambuildingapp.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_DAYNUMBER = "daynumber";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EventsAdapter mAdapter;

    @BindView(R.id.tv_day_count)
    TextView tvDayCount;

    @BindView(R.id.rv_events)
    RecyclerView recyclerViewEvents;

    public EventDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDayFragment newInstance(String param1, String param2, String dayNumber) {
        EventDayFragment fragment = new EventDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_DAYNUMBER, dayNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_day, container, false);
        ButterKnife.bind(this, view);
        tvDayCount.setText(getArguments().getString(ARG_DAYNUMBER));
        setUpRecyclerView();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setUpRecyclerView() {
        mAdapter = new EventsAdapter(SampleData.generateSampleEventList(), this.getContext());
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewEvents.setAdapter(mAdapter);
    }
}
