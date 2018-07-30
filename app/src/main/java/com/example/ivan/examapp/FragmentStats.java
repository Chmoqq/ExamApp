package com.example.ivan.examapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivan.examapp.DataBase.DataBase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class FragmentStats extends Fragment {

    LineChart lineChart;
    PieChart pieChart;


    private DataBase dataBase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.from(getContext()).inflate(R.layout.stats_fragment, container, false);
        dataBase = new DataBase(getActivity());
        dataBase.open();

        lineChart = root.findViewById(R.id.linechart);
        pieChart = root.findViewById(R.id.piechart);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 5, 10, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(54f);
        pieChart.setHoleRadius(37f);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.setDrawEntryLabels(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(17f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(12f);
        l.setXOffset(15);


        pieChart.setData(pieDataGetter());


        lineChart.setData(lineDataGetter());
        return root;
    }

    private PieData pieDataGetter() {
        int[] stats = dataBase.getStats(MainActivity.getCurSubjectId(), 1);
        ArrayList<PieEntry> pieValues = new ArrayList<>();
        pieValues.add(new PieEntry(stats[0], "Ошибки"));
        pieValues.add(new PieEntry(stats[1], "Верные ответы"));

        PieDataSet pieDataSet = new PieDataSet(pieValues, "");
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setSelectionShift(9f);
        int[] colors = new int[]{getResources().getColor(R.color.pink), getResources().getColor(R.color.specblue_stats)};
        pieDataSet.setColors(colors);
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.roboto_bold);
        pieDataSet.setValueTypeface(font);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setDrawValues(false);
        return pieData;
    }

    private LineData lineDataGetter() {
        int[] data = dataBase.getStats(MainActivity.getCurSubjectId(), 2);
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0, data[0]));
        values.add(new Entry(1, data[1]));
        values.add(new Entry(2, data[2]));

        LineDataSet lineDataSet = new LineDataSet(values, "Ошибки за месяц");
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
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(getResources().getColor(R.color.specblue_stats));

        lineChart.getXAxis().setEnabled(false);
        lineChart.getLegend().setTextColor(getResources().getColor(R.color.white));
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.roboto_bold);
        lineChart.getLegend().setTypeface(font);
        lineChart.getLegend().setTextSize(11.5f);
        lineChart.animateX(1000, Easing.EasingOption.EaseInOutCubic);
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
        return lineData;
    }
}
