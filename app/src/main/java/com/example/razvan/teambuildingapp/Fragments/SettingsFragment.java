package com.example.razvan.teambuildingapp.Fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.User;
import com.example.razvan.teambuildingapp.R;
import com.example.razvan.teambuildingapp.Utils.CommonUtilities;
import com.example.razvan.teambuildingapp.Utils.DatePickerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SETTINGS_FRAGMENT";
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.spinner_gender)
    Spinner genderSpinner;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.pb_loading_avatar)
    ProgressBar pbLoadingAvatar;

    @OnClick(R.id.tv_birthday)
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDateStorage(tvBirthday);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.tv_gender)
    public void performSpinnerClick() {
        genderSpinner.performClick();
    }

    @OnClick(R.id.btn_submit)
    public void submitForm() {
        if (isFormValidated()) {
            updateChanges();
            Toast.makeText(getActivity(), "You account was sucessfully edited.", Toast.LENGTH_SHORT).show();
        }
    }

    public String spinner_item;

    @OnItemSelected(R.id.spinner_gender)
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selected;
        selected = genderSpinner.getSelectedItem().toString();
        spinner_item = selected;
        setid(pos);
    }

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        getUserData();
        initSpinner();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getUserData() {
        mDatabase.getReference("users/" + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    setViews(user);
                    CommonUtilities.setAvatar(getContext(), user.getPhotoUrl(),ivAvatar);
                    pbLoadingAvatar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setViews(User user) {
        etName.setText(user.getName());
        etEmail.setText(user.getEmail());
        tvBirthday.setText(user.getBirthday());
        tvGender.setText(user.getGender());
    }

    private void setid(int sp_position) {
        genderSpinner.setSelection(sp_position);
        tvGender.setText(spinner_item);
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private boolean isFormValidated() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String birthday = tvBirthday.getText().toString();
        String gender = tvGender.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Enter your name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter your email!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(birthday)) {
            Toast.makeText(getActivity(), "Enter your birthday!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(getActivity(), "Choose a gender!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateChanges() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String birthday = tvBirthday.getText().toString();
        String gender = tvGender.getText().toString();

        String firebaseUserUid = mAuth.getCurrentUser().getUid();
        User user = new User(firebaseUserUid, email, name, birthday, gender, "");
        DatabaseReference ref = mDatabase.getReference("users").child(firebaseUserUid);

        ref.setValue(user);

        updateFirebaseUser(email);

    }

    private void updateFirebaseUser(String email) {
        String password = etPassword.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });

        if(!TextUtils.isEmpty(password)){
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });
        }
    }
}
