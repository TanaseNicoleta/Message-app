package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.example.messageapp.adapters.ViewPagerAdapter;
import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.database.service.ContactService;
import com.example.messageapp.database.service.CreditService;
import com.example.messageapp.fragments.ClientsFragment;
import com.example.messageapp.fragments.SarbatoritiFragment;
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

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
    }

    private void initComponents(){
        toolbar=findViewById(R.id.toolbar);
        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.raports_view_pager);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(SarbatoritiFragment.newInstance((ArrayList<Contact>) contacts), getString(R.string.tab_title_sarbatoriti));
        viewPagerAdapter.addFragment(ClientsFragment.newInstance((ArrayList<Contact>) contacteCuCredite), getString(R.string.tab_title_indatorati));
        viewPager.setAdapter(viewPagerAdapter);
    }

//    private Callback<Map<String,Integer>> getCreditsAndNumberofThem() {
//        return new Callback<Map<String,Integer>>() {
//            @Override
//            public void runResultOnUiThread(Map<String,Integer> result) {
//                if (result != null) {
//                    credits.putAll(result);
//                    Log.i("HASHMAP", credits.toString());
//                }
//            }
//        };
//    }

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