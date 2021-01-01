package com.example.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amitshekhar.utils.Constants;
import com.example.messageapp.adapters.BankAdapter;
import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.service.ContactService;
import com.example.messageapp.dialogs.EditBankDialog;
import com.example.messageapp.util.Bank;
import com.example.messageapp.util.DateConverter;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BankActivity extends AppCompatActivity implements EditBankDialog.EditBankDialogListener {

    public static final String SHARED_PREF_MESSAGE = "messagesSharedPred";
    public static final String MESAJ = "mesaj";
    public static final String MESAJANTERIOR = "mesaj anterior";;
    public static final String DATA_TRIMITERII = "Data trimiterii";
    public static final String USER_CARE_A_TRIMIS_MESAJUL = "User care a trimis mesajul";

    private SharedPreferences preferencesMesaje; //aici salvez toate mesajele
    private SharedPreferences preferencesMesajAnterior; //aici salvez mesajul anterior trimis unui contact
    String numeUser, prenumeUser;
    List<Contact>contacts=new ArrayList<>();
    ContactService contactService;

    public static final String BANK_LIST = "Bank list";
    public static final String BANK_PREF = "BankPref";
    public static final String UPDATED_BANKS = "Updated banks";
    private BottomNavigationView bottomNavigationView;
    private ListView lvBank;
    private List<Bank>bankList=new ArrayList<>();
    private Intent intent;
    private SharedPreferences clickedPreferences;

    ImageView ivSendSMS, map;
    Bank bancaEditata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        contactService = new ContactService(getApplicationContext());
        contactService.getAllContacts(getAllContactsFromDbCallback());
        intent=getIntent();
        populateBankListFromPref();
        initComponents();
    }

    private void initComponents() {
        ivSendSMS=findViewById(R.id.iv_bank_activity_send_sms);
        lvBank=findViewById(R.id.lv_bank_detail);
        map = findViewById(R.id.iv_bank_map);
        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_view_detalii_banca);
        bottomNavigationView.setOnNavigationItemSelectedListener(addBottomNavView());
        BankAdapter adapter = new BankAdapter(getApplicationContext(), R.layout.lv_bank, bankList, getLayoutInflater());
        lvBank.setAdapter(adapter);
        lvBank.setOnItemClickListener(openEditBankDialog());
        preferencesMesaje=getSharedPreferences(SHARED_PREF_MESSAGE, Context.MODE_PRIVATE);
        getUserName();

        ivSendSMS.setOnClickListener(sendSMSEvent());
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
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
                clickedPreferences = getSharedPreferences(UPDATED_BANKS, MODE_PRIVATE);
                SharedPreferences.Editor e = clickedPreferences.edit();
                e.clear();
                e.putString(String.valueOf(i), clickedBanca.toString());
                e.commit();
            }
        };
        return onItemClickListener;

    }

    private void populateBankListFromPref() {
        clickedPreferences = getSharedPreferences(BANK_PREF, Context.MODE_PRIVATE);
        Map<String, ?> entries = clickedPreferences.getAll();
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

    @Override
    public void sendBank(Bank bank) {
        bancaEditata=bank;
        Map<String, ?> bankEntries = clickedPreferences.getAll();
        SharedPreferences.Editor editor = clickedPreferences.edit();

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
        saveToPref(bankList);
        notifyAdapter();
    }

    private View.OnClickListener sendSMSEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bancaEditata!=null){
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                            if(!contacts.isEmpty()){
                                for(Contact contact:contacts){
                                    sendSMS(contact);
                                }
                            }
                            Toast.makeText(getApplicationContext(),getString(R.string.message_update),Toast.LENGTH_LONG).show();
                        }else{
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.comision_nemodificat,Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private Callback<List<Contact>> getAllContactsFromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contacts.addAll(result);
                    notifyAdapter();
                }
            }
        };
    }
    private void sendSMS(Contact contact) {
        String phoneNo=contact.getTelefon().trim();
        try{
            SmsManager smsManager=SmsManager.getDefault();
            String mesaj=getString(R.string.mesajUpdateComision, contact.getPrenume(), bancaEditata.getDenumireBanca(), bancaEditata.getComision());
            smsManager.sendTextMessage(phoneNo,null,mesaj,null,null);

            //tin evidenta in fisierul de preferinte cu toate mesajele
            saveToPreferencesMesaje(mesaj);

            //salvez in fisierul de preferinte al fiecarui contact pentru a vedea ultimul mesaj trimis din pagina de profil
            saveToPreferencesMesajAnterior(contact, mesaj);

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), R.string.faild_send_message,Toast.LENGTH_LONG).show();
        }
    }

    private void saveToPreferencesMesajAnterior(Contact contact, String mesaj) {
        String numeFisier=String.valueOf(contact.getId());
        preferencesMesajAnterior=getSharedPreferences(numeFisier,MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferencesMesajAnterior.edit();
        String mesajScris=mesaj;
        Date date=new Date();
        String data= DateConverter.fromDate(date);
        editor2.putString(MESAJANTERIOR,mesajScris);
        editor2.putString(DATA_TRIMITERII,data);
        if(numeUser!=null && prenumeUser !=null){
            editor2.putString(USER_CARE_A_TRIMIS_MESAJUL,getString(R.string.numeUser, numeUser,prenumeUser));
        }else{
            editor2.putString(USER_CARE_A_TRIMIS_MESAJUL,getString(R.string.no_user));
        }
        editor2.apply();
    }

    private void saveToPreferencesMesaje(String mesaj) {
        SharedPreferences.Editor editor = preferencesMesaje.edit();
        String mesajAnterior=preferencesMesaje.getString(MESAJ,"");
        String mesajDeScris;
        if(mesajAnterior.isEmpty()){
            mesajDeScris=mesajAnterior+mesaj;
        }else{
            mesajDeScris=mesajAnterior+getString(R.string.separator_de_mesaje)+mesaj;
        }
        editor.putString(MESAJ,mesajDeScris);
        editor.apply();
    }

    public void getUserName() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        final String userId = user.getUid();

        database.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile = snapshot.getValue(User.class);
                if(profile != null) {
                    numeUser=profile.getNume();
                    prenumeUser=profile.getPrenume();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), R.string.msj_error, Toast.LENGTH_SHORT).show();
            }
        });
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