package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.messageapp.util.Bank;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankActivity extends AppCompatActivity {
    public static final String BANK_LIST = "Bank list";

    private BottomNavigationView bottomNavigationView;
    private ListView lvBank;
    private List<Bank>bankList=new ArrayList<>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        intent=getIntent();
        bankList= (List<Bank>) intent.getSerializableExtra(BANK_LIST);
        initComponents();
    }
    private void initComponents() {
        lvBank=findViewById(R.id.lv_bank_detail);
        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_view_detalii_banca);
        bottomNavigationView.setOnNavigationItemSelectedListener(addBottomNavView());
        addLvBank();
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
    private void notifyAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvBank.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void addLvBank() {
        ArrayAdapter<Bank> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                bankList);
        lvBank.setAdapter(adapter);
    }
}