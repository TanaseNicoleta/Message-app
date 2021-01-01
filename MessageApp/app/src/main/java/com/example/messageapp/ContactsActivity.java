package com.example.messageapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.messageapp.adapters.ContactAdapter;
import com.example.messageapp.adapters.ContactProfileAdapter;
import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.database.service.ContactService;
import com.example.messageapp.util.Bank;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    public static final String CONTACT_PROFILE_KEY = "Contact profile";
    private ListView lvContacts;
    private List<Contact>contacts=new ArrayList<>();
    private ContactService contactService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        contactService = new ContactService(getApplicationContext());
        contactService.getAllContacts(getAllContactsFromDbCallback());
        initComponents();
    }

    private void initComponents(){
        lvContacts=findViewById(R.id.lv_contacte);
        addLvContacts();
        lvContacts.setOnItemClickListener(openContactProfileActivityClick());
        lvContacts.setOnItemLongClickListener(DeleteContactItemClick());
    }

    private AdapterView.OnItemLongClickListener DeleteContactItemClick() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                contactService.delete(deleteContactDbCallback(position), contacts.get(position));
                return true;
            }
        };
    }

    private Callback<Integer> deleteContactDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    contacts.remove(position);
                    notifyAdapter();
                }
            }
        };
    }

    private AdapterView.OnItemClickListener openContactProfileActivityClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ContactProfileActivity.class);
                intent.putExtra(CONTACT_PROFILE_KEY, contacts.get(position));
                startActivity(intent);

            }
        };
    }
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private Callback<List<Contact>> getAllContactsFromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contacts.addAll(result);
                    notifyAdapter();
                }
            }
        };
    }

    private void addLvContacts() {
        ContactAdapter adapter=new ContactAdapter(getApplicationContext(),R.layout.lv_contact,contacts,getLayoutInflater());
        lvContacts.setAdapter(adapter);
    }

    public void notifyAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvContacts.getAdapter();
        adapter.notifyDataSetChanged();
    }

}