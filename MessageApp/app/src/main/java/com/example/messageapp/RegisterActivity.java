package com.example.messageapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messageapp.firebase.FirebaseService;
import com.example.messageapp.util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText nume, prenume, telefon, email, parola;
    private Button btnRegister;
    private List<User> users;
    private FirebaseService firebaseService;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();

        firebaseService = FirebaseService.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String id = null;
                    String Nume = nume.getText().toString();
                    String Prenume = prenume.getText().toString();
                    String Email = email.getText().toString();
                    String Telefon = telefon.getText().toString();
                    String Parola = parola.getText().toString();

                    User user = new User(null, Nume, Prenume, Email, Telefon, Parola);
                    firebaseService.upsert(user);
                    mAuth.createUserWithEmailAndPassword(Email, Parola);

                    Toast.makeText(RegisterActivity.this, "Welcome dear user!", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(RegisterActivity.this, "Ups... Looks like there is a problem... Pay more attention please", Toast.LENGTH_SHORT).show();
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
            nume.setError("Completati numele");
            return false;
        }
        if(prenume == null || prenume.getText().toString().trim().isEmpty()) {
            prenume.setError("Completati prenumele");
            return false;
        }
        if(telefon == null || telefon.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(telefon.getText()).matches()) {
            telefon.setError("Completati numarul de telefon");
            return false;
        }
        if(email == null || email.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            email.setError("Completati cu un email valid");
            return false;
        }
        if(parola == null || parola.getText().toString().trim().isEmpty() || parola.getText().toString().trim().length()<8) {
            parola.setError("Parola trebuie sa contina cel putin 8 caractere");
            return false;
        }

        return true;
    }

}