package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.messageapp.adapters.ContactProfileAdapter;
import com.example.messageapp.adapters.CreditAdapter;
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
        EditCreditDialog.EditCreditDialogListener, CustomDialog.CustomDialogListener{

    public static final String EDIT_CONTACT_KEY = "Edit contact key";
    public static final String CONTACT_PROFILE_KEY = "Contact profile";
    public static final String EDIT_CREDIT_KEY = "Edit credit key";
    public static final String CONTACT_KEY_SEND_MESSAGE = "contact key send message";
    public static final String CREDIT_EDITAT_SEND_MESSAJE = "credit editat send messaje";
    public static final String CREDIT_STERS_SEND_MESSAGE = "credit sters send message";
    public static final String SUMA_CONT_FINALA_KEY = "suma cont finala key";
    public static final String MESAJ_ANTERIOR_CONTACT = "mesaj anterior";

    private ListView lvContact;
    private ListView lvCredits;
    private List<Credit> credits=new ArrayList<>();
    private List<Contact> contacts=new ArrayList<>();
    private CreditService creditService;
    private ContactService contactService;
    private Intent intent;
    private Contact contact;

    private ImageView trimiteMesaj;
    private ImageView mesajAnterior;
    private Button btnAddContact;

    Credit creditEditat;
    Credit creditSters;

    private float dobandaInitiala;
    private float dobandaFinala;
    private float sumaContInitial;
    private float sumaContFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        intent=getIntent();
        contact=intent.getParcelableExtra(CONTACT_PROFILE_KEY);

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
        trimiteMesaj=findViewById(R.id.iv_trimite_mesaj);
        mesajAnterior=findViewById(R.id.iv_mesaje_anterioare);
        btnAddContact=findViewById(R.id.btn_contact_profile_add_credit);
        addLvContact();
        addLvCredits();
        lvContact.setOnItemClickListener(openEditContactDialog());
        lvCredits.setOnItemClickListener(openEditCreditDialog());
        lvCredits.setOnItemLongClickListener(stergeCreditEventListener());
        trimiteMesaj.setOnClickListener(openSendMessageActivity());
        mesajAnterior.setOnClickListener(openPreviousMessageActivity());
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog(){
        CustomDialog dialog = new CustomDialog();
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog));
    }

    @Override
    public void sendCredit(String denumireCredit, float sumaImprumutata, float dobanda, int durataAni) {
        Credit credit=new Credit(denumireCredit,sumaImprumutata,dobanda,durataAni);
        creditService.insert(insertCreditDbCallback(), credit,contacts.get(0));
    }

    private Callback<Credit> insertCreditDbCallback() {
        return new Callback<Credit>() {
            @Override
            public void runResultOnUiThread(Credit result) {
                if (result != null) {
                    credits.add(result);
                    notifyCreditsAdapter();
                }
            }
        };
    }


    private View.OnClickListener openPreviousMessageActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PreviousMessageActivity.class);
                intent.putExtra(MESAJ_ANTERIOR_CONTACT, contact);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener openSendMessageActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SendMessageActivity.class);

                intent.putExtra(CONTACT_KEY_SEND_MESSAGE, contact);

                if(sumaContFinal!=sumaContInitial){
                    intent.putExtra(SUMA_CONT_FINALA_KEY,sumaContFinal);
                }

                if(dobandaFinala!=dobandaInitiala){
                    intent.putExtra(CREDIT_EDITAT_SEND_MESSAJE, creditEditat);
                }
                intent.putExtra(CREDIT_STERS_SEND_MESSAGE, creditSters);
                startActivity(intent);
            }
        };
    }

    private AdapterView.OnItemLongClickListener stergeCreditEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                creditService.delete(deleteCreditDbCallback(position),credits.get(position));
                creditSters=credits.get(position);
                return true;
            }
        };
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
                bundle.putParcelable(EDIT_CREDIT_KEY, credits.get(position));
                editCreditDialog.setArguments(bundle);
                editCreditDialog.show(getSupportFragmentManager(), getString(R.string.dialog));

                dobandaInitiala=credits.get(position).getDobanda();
                creditEditat=credits.get(position);
            }
        };
    }

    private AdapterView.OnItemClickListener openEditContactDialog() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditContactDialog dialog=new EditContactDialog();
                Bundle bundle=new Bundle();
                bundle.putParcelable(EDIT_CONTACT_KEY,contacts.get(position));
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), getString(R.string.dialog));
                sumaContInitial=contacts.get(position).getSumaCont();
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
        CreditAdapter adapter=new CreditAdapter(getApplicationContext(),R.layout.lv_credit,credits,getLayoutInflater());
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
                            sumaContFinal=result.getSumaCont();
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
                            dobandaFinala=result.getDobanda();
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