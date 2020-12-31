package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amitshekhar.utils.Constants;
import com.example.messageapp.adapters.BankAdapter;
import com.example.messageapp.dialogs.EditBankDialog;
import com.example.messageapp.util.Bank;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BankActivity extends AppCompatActivity implements EditBankDialog.EditBankDialogListener {
    public static final String BANK_LIST = "Bank list";
    public static final String UPDATED_BANKS = "Updated banks";
    public static final String BANK_PREF = "BankPref";
    private BottomNavigationView bottomNavigationView;
    private ListView lvBank;
    private List<Bank>bankList=new ArrayList<>();
    private Intent intent;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        intent=getIntent();
        populateBankListFromPref();
        initComponents();
    }

    private void initComponents() {
        lvBank=findViewById(R.id.lv_bank_detail);
        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_view_detalii_banca);
        bottomNavigationView.setOnNavigationItemSelectedListener(addBottomNavView());
        BankAdapter adapter = new BankAdapter(getApplicationContext(), R.layout.lv_bank, bankList, getLayoutInflater());
        lvBank.setAdapter(adapter);
        lvBank.setOnItemClickListener(openEditBankDialog());

    }

    private void notifyAdapter() {
        ArrayAdapter adapter = (BankAdapter) lvBank.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener openEditBankDialog() {
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bank clickedBanca = (Bank) adapterView.getItemAtPosition(i);
                EditBankDialog editBankDialog = new EditBankDialog();
                editBankDialog.show(getSupportFragmentManager(), getString(R.string.dialog_edit_commission));
                preferences = getSharedPreferences(UPDATED_BANKS, MODE_PRIVATE);
                SharedPreferences.Editor e = preferences.edit();
                e.clear();
                e.putString(String.valueOf(i), clickedBanca.toString());
                e.commit();

            }
        };
        return onItemClickListener;

    }

    private void populateBankListFromPref() {
        preferences = getSharedPreferences(BANK_PREF, Context.MODE_PRIVATE);
        Map<String, ?> entries = preferences.getAll();
        if(entries.isEmpty()) {
            bankList= (List<Bank>) intent.getSerializableExtra(BANK_LIST);
            saveToPref(bankList);
        }
        else {
            for (Map.Entry<String, ?> entry : entries.entrySet()) {
                String dateBanca = entry.getValue().toString();
                String[] datas = dateBanca.split(",");

                Bank bank = new Bank(datas[0], datas[1], Float.parseFloat(datas[2]));
                bankList.add(bank);
            }
        }
    }

    private void saveToPref(List<Bank> list) {
        SharedPreferences preferences = getSharedPreferences(BANK_PREF, MODE_PRIVATE);
        SharedPreferences.Editor e = preferences.edit();
        Iterator<Bank> i = bankList.iterator();
        int nr = 0;
        while(i.hasNext()) {
            e.putString(String.valueOf(nr), i.next().toString());
            e.commit();
            nr++;
        }

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


    @Override
    public void sendBank(Bank bank) {
        Map<String, ?> bankEntries = preferences.getAll();
        SharedPreferences.Editor editor = preferences.edit();

        for (Map.Entry<String, ?> bankEntry : bankEntries.entrySet()) {
            String dateBanca = bankEntry.getValue().toString();
            String[] datas = dateBanca.split(",");
            if (datas[0].equals(bank.getDenumireBanca())) {
                datas[2] = String.valueOf(bank.getComision());

                StringBuilder sb = new StringBuilder();
                for (String data : datas) {
                    sb.append(data);
                    sb.append(",");
                }
                String str = sb.toString();
                str = str.substring(0, str.length() - 1);
                editor.putString(bankEntry.getKey(), str);
                editor.commit();

            }

        }

        for(Bank b : bankList) {
            if(b.getDenumireBanca().equals(bank.getDenumireBanca())) {
                b.setComision(bank.getComision());
            }
        }

        notifyAdapter();
    }
}