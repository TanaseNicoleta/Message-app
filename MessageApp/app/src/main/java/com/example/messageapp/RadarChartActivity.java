package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.messageapp.database.model.Contact;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class RadarChartActivity extends AppCompatActivity {
    public static final String SARBATORITI = "Sarbatoriti";

    private Intent intent;
    private List<Contact> contactList=new ArrayList<>();
    RadarChart radarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart);
        intent=getIntent();
        contactList=intent.getParcelableArrayListExtra(SARBATORITI);

        radarChart=findViewById(R.id.radar_chart);
        ArrayList<RadarEntry>radarEntries=new ArrayList<>();

        String[]labels = new String[contactList.size()];
        int i=0;
        for(Contact contact:contactList){
            radarEntries.add(new RadarEntry(contact.getSumaCont()));
            labels[i]=contact.getPrenume();
            i++;
        }
        RadarDataSet radarDataSet=new RadarDataSet(radarEntries, getString(R.string.radar_chart_title));
        radarDataSet.setColor(Color.RED);
        radarDataSet.setLineWidth(2f);
        radarDataSet.setValueTextColor(Color.RED);
        radarDataSet.setValueTextSize(14f);

        RadarData radarData=new RadarData();
        radarData.addDataSet(radarDataSet);

        XAxis xAxis=radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.setData(radarData);
    }
}