package com.example.messageapp.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.messageapp.R;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.util.Bank;

import java.util.Map;

public class EditBankDialog extends AppCompatDialogFragment {
    public static final String BANK_PREF = "BankPref";
    public static final String UPDATED_BANKS = "Updated banks";
    private EditText etComision;
    private TextView tvAdresa, tvDenumire;
    Float oldCom = null;
    String denumire, adresa;
    private Bank bank;
    private SharedPreferences clickedPref;
    EditBankDialogListener listener;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_edit_bank_commission_dialog, null);
        initComponents(view);
        clickedPref = this.getActivity().getSharedPreferences(UPDATED_BANKS, Context.MODE_PRIVATE);

        Map<String, ?> entries = clickedPref.getAll();
        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            String dateBanca = entry.getValue().toString();
            String[] datas = dateBanca.split(",");
            denumire=datas[0].trim();
            adresa=datas[1].trim();
            oldCom = Float.parseFloat(datas[2]);
            bank = new Bank(datas[0], datas[1], Float.parseFloat(datas[2]));

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
                Float comisionNou = Float.parseFloat(etComision.getText().toString().trim());
                if(comisionNou > 0 && comisionNou < 1) {
                    bank.setComision(comisionNou);
                    listener.sendBank(bank);
                } else {
                    Toast.makeText(view.getContext(), R.string.err_comision, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }

    private void initComponents(View view) {
        etComision = view.findViewById(R.id.input_edit_commission);
        etComision.setInputType(InputType.TYPE_CLASS_NUMBER);
        tvDenumire = view.findViewById(R.id.tv_bank_name);
        tvAdresa = view.findViewById(R.id.tv_bank_adress);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (EditBankDialog.EditBankDialogListener) context;
    }

    public interface EditBankDialogListener {
        void sendBank(Bank bank);
    }
}

