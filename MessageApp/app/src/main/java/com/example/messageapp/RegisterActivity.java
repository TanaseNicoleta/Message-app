package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messageapp.util.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText nume, prenume, telefon, email, parola;
    private Button btnRegister;
    private List<User> users;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String id = null;
                    final String Nume = nume.getText().toString().trim();
                    final String Prenume = prenume.getText().toString().trim();
                    final String Email = email.getText().toString().trim();
                    final String Telefon = telefon.getText().toString().trim();
                    final String Parola = parola.getText().toString().trim();


                    mAuth.createUserWithEmailAndPassword(Email, Parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                User user = new User(null, Nume, Prenume, Email, Telefon, Parola);
                                database.child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, R.string.mesaj_welcome, Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else
                                            Toast.makeText(RegisterActivity.this, R.string.msj_error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });


                } else
                    Toast.makeText(RegisterActivity.this, R.string.msj_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initComponents() {
        nume = findViewById(R.id.et_nume);
        prenume = findViewById(R.id.et_prenume);
        telefon = findViewById(R.id.et_telefon);
        email = findViewById(R.id.et_email);
        parola = findViewById(R.id.et_parola);

        btnRegister = findViewById(R.id.register);
    }

    private boolean validate() {
        if(nume == null || nume.getText().toString().trim().isEmpty()) {
            nume.setError(getString(R.string.err_name));
            return false;
        }
        if(prenume == null || prenume.getText().toString().trim().isEmpty()) {
            prenume.setError(getString(R.string.err_prenume));
            return false;
        }
        if(telefon == null || telefon.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(telefon.getText()).matches()) {
            telefon.setError(getString(R.string.err_telefon));
            return false;
        }
        if(email == null || email.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            email.setError(getString(R.string.err_email));
            return false;
        }
        if(parola == null || parola.getText().toString().trim().isEmpty() || parola.getText().toString().trim().length()<8) {
            parola.setError(getString(R.string.err_parola));
            return false;
        }

        return true;
    }

}