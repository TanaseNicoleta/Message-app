package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.ContactWithCredits;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.dialogs.CustomDialog;
import com.example.messageapp.util.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddContactActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener {
    public static final String CONTACT_WITH_CREDITS_KEY = "contact with credits key";
    private final DateConverter dateConverter = new DateConverter();


    private Button btnAddCredit;
    private Button btnAddContact;
    private EditText etNume;
    private EditText etPrenume;
    private EditText etTelefon;
    private EditText etDataNasterii;
    private RadioGroup rgGen;
    private EditText etSumaCont;
    private List<Credit>credits=new ArrayList<>();

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        intent=getIntent();
        initComponents();
    }

    private void initComponents(){
        btnAddCredit=findViewById(R.id.btn_add_credit);
        btnAddContact=findViewById(R.id.btn_add_contact);
        etNume=findViewById(R.id.input_nume);
        etPrenume=findViewById(R.id.input_prenume);
        etTelefon=findViewById(R.id.input_telefon);
        etDataNasterii=findViewById(R.id.input_data_nasterii);
        rgGen=findViewById(R.id.rg_adauga_gen);
        etSumaCont=findViewById(R.id.input_suma_cont);

        btnAddCredit.setOnClickListener(openDialogToAddCredit());
        btnAddContact.setOnClickListener(addContactClickEvent());
    }

    private View.OnClickListener addContactClickEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Contact contact = buildContactFromWidgets();
                        ContactWithCredits contactWithCredits=new ContactWithCredits(contact,credits);
                        intent.putExtra(CONTACT_WITH_CREDITS_KEY,contactWithCredits);
                        setResult(RESULT_OK,intent);
                        finish();
                }
            }
        };
    }

    private boolean validate() {
        if (etNume.getText() == null || etNume.getText().toString().trim().length() < 1) {
            Toast.makeText(getApplicationContext(),
                    R.string.invalid_nume_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etPrenume.getText() == null || etPrenume.getText().toString().trim().length() < 1) {
            Toast.makeText(getApplicationContext(),
                    R.string.invalid_prenume_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etTelefon.getText() == null || etTelefon.getText().toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(),
                    R.string.invalid_telefon_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etDataNasterii.getText() == null
                || dateConverter.fromString(etDataNasterii.getText().toString().trim()) == null) {
            Toast.makeText(getApplicationContext(),
                    R.string.invalid_data_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (rgGen.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.invalid_gen_error),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etSumaCont.getText() == null || etSumaCont.getText().toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(),
                    R.string.invalid_suma_cont_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    private Contact buildContactFromWidgets() {
        String nume = etNume.getText().toString();
        String prenume = etPrenume.getText().toString();
        String telefon = etTelefon.getText().toString();
        Date dataNasterii = dateConverter.fromString(etDataNasterii.getText().toString().trim());
        String gen=getString(R.string.gen_feminin);
        if (rgGen.getCheckedRadioButtonId() == R.id.rb_adauga_gen_masculin) {
            gen = getString(R.string.gen_masculin);
        }
        float sumaCont=Float.parseFloat(etSumaCont.getText().toString());
        return new Contact(nume,prenume,telefon,dataNasterii,gen,sumaCont);
    }


    private View.OnClickListener openDialogToAddCredit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        };
    }

    private void openDialog(){
        CustomDialog dialog = new CustomDialog();
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog));
    }

    @Override
    public void sendCredit(String denumireCredit, float sumaImprumutata, float dobanda, int durataAni) {
        Credit credit=new Credit(denumireCredit,sumaImprumutata,dobanda,durataAni);
        credits.add(credit);
    }
}