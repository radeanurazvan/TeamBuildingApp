package com.example.razvan.teambuildingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;

    @OnClick(R.id.tv_forgot_password)
    public void startForgotPasswordActivity(){
        startActivity(new Intent(LogInActivity.this, ForgotPasswordActivity.class));
    }

    @OnClick(R.id.tv_create_account)
    public void startCreateAccountActivity(){
        startActivity(new Intent(LogInActivity.this, CreateAccountActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
    }
}
