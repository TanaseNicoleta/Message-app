package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.messageapp.util.Bank;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public static final String BANK_LIST = "Bank list";

    private BottomNavigationView bottomNavigationView;
    private List<Bank> bankList=new ArrayList<>();
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        intent=getIntent();
        intent=getIntent();
        bankList= (List<Bank>) intent.getSerializableExtra(BANK_LIST);
        initComponents();
    }
    private void initComponents() {
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