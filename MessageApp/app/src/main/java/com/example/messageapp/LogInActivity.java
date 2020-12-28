package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private EditText etEmail, etParola;
    private Button btnLogIn;
    private FirebaseAuth mAuth;

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
                    String email = etEmail.getText().toString().trim();
                    String parola = etParola.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(email, parola)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.i("Mesaj", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d("Mesaj", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LogInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void initComponents() {
        etEmail = findViewById(R.id.et_email);
        etParola = findViewById(R.id.et_parola);
        btnLogIn = findViewById(R.id.log_in);
    }

    private boolean validate() {
        String email = etEmail.getText().toString().trim();
        String parola = etParola.getText().toString().trim();

        if(email == null || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Completati cu un email valid");
            return false;
        }
        if(parola == null || parola.isEmpty() || parola.length()<8) {
            etParola.setError("Parola trebuie sa contina cel putin 8 caractere");
            return false;
        }

        return true;
    }
}