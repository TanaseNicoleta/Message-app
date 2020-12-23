package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.messageapp.adapters.ContactProfileAdapter;
import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.database.service.ContactService;
import com.example.messageapp.database.service.CreditService;
import com.example.messageapp.dialogs.CustomDialog;
import com.example.messageapp.dialogs.EditContactDialog;
import com.example.messageapp.dialogs.EditCreditDialog;

import java.util.ArrayList;
import java.util.List;

public class ContactProfileActivity extends AppCompatActivity implements EditContactDialog.EditContactDialogListener,
EditCreditDialog.EditCreditDialogListener{
    public static final String EDIT_CONTACT_KEY = "Edit contact key";
    public static final String CONTACT_PROFILE_KEY = "Contact profile";
    public static final String EDIT_CREDIT_KEY = "Edit credit key";

    private ListView lvContact;
    private ListView lvCredits;
    private List<Credit> credits=new ArrayList<>();
    private List<Contact> contacts=new ArrayList<>();
    private CreditService creditService;
    private ContactService contactService;
    private Intent intent;
    private Contact contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        intent=getIntent();
        contact=(Contact)intent.getSerializableExtra(CONTACT_PROFILE_KEY);

        contactService=new ContactService(getApplicationContext());
        contactService.getInfoAboutOneContact(getInfoForOneContactDbCallback(),contact);

        creditService = new CreditService(getApplicationContext());
        creditService.getAllCreditsForOneContact(getCreditsForOneContactFromDbCallback(),contact.getId());

        initComponents();
    }

    private Callback<Contact> getInfoForOneContactDbCallback() {
        return new Callback<Contact>() {
            @Override
            public void runResultOnUiThread(Contact result) {
                if (result != null) {
                    contacts.add(result);
                    notifyContactAdapter();
                }
            }
        };
    }

    //fac un get pt CREDITE
    private Callback<List<Credit>> getCreditsForOneContactFromDbCallback() {
        return new Callback<List<Credit>>() {
            @Override
            public void runResultOnUiThread(List<Credit> result) {
                if (result != null) {
                    credits.addAll(result);
                    notifyCreditsAdapter();
                }
            }
        };
    }

    private void initComponents(){
        lvContact=findViewById(R.id.lv_contacts_details);
        lvCredits=findViewById(R.id.lv_credits_details);
        addLvContact();
        addLvCredits();
        lvContact.setOnItemClickListener(openEditContactDialog());
        //la click pe credit se deschide dialogul de editare credit
        lvCredits.setOnItemClickListener(openEditCreditDialog());

        //la long click sterg creditul
        lvCredits.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                creditService.delete(deleteCreditDbCallback(position),credits.get(position));
                return true;
            }
        });
    }

    private Callback<Integer> deleteCreditDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    credits.remove(position);
                    notifyCreditsAdapter();
                }
            }
        };
    }

    private AdapterView.OnItemClickListener openEditCreditDialog() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditCreditDialog editCreditDialog=new EditCreditDialog();
                Bundle bundle=new Bundle();
                bundle.putSerializable(EDIT_CREDIT_KEY, credits.get(position));
                editCreditDialog.setArguments(bundle);
                editCreditDialog.show(getSupportFragmentManager(), getString(R.string.dialog));
            }
        };
    }

    private AdapterView.OnItemClickListener openEditContactDialog() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditContactDialog dialog=new EditContactDialog();
                Bundle bundle=new Bundle();
                bundle.putSerializable(EDIT_CONTACT_KEY,contacts.get(position));
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), getString(R.string.dialog));
            }
        };
    }

    private void addLvContact() {
        ContactProfileAdapter adapter=new ContactProfileAdapter(getApplicationContext(),R.layout.lv_profile,contacts,getLayoutInflater());
        lvContact.setAdapter(adapter);
    }
    public void notifyContactAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvContact.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void addLvCredits() {
        ArrayAdapter<Credit> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                credits);
        lvCredits.setAdapter(adapter);
    }

    public void notifyCreditsAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvCredits.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void sendContact(Contact contact) {
        contactService.update(updateContactToDbCallback(),contact);
    }
    private Callback<Contact> updateContactToDbCallback() {
        return new Callback<Contact>() {
            @Override
            public void runResultOnUiThread(Contact result) {
                if (result != null) {
                    for (Contact contact : contacts) {
                        if (contact.getId() == result.getId()) {
                            contact.setNume(result.getNume());
                            contact.setPrenume(result.getPrenume());
                            contact.setTelefon(result.getTelefon());
                            contact.setDataNasterii(result.getDataNasterii());
                            contact.setSumaCont(result.getSumaCont());
                            break;
                        }
                    }
                    notifyContactAdapter();
                }
            }
        };
    }

    private Callback<Credit> updateCreditToDbCallback() {
        return new Callback<Credit>() {
            @Override
            public void runResultOnUiThread(Credit result) {
                if (result != null) {
                    for (Credit credit : credits) {
                        if (credit.getId() == result.getId()) {
                            credit.setDenumireCredit(result.getDenumireCredit());
                            credit.setSumaImprumutata(result.getSumaImprumutata());
                            credit.setDobanda(result.getDobanda());
                            credit.setDurataAni(result.getDurataAni());
                            break;
                        }
                    }
                    notifyCreditsAdapter();
                }
            }
        };
    }
    @Override
    public void sendCredit(Credit credit) {
        creditService.update(updateCreditToDbCallback(),credit);
    }
}