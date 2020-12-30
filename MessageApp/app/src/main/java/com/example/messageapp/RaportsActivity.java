package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.example.messageapp.adapters.ViewPagerAdapter;
import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.database.service.ContactService;
import com.example.messageapp.database.service.CreditService;
import com.example.messageapp.fragments.AnimationFragment;
import com.example.messageapp.fragments.ClientsFragment;
import com.example.messageapp.fragments.SarbatoritiFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

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
    private Map<String,Integer>mapaCredite;
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
        creditService.getAllCredits(getAllCreditsFromDbCallback());
        mapaCredite=getMapaCredite(credits);
        initComponents();
        openDefaultFragment(savedInstanceState);
    }

    private void initComponents(){
        btnSarbatoriti=findViewById(R.id.btn_raports_sarbatoriti);
        btnIndatorati=findViewById(R.id.btn_raports_indatorati);
        btnSarbatoriti.setOnClickListener(openSarbatoritiFragment());
        btnIndatorati.setOnClickListener(openClientsFragment());

        btnSarbatoriti.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btnSarbatoriti.setBackgroundColor(getResources().getColor(R.color.startColor2));
                    btnIndatorati.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                return false;
            }
        });

        btnIndatorati.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btnSarbatoriti.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    btnIndatorati.setBackgroundColor(getResources().getColor(R.color.startColor2));
                }
                return false;
            }
        });

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

    private Map<String,Integer>getMapaCredite(List<Credit> credits){
        if (credits == null || credits.isEmpty()) {
            return null;
        }
        Map<String, Integer> source = new HashMap<>();
        for (Credit credit : credits) {
            if (source.containsKey(credit.getDenumireCredit())) {
                Integer currentValue = source.get(credit.getDenumireCredit());
                Integer newValue = (currentValue != null ? currentValue : 0) + 1;
                source.put(credit.getDenumireCredit(), newValue);
            } else {
                source.put(credit.getDenumireCredit(), 1);
            }
        }
        return source;
    }
}