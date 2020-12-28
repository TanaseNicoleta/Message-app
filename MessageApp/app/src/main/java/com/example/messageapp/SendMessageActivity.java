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
import android.widget.TextView;
import android.widget.Toast;

import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.util.DateConverter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class SendMessageActivity extends AppCompatActivity {

    public static final String SHARED_PREF_MESSAGE = "messagesSharedPred";
    public static final String MESAJ = "mesaj";
    public static final String MESAJANTERIOR = "mesaj anterior";

    public static final String CONTACT_KEY_SEND_MESSAGE = "contact key send message";
    public static final String CREDIT_EDITAT_SEND_MESSAJE = "credit editat send messaje";
    public static final String CREDIT_STERS_SEND_MESSAGE = "credit sters send message";
    public static final String SUMA_CONT_FINALA_KEY = "suma cont finala key";

    private Contact contact;
    private Credit creditEditat;
    private Credit creditSters;
    private Float sumaContFinal;

    private CardView mesajAniversar;
    private CardView updateSumaCont;
    private CardView updateDobanda;
    private CardView terminareCredit;
    private TextView etMesaj;
    private FloatingActionButton fabTrimite;
    private Intent intent;
    private String mesaj;

    private SharedPreferences preferences;
    private SharedPreferences preferencesMesajAnterior; //aici salvez mesajul anterior trimis unui contact



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        intent=getIntent();
        contact= (Contact) intent.getParcelableExtra(CONTACT_KEY_SEND_MESSAGE);
        creditEditat= (Credit) intent.getParcelableExtra(CREDIT_EDITAT_SEND_MESSAJE);
        creditSters= (Credit) intent.getParcelableExtra(CREDIT_STERS_SEND_MESSAGE);
        sumaContFinal=intent.getFloatExtra(SUMA_CONT_FINALA_KEY, -1);
        initComponents();
    }

    private  void initComponents(){
        mesajAniversar=findViewById(R.id.cv_send_lma);
        updateSumaCont=findViewById(R.id.cv_update_suma_cont);
        updateDobanda=findViewById(R.id.cv_update_dobanda);
        terminareCredit=findViewById(R.id.cv_terminare_credit);
        etMesaj=findViewById(R.id.et_mesaj);
        fabTrimite=findViewById(R.id.fab_trimite);

        preferences=getSharedPreferences(SHARED_PREF_MESSAGE, MODE_PRIVATE);

        String numeFisier=String.valueOf(contact.getId());
        preferencesMesajAnterior=getSharedPreferences(numeFisier,MODE_PRIVATE);

        mesajAniversar.setOnClickListener(populateEditTextLMA());
        updateSumaCont.setOnClickListener(populateTvUpdateSumaCont());
        updateDobanda.setOnClickListener(populateTvUpdateDobanda());
        terminareCredit.setOnClickListener(populateTvTerminareCredit());
        fabTrimite.setOnClickListener(SendSMSEvent());
    }

    private View.OnClickListener populateTvTerminareCredit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(creditSters!=null){
                    mesaj=getString(R.string.mesajTerminareCredit, contact.getPrenume(), creditSters.getDenumireCredit(),
                            creditSters.getSumaImprumutata());
                    etMesaj.setText(mesaj);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.credit_nesters_error, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private View.OnClickListener populateTvUpdateDobanda() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(creditEditat!=null){
                    mesaj=getString(R.string.mesajUpdateDobanda, contact.getPrenume(), creditEditat.getDenumireCredit(),
                            creditEditat.getDobanda());
                    etMesaj.setText(mesaj);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.credit_needitat_error, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private View.OnClickListener populateTvUpdateSumaCont() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sumaContFinal>-1){
                    mesaj=getString(R.string.mesajUpdateSumaCont, contact.getPrenume(), contact.getSumaCont());
                    etMesaj.setText(mesaj);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.suma_cont_needitata_error,Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private View.OnClickListener SendSMSEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                        sendSMS();
                        //aici salvez in fisierul de preferinte
                        saveSMSToSharedPreferences(); //aici salvez toate mesajele trimise
                        saveMessageOfContact(); //aici salvez ultimul mesaj trimis pt contact
                    }else{
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                    }
                }
            }
        };
    }

    private void sendSMS() {
        String phoneNo=contact.getTelefon().trim();
        try{
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null,mesaj,null,null);
            Toast.makeText(getApplicationContext(),"Message is sent",Toast.LENGTH_LONG).show();
            Log.i("NR DE TEL", phoneNo);

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Failed to send message",Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener populateEditTextLMA() {
        //sa verific daca e ziua lui
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contact!=null){
                    mesaj=getString(R.string.mesajLMA, contact.getPrenume());
                    etMesaj.setText(mesaj);
                }
            }
        };
    }

    private void saveSMSToSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        String mesajAnterior=preferences.getString(MESAJ,"");
        String mesajDeScris=mesajAnterior+getString(R.string.separator_de_mesaje)+mesaj;
        editor.putString(MESAJ,mesajDeScris);
        editor.apply();
    }

    private void saveMessageOfContact(){
        SharedPreferences.Editor editor = preferencesMesajAnterior.edit();
        String mesajScris=mesaj;
        editor.putString(MESAJANTERIOR,mesajScris);
        editor.apply();
    }
}