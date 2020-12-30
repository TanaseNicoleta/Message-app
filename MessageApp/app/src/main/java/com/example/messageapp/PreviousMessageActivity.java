package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.messageapp.adapters.MesajAdapter;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.util.Mesaj;

public class PreviousMessageActivity extends AppCompatActivity {
    public static final String MESAJ_ANTERIOR_CONTACT = "mesaj anterior";
    public static final String MESAJANTERIOR = "mesaj anterior";

    public static final String DATA_TRIMITERII = "Data trimiterii";
    public static final String USER_CARE_A_TRIMIS_MESAJUL = "User care a trimis mesajul";

    private String mesaj;
    private String data;
    private String userName;

    private TextView textViewMesaj;
    private TextView textViewReceiver;
    private TextView tvData;
    private TextView tvSender;

    private SharedPreferences preferences;

    private Intent intent;
    private Contact contact;
    private String numeFisier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_message);
        intent=getIntent();
        contact=(Contact) intent.getParcelableExtra(MESAJ_ANTERIOR_CONTACT);
        if(contact!=null){
            numeFisier=String.valueOf(contact.getId());
        }
        initComponents();
    }

    private void initComponents(){
        textViewMesaj=findViewById(R.id.tv_mesaj_anterior);
        textViewReceiver=findViewById(R.id.previous_message_tv_receiver);
        tvData=findViewById(R.id.previous_message_tv_data);
        tvSender=findViewById(R.id.previous_message_tv_sender);
        preferences=getSharedPreferences(numeFisier, MODE_PRIVATE);
        getMessagesFromSharedPreference();
        populateTv(textViewMesaj,mesaj);
        populateTv(textViewReceiver,getString(R.string.nume_contact_ultimul_mesaj,contact.getPrenume(),contact.getNume()));
        populateTv(tvData,data);
        populateTv(tvSender,getString(R.string.numeUserCareATrimisMesajul,userName));
    }

    private void populateTv(TextView textView, String value) {
        if(value!=null && !value.isEmpty()){
            textView.setText(value);
        }else{
            textView.setText(R.string.no_content);
        }
    }
    private void getMessagesFromSharedPreference() {
        mesaj=preferences.getString(MESAJANTERIOR, "");
        data=preferences.getString(DATA_TRIMITERII,"");
        userName=preferences.getString(USER_CARE_A_TRIMIS_MESAJUL,"");
    }
}