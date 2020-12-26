package com.example.messageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.messageapp.R;
import com.example.messageapp.util.Bank;
import java.util.List;

public class BankAdapter extends ArrayAdapter<Bank> {
    private Context context;
    private int resource;
    private List<Bank> banks;
    private LayoutInflater inflater;

    public BankAdapter(@NonNull Context context, int resource, @NonNull List<Bank> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.banks = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Bank bank = banks.get(position);

        if(bank != null) {
            addBankName(view, bank.getDenumireBanca());
            addAdress(view, bank.getAdresa());
            addCommission(view, bank.getComision());
        }

        return view;
    }

    private void addBankName(View view, String bankName) {
        TextView textView = view.findViewById(R.id.tv_bank_name);
        populateTV(bankName, textView);
    }

    private void populateTV(String bankName, TextView textView) {
        if (bankName!=null && !bankName.isEmpty()) {
            textView.setText(bankName);
        } else {
            textView.setText(R.string.lv_bank_dafault);
        }
    }

    private void addAdress(View view, String bankAdress) {
        TextView textView = view.findViewById(R.id.tv_bank_adress);
        populateTV(bankAdress, textView);
    }

    private void addCommission(View view, float bankCommission) {
        TextView textView = view.findViewById(R.id.tv_bank_commission);
        if(bankCommission >= 0)
            populateTV(String.valueOf(bankCommission), textView);
        else textView.setText(R.string.lv_bank_dafault);
    }
}
