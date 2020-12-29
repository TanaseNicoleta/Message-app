package com.example.messageapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.messageapp.MainActivity;
import com.example.messageapp.PieChartActivity;
import com.example.messageapp.R;
import com.example.messageapp.adapters.ContactAdapter;
import com.example.messageapp.database.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ClientsFragment extends Fragment {

    private static String CLIENTS_KEY = "clients key";
    private ListView lvContacte;
    private List<Contact> contacteCuCredite=new ArrayList<>();
    private ImageView ivPieChart;

    public ClientsFragment() {
        // Required empty public constructor
    }

    public static ClientsFragment newInstance(ArrayList<Contact> contacteCuCredite) {
        ClientsFragment fragment = new ClientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ClientsFragment.CLIENTS_KEY, contacteCuCredite);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_clients, container, false);
        initComponents(view);
        return view;
    }
    private void initComponents(View view) {
        ivPieChart=view.findViewById(R.id.iv_piechart);
        lvContacte = view.findViewById(R.id.lv_raports_indatorati);
        if (getArguments() != null) {
            contacteCuCredite = getArguments().getParcelableArrayList(CLIENTS_KEY);
        }
        if (getContext() != null) {
            addContactAdapter();
        }
        ivPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), PieChartActivity.class);
                startActivity(intent);
            }
        });
    }
    private void addContactAdapter() {
        ContactAdapter adapter=new ContactAdapter(getContext().getApplicationContext(),R.layout.lv_contact,contacteCuCredite,getLayoutInflater());
        lvContacte.setAdapter(adapter);
    }
    public void notifyInternalAdapterClients() {
        ArrayAdapter adapter = (ArrayAdapter) lvContacte.getAdapter();
        adapter.notifyDataSetChanged();
    }
}