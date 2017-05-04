package com.example.razvan.teambuildingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razvan.teambuildingapp.Entities.User;
import com.example.razvan.teambuildingapp.Utils.DatePickerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CREATE_ACCOUNT_ACTIVITY";
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.spinner_gender)
    public Spinner genderSpinner;
    @BindView(R.id.tv_birthday)
    public TextView birthdayTextView;
    @BindView(R.id.tv_gender)
    public TextView genderTextView;
    @BindView(R.id.pb_register_loading)
    ProgressBar pbLoading;

    @OnClick(R.id.ico_submit)
    public void submitForm(){
        signUp();
    }

    @OnClick(R.id.tv_birthday)
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDateStorage(birthdayTextView);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @OnClick(R.id.tv_gender)
    public void performSpinnerClick() {
        genderSpinner.performClick();
    }

    public String spinner_item;

    @OnItemSelected(R.id.spinner_gender)
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selected;
        selected = genderSpinner.getSelectedItem().toString();
        spinner_item = selected;
        setid(pos);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase firebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = firebaseInstance.getReference("users");
    }

    private void setid(int sp_position) {
        genderSpinner.setSelection(sp_position);
        genderTextView.setText(spinner_item);
    }

    private void signUp(){
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String name = etName.getText().toString();
        final String birthday = birthdayTextView.getText().toString();
        final String gender = genderTextView.getText().toString();

        if(!validateCreateAccountForm(email, password, name, birthday, gender)){
            return;
        }


        pbLoading.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(CreateAccountActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        pbLoading.setVisibility(View.GONE);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateAccountActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            createUser(email, name, birthday, gender);
                            final Intent intent = new Intent(CreateAccountActivity.this, NavigationDrawerActivity.class);
//                            intent.putExtra(NavigationDrawerActivity.KEY_USERNAME, email);
                            startActivity(intent);
                            finish();
                        }

                        // ...
                    }
                });
    }

    private boolean validateCreateAccountForm(String email, String password, String name, String birthday, String gender){

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter your name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(birthday)) {
            Toast.makeText(getApplicationContext(), "Enter your birthday!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(gender) || (!TextUtils.equals(gender, "Male") && !TextUtils.equals(gender, "Female"))){
            Toast.makeText(getApplicationContext(), "Choose a gender!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void createUser(String email, String name, String birthday, String gender){
        String firebaseUserUID = mAuth.getCurrentUser().getUid();

        User user = new User(firebaseUserUID, email, name, birthday, gender, "");

        Map<String, Object> userValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + firebaseUserUID, userValues);

        mFirebaseDatabase.updateChildren(childUpdates);

    }
}
