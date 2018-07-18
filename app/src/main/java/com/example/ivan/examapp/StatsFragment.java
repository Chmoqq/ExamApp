package com.example.ivan.examapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class StatsFragment extends Fragment {

    LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.from(getContext()).inflate(R.layout.stats_fragment, container, false);
        lineChart = root.findViewById(R.id.linechart);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 70f));
        values.add(new Entry(1, 54f));
        values.add(new Entry(2, 45f));
        values.add(new Entry(3, 30f));
        values.add(new Entry(5, 20f));
        values.add(new Entry(6, 10f));
        values.add(new Entry(7, 23f));
        values.add(new Entry(8, 90f));


        LineDataSet lineDataSet = new LineDataSet(values, "");
        IValueFormatter valueFormatter = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf(Math.round(value));
            }
        };
        lineDataSet.setValueFormatter(valueFormatter);
        lineDataSet.setLineWidth(5f);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setCircleRadius(7f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleHoleRadius(4f);
        lineDataSet.setCircleColorHole(getResources().getColor(R.color.white));
        lineDataSet.setCircleColors(getResources().getColor(R.color.blue_stats));
        //lineDataSet.setValueTypeface();
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(getResources().getColor(R.color.specblue_stats));

        lineChart.getXAxis().setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextColor(getResources().getColor(R.color.white));
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDescription(null);
        lineChart.setDrawBorders(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        return root;
    }
}
