package com.example.messageapp;

import androidx.annotation.NonNull;
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
import com.example.messageapp.util.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class SendMessageActivity extends AppCompatActivity {

    public static final String SHARED_PREF_MESSAGE = "messagesSharedPred";
    public static final String MESAJ = "mesaj";
    public static final String MESAJANTERIOR = "mesaj anterior";

    public static final String CONTACT_KEY_SEND_MESSAGE = "contact key send message";
    public static final String CREDIT_EDITAT_SEND_MESSAJE = "credit editat send messaje";
    public static final String CREDIT_STERS_SEND_MESSAGE = "credit sters send message";
    public static final String SUMA_CONT_FINALA_KEY = "suma cont finala key";
    public static final String DATA_TRIMITERII = "Data trimiterii";
    public static final String USER_CARE_A_TRIMIS_MESAJUL = "User care a trimis mesajul";
    public static final String CREDIT_NOU_INSERAT_KEY = "Credit nou inserat key";

    private Contact contact;
    private Credit creditEditat;
    private Credit creditSters;
    private Credit creditNou;
    private Float sumaContFinal;
    private String numeUser;
    private String prenumeUser;

    private CardView mesajCreditNou;
    private CardView updateSumaCont;
    private CardView updateDobanda;
    private CardView terminareCredit;
    private TextView etMesaj;
    private FloatingActionButton fabTrimite;
    private Intent intent;
    private String mesaj;

    private SharedPreferences preferences; //aici salvam toate mesajele trimise
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
        creditNou=(Credit)intent.getParcelableExtra(CREDIT_NOU_INSERAT_KEY);
        initComponents();
        getUserName();
    }

    private  void initComponents(){
        mesajCreditNou=findViewById(R.id.cv_send_credit_nou);
        updateSumaCont=findViewById(R.id.cv_update_suma_cont);
        updateDobanda=findViewById(R.id.cv_update_dobanda);
        terminareCredit=findViewById(R.id.cv_terminare_credit);
        etMesaj=findViewById(R.id.et_mesaj);
        fabTrimite=findViewById(R.id.fab_trimite);
        preferences=getSharedPreferences(SHARED_PREF_MESSAGE, MODE_PRIVATE);
        String numeFisier=String.valueOf(contact.getId());
        preferencesMesajAnterior=getSharedPreferences(numeFisier,MODE_PRIVATE);
        mesajCreditNou.setOnClickListener(populateEditTextCreditNou());
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
            Toast.makeText(getApplicationContext(), R.string.trimis_cu_succes,Toast.LENGTH_LONG).show();

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), R.string.failed_to_send,Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener populateEditTextCreditNou() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(creditNou!=null){
                    mesaj=getString(R.string.mesajInserareCreditNou, contact.getPrenume(), creditNou.getDenumireCredit(),
                            creditNou.getSumaImprumutata());
                    etMesaj.setText(mesaj);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.neinserare_credit, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void saveSMSToSharedPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        String mesajAnterior=preferences.getString(MESAJ,"");
        String mesajDeScris;
        if(mesajAnterior.isEmpty()){
            mesajDeScris=mesajAnterior+mesaj;
        }else{
            mesajDeScris=mesajAnterior+getString(R.string.separator_de_mesaje)+mesaj;
        }
        editor.putString(MESAJ,mesajDeScris);
        editor.apply();
    }

    private void saveMessageOfContact(){
        SharedPreferences.Editor editor = preferencesMesajAnterior.edit();
        String mesajScris=mesaj;
        Date date=new Date();
        String data=DateConverter.fromDate(date);
        editor.putString(MESAJANTERIOR,mesajScris);
        editor.putString(DATA_TRIMITERII,data);
        if(numeUser!=null && prenumeUser !=null){
            editor.putString(USER_CARE_A_TRIMIS_MESAJUL,getString(R.string.numeUser, numeUser,prenumeUser));
        }else{
            editor.putString(USER_CARE_A_TRIMIS_MESAJUL,getString(R.string.no_user));
        }
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
}