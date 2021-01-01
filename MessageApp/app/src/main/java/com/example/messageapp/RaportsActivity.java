package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.database.service.ContactService;
import com.example.messageapp.database.service.CreditService;
import com.example.messageapp.fragments.AnimationFragment;
import com.example.messageapp.fragments.ClientsFragment;
import com.example.messageapp.fragments.SarbatoritiFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaportsActivity extends AppCompatActivity {

    private List<Contact> contacts=new ArrayList<>();
    private List<Contact>contacteCuCredite=new ArrayList<>();
    private List<Credit>credits=new ArrayList<>();
    private ContactService contactService;
    private CreditService creditService;
    private Button btnSarbatoriti;
    private Button btnIndatorati;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raports);
        contactService = new ContactService(getApplicationContext());
        creditService=new CreditService(getApplicationContext());
        contactService.getSarbatoriti(getSarbatoritiFromDbCallback());
        contactService.getContacteImprumutateCuPeste6000lei(getContacteImprumutateFromDbCallback());
        initComponents();
        openDefaultFragment(savedInstanceState);
    }

    private void initComponents(){
        btnSarbatoriti=findViewById(R.id.btn_raports_sarbatoriti);
        btnIndatorati=findViewById(R.id.btn_raports_indatorati);
        btnSarbatoriti.setOnClickListener(openSarbatoritiFragment());
        btnIndatorati.setOnClickListener(openClientsFragment());
        btnSarbatoriti.setOnTouchListener(changeColorBtnSarbatoriti());
        btnIndatorati.setOnTouchListener(changeColorBtnIndatorati());
    }

    private View.OnTouchListener changeColorBtnIndatorati() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btnSarbatoriti.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    btnIndatorati.setBackgroundColor(getResources().getColor(R.color.startColor2));
                }
                return false;
            }
        };
    }

    private View.OnTouchListener changeColorBtnSarbatoriti() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btnSarbatoriti.setBackgroundColor(getResources().getColor(R.color.startColor2));
                    btnIndatorati.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                return false;
            }
        };
    }

    private View.OnClickListener openClientsFragment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFragment = ClientsFragment.newInstance((ArrayList<Contact>) contacteCuCredite);
                openFragment();
            }
        };
    }

    private View.OnClickListener openSarbatoritiFragment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFragment = SarbatoritiFragment.newInstance((ArrayList<Contact>) contacts);
                openFragment();
            }
        };
    }

    private void openFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_container, currentFragment)
                .commit();
    }
    private void openDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment =new AnimationFragment();
            openFragment();
        }
    }

    private Callback<List<Contact>> getSarbatoritiFromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contacts.addAll(result);
                    Log.i("SARBATORITI", contacts.toString());
                }
            }
        };
    }

    private Callback<List<Credit>> getAllCreditsFromDbCallback() {
        return new Callback<List<Credit>>() {
            @Override
            public void runResultOnUiThread(List<Credit> result) {
                if (result != null) {
                    credits.addAll(result);
                    Log.i("Credite", credits.toString());
                }
            }
        };
    }

    private Callback<List<Contact>> getContacteImprumutateFromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contacteCuCredite.addAll(result);
                    Log.i("Peste 6000", contacteCuCredite.toString());
                }
            }
        };
    }

}