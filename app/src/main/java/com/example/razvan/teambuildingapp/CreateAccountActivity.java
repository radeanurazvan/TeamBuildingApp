package com.example.razvan.teambuildingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class CreateAccountActivity extends AppCompatActivity {

    @BindView(R.id.spinner_gender)
    public Spinner genderSpinner;

    @OnClick(R.id.tv_gender)
    public void performSpinnerClick(){
        genderSpinner.performClick();
    }

    @BindView(R.id.tv_gender)
    public TextView genderTextView;

    public String spinner_item;

    @OnItemSelected(R.id.spinner_gender)
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        String selected;
        selected = genderSpinner.getSelectedItem().toString();
        if (!selected.equals("Country"))
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
