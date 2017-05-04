package com.example.razvan.teambuildingapp.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.AttendedEvent;
import com.example.razvan.teambuildingapp.Entities.EmployeeEvent;
import com.example.razvan.teambuildingapp.Entities.Event;
import com.example.razvan.teambuildingapp.Entities.EventAttendant;
import com.example.razvan.teambuildingapp.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by LexGrup on 5/3/2017.
 */

public class EventAttendantsUtils {
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private static String mParentEventKey;

    public static void joinEmployeeEvent(String parentEventKey, EmployeeEvent event, Context context) {
        mParentEventKey = parentEventKey;
        checkUserJoinedEvent(event, context);
    }

    private static void createAttendant(EmployeeEvent event, User user) {
        //add to attendants
        DatabaseReference ref = mDatabase.getReference("attendants/" + event.getId());
        String key = ref.push().getKey();

        EventAttendant attendant = new EventAttendant(key, mAuth.getCurrentUser().getUid(), user.getName(), user.getPhotoUrl());

        Map<String, Object> attendantValues = attendant.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key, attendantValues);
        ref.updateChildren(childUpdates);

        //add to attendedEvents
        String node = "attendedEvents/"+ mAuth.getCurrentUser().getUid() + "/" + event.getId();
        ref = mDatabase.getReference(node);
        AttendedEvent attended = new AttendedEvent(event, key);
        ref.setValue(attended);

        //update event stats
        incrementAttendants(attended);

    }

    private static void checkUserJoinedEvent(final EmployeeEvent event, final Context context) {
        final String firebaseUserUID = mAuth.getCurrentUser().getUid();

        mDatabase.getReference("attendedEvents/" + firebaseUserUID + "/" + event.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AttendedEvent attended = dataSnapshot.getValue(AttendedEvent.class);
                removeOrAddAttendant(attended, event, context);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private static void removeOrAddAttendant(final AttendedEvent attended,final EmployeeEvent eventToAttend, final  Context context) {
        String messageVerb = attended == null ? "join" : "leave";
        String message = "Are you sure you want to " + messageVerb + " " + eventToAttend.getHost() + "'s event?";

        new AlertDialog.Builder(context)
                .setTitle("Join Event")
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (attended != null) {
                            removeAttendantFromEvent(attended, context);
                        } else {
                            addAttendantToEvent(eventToAttend, context);
                        }
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private static void removeAttendantFromEvent(AttendedEvent attended, Context context){
        String firebaseUserUID = mAuth.getCurrentUser().getUid();
        mDatabase.getReference("attendedEvents").child(firebaseUserUID).child(attended.getEvent().getId()).removeValue();


        mDatabase.getReference("attendants").child(attended.getEvent().getId()).child(attended.getAttendantId()).removeValue();

        //update event stats
        decrementAttendants(attended);
        Toast.makeText(context, "Event successfully left", Toast.LENGTH_SHORT).show();
    }

    private static void addAttendantToEvent(final EmployeeEvent event, final Context context) {
        String firebaseUserUID = mAuth.getCurrentUser().getUid();
        mDatabase.getReference("users/" + firebaseUserUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    createAttendant(event, user);
                    Toast.makeText(context, "Event successfully joined", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void decrementAttendants(AttendedEvent attended){
        DatabaseReference ref = mDatabase.getReference("employeeEvents").child(mParentEventKey).child(attended.getEvent().getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EmployeeEvent event = dataSnapshot.getValue(EmployeeEvent.class);
                if(event != null){
                    DatabaseReference ref = mDatabase.getReference("employeeEvents").child(mParentEventKey).child(event.getId()).child("numberOfAttendants");
                    ref.setValue(event.getNumberOfAttendants()-1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private static  void incrementAttendants(AttendedEvent attended){
        DatabaseReference ref = mDatabase.getReference("employeeEvents").child(mParentEventKey).child(attended.getEvent().getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EmployeeEvent event = dataSnapshot.getValue(EmployeeEvent.class);
                if(event != null){
                    DatabaseReference ref = mDatabase.getReference("employeeEvents").child(mParentEventKey).child(event.getId()).child("numberOfAttendants");
                    ref.setValue(event.getNumberOfAttendants()+1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public static void setEventButtonText(EmployeeEvent event,final TextView tv){
        final String firebaseUserUID = mAuth.getCurrentUser().getUid();

        mDatabase.getReference("attendedEvents/" + firebaseUserUID + "/" + event.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AttendedEvent attended = dataSnapshot.getValue(AttendedEvent.class);
                if(attended != null){
                    tv.setText("Leave");
                } else {
                    tv.setText("Join");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
