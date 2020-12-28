package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messageapp.dialogs.EditBankDialog;
import com.example.messageapp.dialogs.UpdateUserProfileDialog;
import com.example.messageapp.util.Bank;
import com.example.messageapp.util.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public static final String BANK_LIST = "Bank list";
    private TextView tvNume, tvPrenume, tvEmail, tvTelefon;
    private Button logOut, updateUser;
    private BottomNavigationView bottomNavigationView;
    private List<Bank> bankList=new ArrayList<>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        intent=getIntent();
        bankList= (List<Bank>) intent.getSerializableExtra(BANK_LIST);
        initComponents();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        final String userId = user.getUid();

        database.child(userId).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                   User profile = snapshot.getValue(User.class);
                         if(profile != null) {
                             tvNume.setText(profile.getNume());
                             tvPrenume.setText(profile.getPrenume());
                             tvEmail.setText(profile.getEmail());
                             tvTelefon.setText(profile.getTelefon());
                         }
                 }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, R.string.msj_error, Toast.LENGTH_SHORT).show();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
            }
        });

        updateUser.setOnClickListener(openUpdateProfileDialog());
    }

    private AdapterView.OnClickListener openUpdateProfileDialog() {
        AdapterView.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserProfileDialog updateProfileDialog = new UpdateUserProfileDialog();
                updateProfileDialog.show(getSupportFragmentManager(), String.valueOf(R.id.update_profile));
            }
        };
        return onClickListener;
    }


    private void initComponents() {
        tvNume = findViewById(R.id.display_nume);
        tvPrenume = findViewById(R.id.display_prenume);
        tvEmail = findViewById(R.id.display_email);
        tvTelefon = findViewById(R.id.display_telefon);
        logOut = findViewById(R.id.log_out);
        updateUser = findViewById(R.id.update_profile);

        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_view_profil);
        bottomNavigationView.setOnNavigationItemSelectedListener(addBottomNavView());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener addBottomNavView() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.bottom_nav_view_acasa){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                else if(item.getItemId()==R.id.bottom_nav_view_detalii_banca){
                    Intent intent=new Intent(getApplicationContext(), BankActivity.class);
                    intent.putExtra(BANK_LIST,(Serializable)bankList);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                }
                else if(item.getItemId()==R.id.bottom_nav_view_profil){
                    Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.putExtra(BANK_LIST,(Serializable)bankList);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        };
    }
}