package com.example.messageapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.messageapp.R;
import com.example.messageapp.RadarChartActivity;
import com.example.messageapp.SendMessageActivity;
import com.example.messageapp.adapters.ContactAdapter;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.util.DateConverter;
import com.example.messageapp.util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SarbatoritiFragment extends Fragment {

    public static final String SHARED_PREF_MESSAGE = "messagesSharedPred";
    public static final String SARBATORITI = "Sarbatoriti";
    public static final String MESAJ = "mesaj";
    public static final String MESAJANTERIOR = "mesaj anterior";
    public static final String DATA_TRIMITERII = "Data trimiterii";
    public static final String USER_CARE_A_TRIMIS_MESAJUL = "User care a trimis mesajul";
    private SharedPreferences preferencesMesajAnterior;

    String numeUser;
    String prenumeUser;

    private static String KEY = "key";
    private ListView lvContacte;
    private List<Contact> contacts=new ArrayList<>();
    private ImageView ivRadar;
    private ImageView ivSendSMS;
    private SharedPreferences preferences;

    public SarbatoritiFragment() {
        // Required empty public constructor
    }

    public static SarbatoritiFragment newInstance(ArrayList<Contact> contacts) {
        SarbatoritiFragment fragment = new SarbatoritiFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SarbatoritiFragment.KEY, contacts);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sarbatoriti, container, false);
        initComponents(view);
        getUserName();
        return view;
    }
    private void initComponents(View view) {
        ivRadar=view.findViewById(R.id.iv_radar_grapf);
        ivSendSMS=view.findViewById(R.id.iv_send_messages_to_all_contacts);
        lvContacte = view.findViewById(R.id.lv_raports_sarbatoriti);
        if (getArguments() != null) {
            contacts = getArguments().getParcelableArrayList(KEY);
        }
        if (getContext() != null) {
            addContactAdapter();
        }
        preferences=this.getActivity().getSharedPreferences(SHARED_PREF_MESSAGE, Context.MODE_PRIVATE);
        ivRadar.setOnClickListener(openRadarActivity());
        ivSendSMS.setOnClickListener(trimitereMesajeTuturorSarbatoritilor());

    }

    private View.OnClickListener trimitereMesajeTuturorSarbatoritilor() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                        if(!contacts.isEmpty()){
                            for(Contact contact: contacts){
                                sendSMS(contact);
                            }
                            Toast.makeText(getContext().getApplicationContext(), R.string.informare_trimitere_mesaje,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext().getApplicationContext(), R.string.intrebare,Toast.LENGTH_LONG).show();
                        }
                    }else{
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                    }
                }
            }
        };
    }

    private View.OnClickListener openRadarActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), RadarChartActivity.class);
                intent.putParcelableArrayListExtra(SARBATORITI, (ArrayList<? extends Parcelable>) contacts);
                startActivity(intent);
            }
        };
    }
    private void addContactAdapter() {
        ContactAdapter adapter=new ContactAdapter(getContext().getApplicationContext(),R.layout.lv_contact,contacts,getLayoutInflater());
        lvContacte.setAdapter(adapter);
    }

    private void sendSMS(Contact contact) {
        String phoneNo=contact.getTelefon().trim();
        try{
            SmsManager smsManager=SmsManager.getDefault();
            String mesaj=getString(R.string.mesajLMA, contact.getPrenume());
            smsManager.sendTextMessage(phoneNo,null,mesaj,null,null);
            Toast.makeText(getContext().getApplicationContext(), getString(R.string.toastTrimitereSMS,contact.getPrenume()),Toast.LENGTH_LONG).show();

            //tin evidenta in fisierul de prefetinte cu toate mesajele
            saveSMSToSharePref(mesaj);

            //salvez in fisierul de preferinte al fiecarui contact pentru a vedea ultimul mesaj trimis din pagina de profil
            saveForPreviousSMSActivity(contact, mesaj);

        }catch(Exception e){
            Toast.makeText(getContext().getApplicationContext(), R.string.faild_send_message,Toast.LENGTH_LONG).show();
        }
    }

    private void saveForPreviousSMSActivity(Contact contact, String mesaj) {
        String numeFisier=String.valueOf(contact.getId());
        preferencesMesajAnterior=this.getActivity().getSharedPreferences(numeFisier, Context.MODE_PRIVATE);
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

    private void saveSMSToSharePref(String mesaj) {
        SharedPreferences.Editor editor = preferences.edit();
        String mesajAnterior=preferences.getString(MESAJ,"");
        String mesajDeScris=mesajAnterior+getString(R.string.separator_de_mesaje)+mesaj;
        editor.putString(MESAJ,mesajDeScris);
        editor.apply();
    }

    private void  getUserName() {
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
                Toast.makeText(getContext().getApplicationContext(), R.string.msj_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}