package com.example.messageapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.messageapp.R;
import com.example.messageapp.RadarChartActivity;
import com.example.messageapp.adapters.ContactAdapter;
import com.example.messageapp.database.model.Contact;

import java.util.ArrayList;
import java.util.List;


public class SarbatoritiFragment extends Fragment {

    public static final String SARBATORITI = "Sarbatoriti";
    private static String KEY = "key";
    private ListView lvContacte;
    private List<Contact> contacts=new ArrayList<>();
    private ImageView ivRadar;

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
        ivRadar=view.findViewById(R.id.iv_radar_grapf);
        lvContacte = view.findViewById(R.id.lv_raports_sarbatoriti);
        if (getArguments() != null) {
            contacts = getArguments().getParcelableArrayList(KEY);
        }
        if (getContext() != null) {
            addContactAdapter();
        }
        ivRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), RadarChartActivity.class);
                intent.putParcelableArrayListExtra(SARBATORITI, (ArrayList<? extends Parcelable>) contacts);
                startActivity(intent);
            }
        });
    }
    private void addContactAdapter() {
        ContactAdapter adapter=new ContactAdapter(getContext().getApplicationContext(),R.layout.lv_contact,contacts,getLayoutInflater());
        lvContacte.setAdapter(adapter);
    }
    public void notifyInternalAdapterSarbatoriti() {
        ArrayAdapter adapter = (ArrayAdapter) lvContacte.getAdapter();
        adapter.notifyDataSetChanged();
    }
}