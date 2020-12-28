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
import com.example.messageapp.util.Mesaj;

import java.util.List;

public class MesajAdapter extends ArrayAdapter<Mesaj> {

    private Context context;
    private int resource;
    private List<Mesaj> mesaje;
    private LayoutInflater inflater;

    public MesajAdapter(@NonNull Context context, int resource,
                         @NonNull List<Mesaj> objects,
                         LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.mesaje=objects;
        this.inflater=inflater;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(resource,parent,false);
        Mesaj mesaj=mesaje.get(position);
        if(mesaj!=null){
            adaugaContinut(view,mesaj.getContinut());
        }
        return view;
    }
    private void adaugaContinut(View view, String continut){
        TextView textView=view.findViewById(R.id.lv_mesaj_tv_continut);
        populareTV(continut,textView);
    }
    private void populareTV(String value, TextView textView) {
        if(value!=null && !value.isEmpty()){
            textView.setText(value);
        }else{
            textView.setText(R.string.no_content);
        }
    }
}
