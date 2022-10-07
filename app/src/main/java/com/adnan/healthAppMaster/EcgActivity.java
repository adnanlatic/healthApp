package com.adnan.healthAppMaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EcgActivity extends AppCompatActivity {

    private FloatingActionButton backToHome;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private LineChart lineChart;
    private TextView izmjerenEKG;
    List<Entry> lineEntries = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        backToHome = findViewById(R.id.idBackToHome);
        // DB
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        izmjerenEKG = findViewById(R.id.idVrijednostMjerenjaEKG);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity for adding a course.
                Intent i = new Intent(EcgActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        getDataFromDatabase();
    }

    private void getDataFromDatabase() {
        databaseReference = firebaseDatabase.getReference("sensors/ecg");
        ArrayList<Object> ecgSenzor = new ArrayList<>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sensor data = snapshot.getValue(Sensor.class);
                ecgSenzor.add(data.getValue());
                izmjerenEKG.setText(ecgSenzor.get(ecgSenzor.size()-1)+ "bpm");
                lineEntries.add(new Entry(Float.parseFloat(data.getPosition()), Float.parseFloat(data.getValue())));
                drawLineChart();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sensor data = snapshot.getValue(Sensor.class);
                ecgSenzor.add(data.getValue());
                izmjerenEKG.setText(ecgSenzor.get(ecgSenzor.size()-1)+ "bpm");
                lineEntries.add(new Entry(Float.parseFloat(data.getPosition()), Float.parseFloat(data.getValue())));
                drawLineChart();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void drawLineChart() {
        LineChart lineChart = findViewById(R.id.lineChart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        List<Entry> lineEntries = getDataSet();
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "EKG");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.rgb(36, 211, 226));
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setHighLightColor(Color.BLUE);
        lineDataSet.setFillAlpha(50);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.rgb(36, 211, 226));
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.DKGRAY);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);

        LineData lineData = new LineData(lineDataSet);
        lineChart.getDescription().setTextSize(12);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawMarkers(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.setData(lineData);
        lineDataSet.setDrawValues(false);

        ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Vrijeme");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel) {
            @Override
            public String[] getValues() {
                return super.getValues();
            }
        });

        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate();

    }

    private List<Entry> getDataSet() {
        return lineEntries;
    }
}