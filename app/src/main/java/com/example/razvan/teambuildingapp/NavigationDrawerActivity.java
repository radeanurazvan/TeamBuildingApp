package com.example.razvan.teambuildingapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.razvan.teambuildingapp.Entities.EventDay;
import com.example.razvan.teambuildingapp.Entities.User;
import com.example.razvan.teambuildingapp.Fragments.OverviewFragment;
import com.example.razvan.teambuildingapp.Fragments.SettingsFragment;
import com.example.razvan.teambuildingapp.EventDaysRV.EventDaysAdapter;
import com.example.razvan.teambuildingapp.EventDaysRV.SampleData;
import com.example.razvan.teambuildingapp.Utils.CommonUtilities;
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

    private FirebaseDatabase mDatabase;
    private EventDaysAdapter mAdapter;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        initAuth();
        setContentView(R.layout.activity_navigation_drawer);

        initDrawer();
        ButterKnife.bind(this);
        replaceFragment(OverviewFragment.newInstance(), "Good morning!");
        //initRecyclerView();
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
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            if(backStackCount <= 1){
                finish();
            } else {
                super.onBackPressed();
            }
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
            replaceFragment(OverviewFragment.newInstance(), "Good morning!");
        } else if (id == R.id.nav_settings) {
            replaceFragment(SettingsFragment.newInstance(), "Settings");
        } else if (id == R.id.nav_logout) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void replaceFragment(@NonNull Fragment fragment, @NonNull String title) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null).commit();
        getSupportActionBar().setTitle(title);
    }

    public void signOut() {
        mAuth.signOut();
    }

    private void initAuth(){
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
    }

    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setCameraDistance(0);
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initAvatar();

    }


    private void initAvatar(){
        mDatabase.getReference("users/" + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    setAvatar(user.getPhotoUrl());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAvatar(String photoUrl){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ImageView ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        ProgressBar pbLoadingAvatar = (ProgressBar) findViewById(R.id.pb_loading_avatar);
        CommonUtilities.setAvatar(this,photoUrl,ivAvatar);

        pbLoadingAvatar.setVisibility(View.GONE);
    }

}
