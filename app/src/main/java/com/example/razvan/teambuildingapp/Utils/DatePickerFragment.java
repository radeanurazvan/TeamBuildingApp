package com.example.razvan.teambuildingapp.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Razvan on 4/17/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private TextView dateStorage;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String date = Integer.toString(day) + "-" + Integer.toString(month) + "-" + Integer.toString(year);
        dateStorage.setText(date);
    }

    public void setDateStorage(TextView _dateStorage){
        dateStorage = _dateStorage;
    }
}
