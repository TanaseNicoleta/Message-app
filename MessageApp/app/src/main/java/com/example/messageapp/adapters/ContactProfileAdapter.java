package com.example.messageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.messageapp.R;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.util.DateConverter;

import java.util.Date;
import java.util.List;

public class ContactProfileAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private List<Contact> contacts;
    private LayoutInflater inflater;


    public ContactProfileAdapter(@NonNull Context context, int resource,
                                 @NonNull List<Contact> objects,
                                 LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.contacts=objects;
        this.inflater=inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(resource,parent,false);
        Contact contact=contacts.get(position);
        if(contact!=null){
            adaugaNume(view,contact.getNume());
            adaugaPrenume(view,contact.getPrenume());
            adaugaTelefon(view,contact.getTelefon());
            adaugaDataNasterii(view,contact.getDataNasterii());
            adaugaSumaCont(view,contact.getSumaCont());
            adaugaGen(view, contact.getGen());
            setareAvatar(view,contact.getGen());
        }
        return view;
    }
    private void adaugaGen(View view, String gen){
        RadioGroup radioGroup=view.findViewById(R.id.lv_profile_rg_gen);
        RadioButton rb1=view.findViewById(R.id.lv_profile_rb_feminin);
        RadioButton rb2=view.findViewById(R.id.lv_profile_rb_masculin);
        rb1.setEnabled(false);
        rb2.setEnabled(false);
        if(gen.equalsIgnoreCase(context.getString(R.string.gen_feminin))){
            radioGroup.check(R.id.lv_profile_rb_feminin);
        }else if(gen.equalsIgnoreCase(context.getString(R.string.gen_masculin))){
            radioGroup.check(R.id.lv_profile_rb_masculin);
        }
    }
    private void setareAvatar(View view, String gen){
        ImageView avatar=view.findViewById(R.id.iv_avatar);
        if(gen.equalsIgnoreCase(context.getString(R.string.gen_feminin))){
            avatar.setImageResource(R.drawable.ic_woman_face);
        }else if(gen.equalsIgnoreCase(context.getString(R.string.gen_masculin))){
            avatar.setImageResource(R.drawable.ic_cara_ale);
        }
    }
    private void adaugaSumaCont(View view, float suma){
        TextView textView=view.findViewById(R.id.tv_suma_cont);
        populareTV(String.valueOf(suma),textView);
    }
    private void adaugaDataNasterii(View view, Date data_nasterii){
        TextView textView=view.findViewById(R.id.tv_data_nasterii);
        populareTV(DateConverter.fromDate(data_nasterii),textView);
    }
    private void adaugaTelefon(View view, String telefon){
        TextView textView=view.findViewById(R.id.tv_telefon);
        populareTV(telefon,textView);
    }
    private void adaugaPrenume(View view, String prenume){
        TextView textView=view.findViewById(R.id.tv_prenume_contact);
        populareTV(prenume,textView);
    }
    private void adaugaNume(View view, String nume){
        TextView textView=view.findViewById(R.id.tv_nume_contact);
        populareTV(nume,textView);
    }
    private void populareTV(String value, TextView textView) {
        if(value!=null && !value.isEmpty()){
            textView.setText(value);
        }else{
            textView.setText(R.string.no_content);
        }
    }
}
