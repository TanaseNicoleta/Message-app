package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.service.ContactService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartActivity extends AppCompatActivity {

    ContactService contactService;
    List<Contact>contactePeste6000=new ArrayList<>();
    List<Contact>contactePanaIn3000=new ArrayList<>();
    List<Contact>contacteIntre3000si6000=new ArrayList<>();
    PieChart pieChart;
    Map<String,Float>mapa=new HashMap<>();
    private float nrCreditePeste6000;
    private float nrCreditePanaIn3000;
    private float nrCrediteIntre3000si6000;

    ArrayList<PieEntry> pieEntries=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        contactService=new ContactService(getApplicationContext());
        contactService.getContacteImprumutateCuPeste6000lei(getContacteImprumutateCuPeste6000FromDbCallback());
        contactService.getContacteImprumutateMaiPutinDe3000lei(getContacteImprumutateCuMaiPutin3000FromDbCallback());
        contactService.getContacteImprumutateCuSumaTotalaIntre3000si6000(getContacteImprumutateCuSumaTotalaIntre3000si6000FromDbCallback());
        initComponents();
        DrawChart();
    }

    private void initComponents() {
        pieChart=findViewById(R.id.pie_chart);
        nrCreditePeste6000=nrContactePeste6000();
        nrCreditePanaIn3000=nrContactePanaIn3000();
        nrCrediteIntre3000si6000=nrContacteIntre3000si6000();
    }

    private float nrContactePeste6000() {
        float nr=0;
        for(Contact contact:contactePeste6000){
            nr++;
        }
        return  nr;
    }
    private float nrContactePanaIn3000() {
        float nr=0;
        for(Contact contact:contactePanaIn3000){
            nr++;
        }
        return  nr;
    }
    private float nrContacteIntre3000si6000() {
        float nr=0;
        for(Contact contact:contacteIntre3000si6000){
            nr++;
        }
        return  nr;
    }

    private void DrawChart() {
        pieEntries=new ArrayList<>();

//        pieEntries.add(new PieEntry(nrContactePeste6000(), getString(R.string.label_peste_6000)));
//        pieEntries.add(new PieEntry(nrContactePanaIn3000(), getString(R.string.label_sub)));
//        pieEntries.add(new PieEntry(nrContacteIntre3000si6000(), getString(R.string.label_between)));

//        pieEntries.add(new PieEntry(nrCreditePeste6000*0.1f, getString(R.string.label_peste_6000)));
//        pieEntries.add(new PieEntry(nrCreditePanaIn3000*0.1f, getString(R.string.label_sub)));
//        pieEntries.add(new PieEntry(nrCrediteIntre3000si6000*0.1f, getString(R.string.label_between)));

        pieEntries.add(new PieEntry(22.4f, getString(R.string.label_peste_6000)));
        pieEntries.add(new PieEntry(23, getString(R.string.label_sub)));
        pieEntries.add(new PieEntry(45, getString(R.string.label_between)));

        Log.i("PIE ENTRIES", pieEntries.toString());

        PieDataSet pieDataSet=new PieDataSet(pieEntries, getString(R.string.title_pie));
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setSliceSpace(0f);

        PieData pieData=new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(getString(R.string.title_pie));
        pieChart.animate();
    }

    private Callback<List<Contact>> getContacteImprumutateCuPeste6000FromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contactePeste6000.addAll(result);
                    Log.i("Peste 6000", contactePeste6000.toString());
                    Log.i("SIZE", String.valueOf(contactePeste6000.size()));
                }
            }
        };
    }
    private Callback<List<Contact>> getContacteImprumutateCuMaiPutin3000FromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contactePanaIn3000.addAll(result);
                    Log.i("Sub 3000", contactePanaIn3000.toString());
                }
            }
        };
    }
    private Callback<List<Contact>> getContacteImprumutateCuSumaTotalaIntre3000si6000FromDbCallback() {
        return new Callback<List<Contact>>() {
            @Override
            public void runResultOnUiThread(List<Contact> result) {
                if (result != null) {
                    contacteIntre3000si6000.addAll(result);
                    Log.i("Intre 3000 si 6000", contacteIntre3000si6000.toString());
                }
            }
        };
    }
}