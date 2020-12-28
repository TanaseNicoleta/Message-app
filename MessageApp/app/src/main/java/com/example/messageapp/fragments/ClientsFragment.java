package com.example.messageapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.messageapp.R;
import com.example.messageapp.adapters.ContactAdapter;
import com.example.messageapp.database.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ClientsFragment extends Fragment {

    private static String CLIENTS_KEY = "clients key";
    private ListView lvIndatorati;
    private List<Contact> contacts=new ArrayList<>();

    public ClientsFragment() {
        // Required empty public constructor
    }

    public static ClientsFragment newInstance(ArrayList<Contact> contacts) {
        ClientsFragment fragment = new ClientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ClientsFragment.CLIENTS_KEY, contacts);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_clients, container, false);
        initComponents(view);
        return view;
    }
    private void initComponents(View view) {
        lvIndatorati = view.findViewById(R.id.lv_raports_indatorati);
        if (getArguments() != null) {
            contacts = getArguments().getParcelableArrayList(CLIENTS_KEY);
        }
        if (getContext() != null) {
            addContactAdapter();
        }
    }
    private void addContactAdapter() {
        ContactAdapter adapter=new ContactAdapter(getContext().getApplicationContext(),R.layout.lv_contact,contacts,getLayoutInflater());
        lvIndatorati.setAdapter(adapter);
    }
}