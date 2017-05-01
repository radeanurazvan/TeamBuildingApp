package com.example.razvan.teambuildingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {


    private static final String TAG = "LOG_IN_ACTIVITY";

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.pb_login_loading)
    ProgressBar pbLoading;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @OnClick(R.id.btn_log_in)
    public void submitLogInForm() {
        signIn();
    }

    @OnClick(R.id.tv_forgot_password)
    public void startForgotPasswordActivity() {
        startActivity(new Intent(LogInActivity.this, ForgotPasswordActivity.class));
    }

    @OnClick(R.id.tv_create_account)
    public void startCreateAccountActivity() {
        startActivity(new Intent(LogInActivity.this, CreateAccountActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth.getCurrentUser() != null) {
            final Intent intent = new Intent(LogInActivity.this, NavigationDrawerActivity.class);
//            intent.putExtra(NavigationDrawerActivity.KEY_USERNAME, mAuth.getCurrentUser().getEmail());
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void signIn() {
        final String email = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);

        // Authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        pbLoading.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                etPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LogInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(LogInActivity.this, NavigationDrawerActivity.class);
//                            intent.putExtra(ProfileActivity.KEY_USERNAME, email);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

}
