package com.example.razvan.teambuildingapp.Fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;
import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.User;
import com.example.razvan.teambuildingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;



public class CreateEmployeeEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARENTEVENT = "ARG_PARENTEVENT";

    // TODO: Rename and change types of parameters
    private Event mParentEvent;

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.pb_creating_event)
    ProgressBar pbCreatingEvent;

    public CreateEmployeeEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mParentEvent parentEvent
     * @return A new instance of fragment CreateEmployeeEventFragment.
     */
    public static CreateEmployeeEventFragment newInstance(Event mParentEvent) {
        CreateEmployeeEventFragment fragment = new CreateEmployeeEventFragment();
        Bundle args = new Bundle();
        Gson mGson = new Gson();
        String jsonParentEvent = mGson.toJson(mParentEvent);
        args.putString(ARG_PARENTEVENT, jsonParentEvent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson mGson = new Gson();
        if (getArguments() != null) {
            String jsonParentEvent = getArguments().getString(ARG_PARENTEVENT);
            mParentEvent = mGson.fromJson(jsonParentEvent, Event.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_employee_event, container, false);
        ButterKnife.bind(this, view);

        initListeners();
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

    private void setTimePickerListeners(){
        tvStartTime.setOnClickListener(initTimePickerDialog(false, tvStartTime));
        tvEndTime.setOnClickListener(initTimePickerDialog(true, tvEndTime));
    }

    private void initListeners(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFormValidated()){
                    pbCreatingEvent.setVisibility(View.VISIBLE);
                    btnSubmit.setEnabled(false);
                    initCreateEvent();
                }
            }
        });
        setTimePickerListeners();
    }

    private View.OnClickListener initTimePickerDialog(final boolean isEndTime, final TextView tv){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                if(isEndTime){
                    hour += 1;
                }
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String newSelectedMinute = Integer.toString(selectedMinute);
                        if(selectedMinute < 10)
                            newSelectedMinute = "0"+newSelectedMinute;
                        tv.setText( selectedHour + ":" + newSelectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };
    }

    private boolean isFormValidated(){
        String title = etTitle.getText().toString();
        String location = etLocation.getText().toString();
        String description = etDescription.getText().toString();
        String startTime = tvStartTime.getText().toString();
        String endTime = tvEndTime.getText().toString();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(getContext(), "Enter title!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(location)){
            Toast.makeText(getContext(), "Enter location!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(description)){
            Toast.makeText(getContext(), "Enter description!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(startTime)){
            Toast.makeText(getContext(), "Enter start time!",Toast.LENGTH_SHORT).show();
            return false;
        }


        if(TextUtils.isEmpty(endTime)){
            Toast.makeText(getContext(), "Enter end time!",Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("H:m");
        try{
            Date startTimeDate = formatter.parse(startTime);
            Date endTimeDate = formatter.parse(endTime);
            if (startTimeDate.compareTo(endTimeDate)>0){
                Toast.makeText(getContext(), "End time must be greater than start time!",Toast.LENGTH_SHORT).show();
                return false;
            }

            String parentEventStartHour = new SimpleDateFormat("H:m").format(mParentEvent.getStartTime());
            String parentEventEndHour = new SimpleDateFormat("H:m").format(mParentEvent.getEndTime());
            Date parentStartTimeDate = formatter.parse(parentEventStartHour);
            Date parentEndTimeDate = formatter.parse(parentEventEndHour);
            if (parentStartTimeDate.compareTo(startTimeDate)>0 || parentEndTimeDate.compareTo(endTimeDate)<0){
                Toast.makeText(getContext(), "Start time and end time must be between parent's time range!",Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e){
                e.printStackTrace();
        }

        return true;
    }

    private void initCreateEvent(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance().getInstance();
        String firebaseUserUID = mAuth.getCurrentUser().getUid();

        mDatabase.getReference("users/" + firebaseUserUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    createEvent(user);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,OverviewFragment.newInstance()).addToBackStack(null).commit();
                    Toast.makeText(getContext(), "Event successfully created", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void createEvent(User user){

        String title = etTitle.getText().toString();
        String location = etLocation.getText().toString();
        String description = etDescription.getText().toString();
        String startTime = tvStartTime.getText().toString();
        String endTime = tvEndTime.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("employeeEvents").child(mParentEvent.getId());
        String key = ref.push().getKey();

        Date startTimeDate = null;
        Date endTimeDate = null;
        try {
            startTimeDate = new SimpleDateFormat("H:m").parse(startTime);
            endTimeDate = new SimpleDateFormat("H:m").parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        EmployeeEvent event = new EmployeeEvent(key, title, location, description, user.getName(),user.getFirebaseUserUID(), startTimeDate, endTimeDate);

        ref.child(key).setValue(event);
//        Map<String, Object> eventValues = event.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/" + key, eventValues);
//
//        ref.updateChildren(childUpdates);
    }
}
