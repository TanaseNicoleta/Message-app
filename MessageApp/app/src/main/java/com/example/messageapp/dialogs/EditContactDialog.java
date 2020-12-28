package com.example.messageapp.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.messageapp.R;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.util.DateConverter;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;

public class EditContactDialog extends AppCompatDialogFragment {
    private final DateConverter dateConverter = new DateConverter();
    public static final String EDIT_CONTACT_KEY = "Edit contact key";
    private Contact contact;

    private EditText etNume;
    private EditText etPrenume;
    private EditText etTelefon;
    private EditText etDataNasterii;
    private RadioGroup rgGen;
    private EditText etSumaCont;
    private Bundle bundle;

    EditContactDialogListener listener;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_edit_contact_dialog,null);

        bundle=getArguments();
        if(bundle!=null){
            contact= bundle.getParcelable(EDIT_CONTACT_KEY);
        }

        builder.setView(view)
                .setTitle(R.string.contact_edit)
                .setNegativeButton(getString(R.string.renunta), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(R.string.salveaza, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(validate()){

                            createFromViews();
                            listener.sendContact(contact);
                        }
                    }
                });
        initComponents(view);
        buildViewsFromContact(contact);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (EditContactDialogListener) context;
    }
    public interface EditContactDialogListener {
        void sendContact(Contact contact);
    }

    private void initComponents(View view){
        etNume=view.findViewById(R.id.input_nume_edit_contact_dialog);
        etPrenume=view.findViewById(R.id.input_prenume_edit_contact_dialog);
        etTelefon=view.findViewById(R.id.input_telefon_edit_contact_dialog);
        etDataNasterii=view.findViewById(R.id.input_data_nasterii_edit_contact_dialog);
        rgGen=view.findViewById(R.id.rg_adauga_gen_edit_contact_dialog);
        etSumaCont=view.findViewById(R.id.input_suma_cont_edit_contact_dialog);
    }

    private void buildViewsFromContact(Contact contact) {
        if (contact == null) {
            return;
        }
        if(contact.getNume()!=null){
            etNume.setText(contact.getNume());
        }
        if(contact.getPrenume()!=null){
            etPrenume.setText(contact.getPrenume());
        }
        if(contact.getTelefon()!=null){
            etTelefon.setText(contact.getTelefon());
        }
        if (contact.getDataNasterii() != null) {
            etDataNasterii.setText(DateConverter.fromDate(contact.getDataNasterii()));
        }
        if(contact.getGen().equalsIgnoreCase(getString(R.string.gen_feminin))){
            rgGen.check(R.id.rb_adauga_gen_feminin_edit_contact_dialog);
        }else{
            rgGen.check(R.id.rb_adauga_gen_masculin_edit_contact_dialog);
        }
        etSumaCont.setText(String.valueOf(contact.getSumaCont()));
    }

    private boolean validate() {
        if (etNume.getText() == null || etNume.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_nume_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etPrenume.getText() == null || etPrenume.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_prenume_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etTelefon.getText() == null || etTelefon.getText().toString().trim().length() == 0){
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_telefon_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etDataNasterii.getText() == null
                || dateConverter.fromString(etDataNasterii.getText().toString().trim()) == null) {
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_data_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (rgGen.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getContext().getApplicationContext(),
                    getString(R.string.invalid_gen_error),
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etSumaCont.getText() == null || etSumaCont.getText().toString().trim().length() == 0){
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_suma_cont_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    private void createFromViews() {
        String nume = etNume.getText().toString();
        String prenume = etPrenume.getText().toString();
        String telefon = etTelefon.getText().toString();
        Date dataNasterii = dateConverter.fromString(etDataNasterii.getText().toString().trim());
        String gen=getString(R.string.gen_feminin);
        if (rgGen.getCheckedRadioButtonId() == R.id.rb_adauga_gen_masculin_edit_contact_dialog) {
            gen = getString(R.string.gen_masculin);
        }
        float sumaCont=Float.parseFloat(etSumaCont.getText().toString());
        contact.setNume(nume);
        contact.setPrenume(prenume);
        contact.setTelefon(telefon);
        contact.setDataNasterii(dataNasterii);
        contact.setGen(gen);
        contact.setSumaCont(sumaCont);
    }
}
