package com.example.messageapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
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
import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.service.ContactService;

import java.util.ArrayList;
import java.util.List;

public class ClientsFragment extends Fragment {

    public static final String PESTE_6000 = "peste 6000";
    public static final String SUB_3000 = "sub 3000";
    public static final String INTRE_3000_SI_6000 = "intre 3000 si 6000";
    private static String CLIENTS_KEY = "clients key";
    private ListView lvContacte;
    private List<Contact> contacteCuCreditePeste6000=new ArrayList<>();
    private ImageView ivPieChart;

    //BD
    ContactService contactService;
    List<Contact>contactePeste6000=new ArrayList<>();
    List<Contact>contactePanaIn3000=new ArrayList<>();
    List<Contact>contacteIntre3000si6000=new ArrayList<>();


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
        contactService=new ContactService(getContext().getApplicationContext());
        contactService.getContacteImprumutateCuPeste6000lei(getContacteImprumutateCuPeste6000FromDbCallback());
        contactService.getContacteImprumutateMaiPutinDe3000lei(getContacteImprumutateCuMaiPutin3000FromDbCallback());
        contactService.getContacteImprumutateCuSumaTotalaIntre3000si6000(getContacteImprumutateCuSumaTotalaIntre3000si6000FromDbCallback());


        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_clients, container, false);
        initComponents(view);
        return view;
    }
    private void initComponents(View view) {
        ivPieChart=view.findViewById(R.id.iv_piechart);
        lvContacte = view.findViewById(R.id.lv_raports_indatorati);
        if (getArguments() != null) {
            contacteCuCreditePeste6000 = getArguments().getParcelableArrayList(CLIENTS_KEY);
        }
        if (getContext() != null) {
            addContactAdapter();
        }
        ivPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), PieChartActivity.class);
                intent.putParcelableArrayListExtra(PESTE_6000, (ArrayList<? extends Parcelable>) contactePeste6000);
                intent.putParcelableArrayListExtra(SUB_3000, (ArrayList<? extends Parcelable>) contactePanaIn3000);
                intent.putParcelableArrayListExtra(INTRE_3000_SI_6000, (ArrayList<? extends Parcelable>) contacteIntre3000si6000);
                startActivity(intent);
            }
        });
    }
    private void addContactAdapter() {
        ContactAdapter adapter=new ContactAdapter(getContext().getApplicationContext(),R.layout.lv_contact,contacteCuCreditePeste6000,getLayoutInflater());
        lvContacte.setAdapter(adapter);
    }
    public void notifyInternalAdapterClients() {
        ArrayAdapter adapter = (ArrayAdapter) lvContacte.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Contact>> getContacteImprumutateCuPeste6000FromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contactePeste6000.addAll(result);
                    Log.i("Peste 6000", contactePeste6000.toString());
                    Log.i("SIZE", String.valueOf(contactePeste6000.size()));
                }
            }
        };
    }
    private Callback<List<Contact>> getContacteImprumutateCuMaiPutin3000FromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contactePanaIn3000.addAll(result);
                    Log.i("Sub 3000", contactePanaIn3000.toString());
                }
            }
        };
    }
    private Callback<List<Contact>> getContacteImprumutateCuSumaTotalaIntre3000si6000FromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contacteIntre3000si6000.addAll(result);
                    Log.i("Intre 3000 si 6000", contacteIntre3000si6000.toString());
                }
            }
        };
    }
}