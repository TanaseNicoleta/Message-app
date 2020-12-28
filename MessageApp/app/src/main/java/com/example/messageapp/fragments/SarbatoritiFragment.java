package com.example.messageapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.messageapp.R;
import com.example.messageapp.adapters.ContactAdapter;
import com.example.messageapp.database.model.Contact;

import java.util.ArrayList;
import java.util.List;


public class SarbatoritiFragment extends Fragment {

    private static String KEY = "key";
    private ListView lvContacte;
    private List<Contact> contacts=new ArrayList<>();

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
        return view;
    }
    private void initComponents(View view) {
        lvContacte = view.findViewById(R.id.lv_raports_sarbatoriti);
        if (getArguments() != null) {
            contacts = getArguments().getParcelableArrayList(KEY);
        }
        if (getContext() != null) {
            addContactAdapter();
        }
    }
    private void addContactAdapter() {
        ContactAdapter adapter=new ContactAdapter(getContext().getApplicationContext(),R.layout.lv_contact,contacts,getLayoutInflater());
        lvContacte.setAdapter(adapter);
    }

}