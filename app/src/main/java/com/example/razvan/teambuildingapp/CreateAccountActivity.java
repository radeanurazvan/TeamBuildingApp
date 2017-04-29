package com.example.razvan.teambuildingapp;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.razvan.teambuildingapp.Utils.DatePickerFragment;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class CreateAccountActivity extends AppCompatActivity {

    @BindView(R.id.spinner_gender)
    public Spinner genderSpinner;

    @BindView(R.id.tv_birthday)
    public TextView birthdayTextView;

    @BindView(R.id.tv_gender)
    public TextView genderTextView;

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

    }

    private void setid(int sp_position) {
        genderSpinner.setSelection(sp_position);
        genderTextView.setText(spinner_item);
    }
}
