package com.example.messageapp.util;

import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class JsonParser {

    public static final String DENUMIRE_BANCA = "denumireBanca";
    public static final String ADRESA = "adresa";
    public static final String COMISION = "comision";
    public static final String CLIENT = "Client";
    public static final String NUME = "nume";
    public static final String PRENUME = "prenume";
    public static final String TELEFON = "telefon";
    public static final String DATA_NASTERII = "dataNasterii";
    public static final String SUMA_CONT = "sumaCont";
    public static final String CREDIT = "Credit";
    public static final String DENUMIRE_CREDIT = "denumireCredit";
    public static final String SUMA_IMPRUMUTATA = "sumaImprumutata";
    public static final String DOBANDA = "dobanda";
    public static final String DURATA_ANI = "durataAni";
    public static final String GEN = "gen";

    public static void fromJson(String buffer, List<Contact>contacts, List<Bank>bankList, List<Credit>credits){
        try {
            JSONArray vectorRezultate=new JSONArray(buffer);
            for(int i=0;i<vectorRezultate.length();i++){

                JSONObject jsonObjectBank=vectorRezultate.getJSONObject(i);
                String denumireBanca=jsonObjectBank.getString(DENUMIRE_BANCA);
                String adresa=jsonObjectBank.getString(ADRESA);
                float comision=Float.parseFloat(jsonObjectBank.getString(COMISION));
                Bank bank=new Bank(denumireBanca,adresa,comision);
                bankList.add(bank);

                JSONObject jsonObjectClient=jsonObjectBank.getJSONObject(CLIENT);
                String nume=jsonObjectClient.getString(NUME);
                String prenume=jsonObjectClient.getString(PRENUME);
                String telefon=jsonObjectClient.getString(TELEFON);
                String dataNasteriiString=jsonObjectClient.getString(DATA_NASTERII);
                Date dataNasterii= DateConverter.fromString(dataNasteriiString);
                String gen=jsonObjectClient.getString(GEN);
                float sumaCont=Float.parseFloat(jsonObjectClient.getString(SUMA_CONT));

                Contact contact=new Contact(nume,prenume,telefon,dataNasterii,gen,sumaCont);
                contacts.add(contact);

                JSONObject jsonObjectCredit=jsonObjectClient.getJSONObject(CREDIT);
                String denumireCredit=jsonObjectCredit.getString(DENUMIRE_CREDIT);
                float sumaImprumutata=Float.parseFloat(jsonObjectCredit.getString(SUMA_IMPRUMUTATA));
                float dobanda=Float.parseFloat(jsonObjectCredit.getString(DOBANDA));
                int durataAni=Integer.parseInt(jsonObjectCredit.getString(DURATA_ANI));

                Credit credit=new Credit(denumireCredit,sumaImprumutata,dobanda,durataAni);
                credits.add(credit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
