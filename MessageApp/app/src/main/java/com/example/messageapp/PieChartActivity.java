package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    public static final String PESTE_6000 = "peste 6000";
    public static final String SUB_3000 = "sub 3000";
    public static final String INTRE_3000_SI_6000 = "intre 3000 si 6000";


    List<Contact>contactePeste6000=new ArrayList<>();
    List<Contact>contactePanaIn3000=new ArrayList<>();
    List<Contact>contacteIntre3000si6000=new ArrayList<>();
    PieChart pieChart;
    Map<String,Float>mapa=new HashMap<>();
    private Intent intent;

    ArrayList<PieEntry> pieEntries=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        intent=getIntent();
        contactePeste6000=intent.getParcelableArrayListExtra(PESTE_6000);
        contactePanaIn3000=intent.getParcelableArrayListExtra(SUB_3000);
        contacteIntre3000si6000=intent.getParcelableArrayListExtra(INTRE_3000_SI_6000);
        initComponents();
        DrawChart();
    }

    private void initComponents() {
        pieChart=findViewById(R.id.pie_chart);
    }

    private void DrawChart() {
        pieEntries=new ArrayList<>();
        pieEntries.add(new PieEntry(Float.valueOf(contactePeste6000.size()), getString(R.string.label_peste_6000)));
        pieEntries.add(new PieEntry(Float.valueOf(contactePanaIn3000.size()), getString(R.string.label_sub)));
        pieEntries.add(new PieEntry(Float.valueOf(contacteIntre3000si6000.size()), getString(R.string.label_between)));

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
}