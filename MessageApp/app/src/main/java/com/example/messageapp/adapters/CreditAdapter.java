package com.example.messageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.messageapp.R;
import com.example.messageapp.database.model.Credit;

import java.util.List;

public class CreditAdapter  extends ArrayAdapter<Credit> {

    private Context context;
    private int resource;
    private List<Credit> credits;
    private LayoutInflater inflater;

    public CreditAdapter(@NonNull Context context, int resource,
                                 @NonNull List<Credit> objects,
                                 LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.credits=objects;
        this.inflater=inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(resource,parent,false);
        Credit credit=credits.get(position);
        if(credit!=null){
            adaugadenumireCredit(view,credit.getDenumireCredit());
            adaugaSumaImprumutata(view,String.valueOf(credit.getSumaImprumutata()));
            adaugaDobanda(view,String.valueOf(credit.getDobanda()));
            adaugaDurataAni(view,String.valueOf(credit.getDurataAni()));
        }
        return view;
    }
    private void adaugaDurataAni(View view, String durataAni){
        EditText editText=view.findViewById(R.id.lv_credit_durata_ani);
        editText.setEnabled(false);
        populareEditText(durataAni,editText);
    }
    private void adaugaDobanda(View view, String dobanda){
        EditText editText=view.findViewById(R.id.lv_credit_dobanda);
        editText.setEnabled(false);
        populareEditText(dobanda,editText);
    }
    private void adaugaSumaImprumutata(View view, String sumaImprumutata){
        EditText editText=view.findViewById(R.id.lv_credit_suma_imprumutata);
        editText.setEnabled(false);
        populareEditText(sumaImprumutata,editText);
    }
    private void adaugadenumireCredit(View view, String denumireCredit){
        EditText editText=view.findViewById(R.id.lv_credit_denumire_credit);
        editText.setEnabled(false);
        populareEditText(denumireCredit,editText);
    }
    private void populareEditText(String value, EditText editText) {
        if(value!=null && !value.isEmpty()){
            editText.setText(value);
        }else{
            editText.setText(R.string.no_content);
        }
    }
}
