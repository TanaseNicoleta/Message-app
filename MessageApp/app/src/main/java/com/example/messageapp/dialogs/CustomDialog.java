package com.example.messageapp.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.messageapp.R;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.util.DateConverter;

public class CustomDialog  extends AppCompatDialogFragment {
    private EditText etDenumireCredit;
    private EditText etSumaImprumutata;
    private EditText etDobanda;
    private EditText etDurataAni;
    private CustomDialogListener listener;
    private Credit credit;


    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle(R.string.credit)
                .setNegativeButton(getString(R.string.renunta), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(R.string.adauga, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(validate()){
                            credit=buildCreditFromWidgets();
                            listener.sendCredit(credit.getDenumireCredit(), credit.getSumaImprumutata(), credit.getDobanda(),
                                    credit.getDurataAni());
                        }
                    }
                });
        initComponents(view);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CustomDialogListener) context;

    }
    public interface CustomDialogListener {
        void sendCredit(String denumireCredit, float sumaImprumutata, float dobanda, int durataAni);
    }

    private void initComponents(View view){
        etDenumireCredit=view.findViewById(R.id.input_denumire_credit);
        etSumaImprumutata=view.findViewById(R.id.input_suma_imprumutata);
        etDobanda=view.findViewById(R.id.input_dobanda);
        etDurataAni=view.findViewById(R.id.input_durata_ani);
    }

    private boolean validate() {
        if (etDenumireCredit.getText() == null || etDenumireCredit.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_denumire_credit_error,
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (etSumaImprumutata.getText() == null || etSumaImprumutata.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_suma_field_error,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDobanda.getText() == null || etDobanda.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_dobanda_error,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDurataAni.getText() == null || etDurataAni.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext().getApplicationContext(),
                    R.string.invalid_durata_error,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Credit buildCreditFromWidgets() {
        String denumireCredit = etDenumireCredit.getText().toString();
        float sumaImprumutata = Float.parseFloat(etSumaImprumutata.getText().toString().trim());
        float dobanda=Float.parseFloat(etDobanda.getText().toString().trim());
        int durataAni=Integer.parseInt(etDurataAni.getText().toString().trim());
        return new Credit(denumireCredit,sumaImprumutata,dobanda,durataAni);
    }
}
