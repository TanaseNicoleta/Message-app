package com.example.messageapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.messageapp.BankActivity;
import com.example.messageapp.R;

import java.util.Map;

public class EditBankDialog extends AppCompatDialogFragment {
    public static final String BANK_PREF = "BankPref";
    public static final String UPDATED_BANKS = "Updated banks";
    public static final String COMISION = "Comision";
    private EditText etComision;
    private TextView tvAdresa, tvDenumire;

    private SharedPreferences preferences, clickedPref;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edit_bank_commission_dialog, null);

        initComponents(view);
        preferences = this.getActivity().getSharedPreferences(BANK_PREF, Context.MODE_PRIVATE);
        clickedPref = this.getActivity().getSharedPreferences(UPDATED_BANKS, Context.MODE_PRIVATE);

        Map<String, ?> entries = clickedPref.getAll();
        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            String dateBanca = entry.getValue().toString();
            String[] datas = dateBanca.split(",");

           tvDenumire.setText(datas[0]);
           tvAdresa.setText(datas[1]);
           etComision.setText(datas[2]);

        }


        builder.setView(view).setTitle(R.string.dialog_edit_commission)
                .setNegativeButton(R.string.renunta, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
        .setPositiveButton(R.string.salveaza, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                float comision = Float.parseFloat(etComision.getText().toString());
                modificaComision(comision);
                getActivity().finish();
                Intent intent = new Intent(getContext(), BankActivity.class);
                startActivity(intent);
            }
        });

        return builder.create();
    }

    private void initComponents(View view) {
        etComision = view.findViewById(R.id.input_edit_commission);
        tvDenumire = view.findViewById(R.id.tv_bank_name);
        tvAdresa = view.findViewById(R.id.tv_bank_adress);
    }

    private void modificaComision(Float com) {
        preferences = this.getActivity().getSharedPreferences(BANK_PREF, Context.MODE_PRIVATE);
        clickedPref = this.getActivity().getSharedPreferences(UPDATED_BANKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Map<String, ?> allEntries = clickedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String dateBanca = entry.getValue().toString();
            String[] datas = dateBanca.split(",");
            datas[2] = com.toString();

            StringBuilder builder = new StringBuilder();
            for(String s : datas) {
                builder.append(s);
                builder.append(",");
            }
            String s = builder.toString();

            editor.putString(entry.getKey(), s);
            editor.commit();

        }


    }


}
