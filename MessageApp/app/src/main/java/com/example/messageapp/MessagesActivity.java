package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messageapp.adapters.MesajAdapter;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.util.Mesaj;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    public static final String SHARED_PREF_MESSAGE = "messagesSharedPred";
    public static final String MESAJ = "mesaj";
    private SharedPreferences preferences;

    ListView lvMesaje;
    private List<Mesaj> mesajeTrimise=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        initComponents();
    }

    private void initComponents(){
        lvMesaje=findViewById(R.id.lv_mesaje_trimise);
        preferences=getSharedPreferences(SHARED_PREF_MESSAGE, MODE_PRIVATE);
        getMessagesFromSharedPreference();
        addLvMesajeAdapter();
    }

    private void addLvMesajeAdapter() {
        MesajAdapter adapter=new MesajAdapter(getApplicationContext(),R.layout.lv_mesaj,mesajeTrimise,getLayoutInflater());
        lvMesaje.setAdapter(adapter);
    }
    private void getMessagesFromSharedPreference() {
        String buffer = preferences.getString(MESAJ, "");
        String[]mesaje=buffer.split(getString(R.string.separator_de_mesaje));
        for(int i=0;i<mesaje.length;i++){
            Mesaj mesaj=new Mesaj(mesaje[i]);
            mesajeTrimise.add(mesaj);
        }
    }
}