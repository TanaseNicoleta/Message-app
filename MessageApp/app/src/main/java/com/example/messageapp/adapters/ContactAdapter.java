package com.example.messageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.messageapp.R;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.util.DateConverter;

import java.util.Date;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private int resource;
    private List<Contact> contacts;
    private LayoutInflater inflater;

    public ContactAdapter(@NonNull Context context, int resource,
                          @NonNull List<Contact> objects, LayoutInflater inflater) {
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
            setareAvatar(view,contact.getGen());
        }
        return view;
    }
    private void setareAvatar(View view, String gen){
        ImageView avatar=view.findViewById(R.id.civ_atavar);
        if(gen.equalsIgnoreCase(context.getString(R.string.gen_feminin))){
            avatar.setImageResource(R.drawable.ic_woman_face);
        }else if(gen.equalsIgnoreCase(context.getString(R.string.gen_masculin))){
            avatar.setImageResource(R.drawable.ic_cara_ale);
        }
    }
    private void adaugaTelefon(View view, String telefon){
        TextView textView=view.findViewById(R.id.lv_contact_telefon);
        populareTV(telefon,textView);
    }
    private void adaugaPrenume(View view, String prenume){
        TextView textView=view.findViewById(R.id.lv_contact_prenume);
        populareTV(prenume,textView);
    }
    private void adaugaNume(View view, String nume){
        TextView textView=view.findViewById(R.id.lv_contact_nume);
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
