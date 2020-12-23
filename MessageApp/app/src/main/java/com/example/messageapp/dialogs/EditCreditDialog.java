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
import com.example.messageapp.database.model.Credit;


public class EditCreditDialog extends AppCompatDialogFragment {

    public static final String EDIT_CREDIT_KEY = "Edit credit key";
    private Credit credit;
    private Bundle bundle;
    private EditText etDenumireCredit;
    private EditText etSumaImprumutata;
    private EditText etDobanda;
    private EditText etDurataAni;
    EditCreditDialogListener listener;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_edit_credit_dialog,null);

        bundle=getArguments();
        credit= (Credit) bundle.getSerializable(EDIT_CREDIT_KEY);

        builder.setView(view)
                .setTitle(R.string.edit_credit)
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
                           listener.sendCredit(credit);
                        }
                    }
                });
        initComponents(view);
        buildViewsFromCredit(credit);
        return builder.create();
    }

    private void initComponents(View view){
        etDenumireCredit=view.findViewById(R.id.input_denumire_credit_edit_credit);
        etSumaImprumutata=view.findViewById(R.id.input_suma_imprumutata_edit_credit);
        etDobanda=view.findViewById(R.id.input_dobanda_edit_credit);
        etDurataAni=view.findViewById(R.id.input_durata_ani_edit_credit);
    }

    private void buildViewsFromCredit(Credit credit) {
        if (credit == null) {
            return;
        }
        if(credit.getDenumireCredit()!=null){
            etDenumireCredit.setText(credit.getDenumireCredit());
        }
        if(String.valueOf(credit.getSumaImprumutata())!=null){
            etSumaImprumutata.setText(String.valueOf(credit.getSumaImprumutata()));
        }
        if(String.valueOf(credit.getDobanda())!=null){
            etDobanda.setText(String.valueOf(credit.getDobanda()));
        }
        if(String.valueOf(credit.getDurataAni())!=null){
            etDurataAni.setText(String.valueOf(credit.getDurataAni()));
        }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener=(EditCreditDialogListener) context;

    }
    public interface EditCreditDialogListener {
        void sendCredit(Credit credit);
    }
    private void createFromViews(){
        String denumireCredit = etDenumireCredit.getText().toString();
        float sumaImprumutata = Float.parseFloat(etSumaImprumutata.getText().toString().trim());
        float dobanda=Float.parseFloat(etDobanda.getText().toString().trim());
        int durataAni=Integer.parseInt(etDurataAni.getText().toString().trim());
        credit.setDenumireCredit(denumireCredit);
        credit.setDurataAni(durataAni);
        credit.setDobanda(dobanda);
        credit.setSumaImprumutata(sumaImprumutata);
    }
}