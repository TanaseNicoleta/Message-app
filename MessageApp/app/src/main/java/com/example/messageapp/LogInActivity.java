package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    public static final String SHARED_PREF_USER = "userSharedPred";
    public static final String SALVAT = "salvat";
    public static final String EMAIL = "email";
    public static final String PAROLA = "parola";

    private EditText etEmail, etParola;
    private TextView tvForgotPass;
    private Button btnLogIn;
    private FirebaseAuth mAuth;
    private Switch switchBtn;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initComponents();
        mAuth = FirebaseAuth.getInstance();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    saveUserToSharePref();
                    String email = etEmail.getText().toString().trim();
                    String parola = etParola.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(email, parola)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        finish();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LogInActivity.this, R.string.auth_failed,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        tvForgotPass = findViewById(R.id.tv_forgot_password);
        etEmail = findViewById(R.id.et_email);
        etParola = findViewById(R.id.et_parola);
        btnLogIn = findViewById(R.id.log_in);
        switchBtn=findViewById(R.id.switch_remember_me);
        preferences=getSharedPreferences(SHARED_PREF_USER, MODE_PRIVATE);
        getUserDetailsFromSharedPref();
    }

    private boolean validate() {
        String email = etEmail.getText().toString().trim();
        String parola = etParola.getText().toString().trim();

        if(email == null || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.err_email));
            return false;
        }
        if(parola == null || parola.isEmpty() || parola.length()<8) {
            etParola.setError(getString(R.string.err_parola));
            return false;
        }
        return true;
    }
    private void saveUserToSharePref(){
        SharedPreferences.Editor editor = preferences.edit();
        if(switchBtn.isChecked()){
            editor.putBoolean(SALVAT, true);
            String email=etEmail.getText().toString().trim();
            editor.putString(EMAIL, email);
            String parola=etParola.getText().toString().trim();
            editor.putString(PAROLA,parola);
            editor.apply();
        }else{
            editor.clear().apply();
        }
    }
    private void getUserDetailsFromSharedPref(){
        switchBtn.setChecked(preferences.getBoolean(SALVAT,false));
        etEmail.setText(preferences.getString(EMAIL,""));
        etParola.setText(preferences.getString(PAROLA,""));
    }
}