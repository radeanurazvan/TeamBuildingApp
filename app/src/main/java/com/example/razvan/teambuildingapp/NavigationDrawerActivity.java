package com.example.razvan.teambuildingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.Entities.User;
import com.example.razvan.teambuildingapp.Fragments.OverviewFragment;
import com.example.razvan.teambuildingapp.Fragments.SettingsFragment;
import com.example.razvan.teambuildingapp.EventDaysRV.EventDaysAdapter;
import com.example.razvan.teambuildingapp.EventDaysRV.SampleData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "NAVIGATION_ACTIVITY";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;

    private EventDaysAdapter mAdapter;

    private User user;

    @BindView(R.id.rv_event_days)
    RecyclerView recyclerViewEventDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mCurrentUser = firebaseAuth.getCurrentUser();
                if (mCurrentUser == null) {
                    // user mAuth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(NavigationDrawerActivity.this, LogInActivity.class));
                    finish();
                }
            }
        };

        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final TextView tvUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        if (mCurrentUser != null) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
            DatabaseReference ref = database.child(mCurrentUser.getUid());

//            Query uidQuery = ref.orderByChild("firebaseuseruid").equalTo(mCurrentUser.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
//                    Log.i(TAG, "User result:"+user);
                    if (user != null) {
                        tvUsername.setText(user.name);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
        }

        ButterKnife.bind(this);
        setUpRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_overview) {
            replaceFragment(OverviewFragment.newInstance("", ""), "Good morning!");
        } else if (id == R.id.nav_settings) {
            replaceFragment(SettingsFragment.newInstance("", ""), "Settings");
        } else if (id == R.id.nav_logout) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpRecyclerView() {
        final List<EventDay> mDataSet = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("eventDays");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventDaySnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the
                    EventDay eventDay = eventDaySnapshot.getValue(EventDay.class);
                    Log.i(TAG, "eventDaySnapshot:"+eventDay);
                    mDataSet.add(eventDay);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        mAdapter = new EventDaysAdapter(mDataSet, this);
        recyclerViewEventDays.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEventDays.setAdapter(mAdapter);
    }

    private void replaceFragment(@NonNull Fragment fragment, @NonNull String title) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commitNow();
        getSupportActionBar().setTitle(title);
    }

    public void signOut() {
        mAuth.signOut();
    }
}
