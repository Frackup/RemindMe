package com.example.avescera.remindme.Classes;

import android.content.Context;

import com.example.avescera.remindme.R;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by a.vescera on 14/12/2016.
 */

public class MonthAxisValueFormatter implements IAxisValueFormatter {

    protected String[] mMonths;
    private Context context;

    private BarLineChartBase<?> chart;

    public MonthAxisValueFormatter(BarLineChartBase<?> chart, Context context) {
        this.chart = chart;
        this.context = context;

        mMonths = new String[]{
                this.context.getResources().getString(R.string.january),
                this.context.getResources().getString(R.string.february),
                this.context.getResources().getString(R.string.march),
                this.context.getResources().getString(R.string.april),
                this.context.getResources().getString(R.string.may),
                this.context.getResources().getString(R.string.june),
                this.context.getResources().getString(R.string.july),
                this.context.getResources().getString(R.string.august),
                this.context.getResources().getString(R.string.september),
                this.context.getResources().getString(R.string.october),
                this.context.getResources().getString(R.string.november),
                this.context.getResources().getString(R.string.december)
        };
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int month = (int) value;

        String monthName = mMonths[month % mMonths.length];

        return monthName;
    }
}
