package com.example.avescera.remindme.Classes;

import android.content.Context;

import com.example.avescera.remindme.R;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by a.vescera on 14/12/2016.
 * This class is built to format the X axis of the graph to display month of the year.
 */

public class MonthAxisValueFormatter implements IAxisValueFormatter {

    private final String[] mMonths;

    public MonthAxisValueFormatter(Context context) {

        mMonths = new String[]{
                context.getResources().getString(R.string.january),
                context.getResources().getString(R.string.february),
                context.getResources().getString(R.string.march),
                context.getResources().getString(R.string.april),
                context.getResources().getString(R.string.may),
                context.getResources().getString(R.string.june),
                context.getResources().getString(R.string.july),
                context.getResources().getString(R.string.august),
                context.getResources().getString(R.string.september),
                context.getResources().getString(R.string.october),
                context.getResources().getString(R.string.november),
                context.getResources().getString(R.string.december),
                context.getResources().getString(R.string.january),
                context.getResources().getString(R.string.february),
                context.getResources().getString(R.string.march),
                context.getResources().getString(R.string.april),
                context.getResources().getString(R.string.may),
                context.getResources().getString(R.string.june),
                context.getResources().getString(R.string.july),
                context.getResources().getString(R.string.august),
                context.getResources().getString(R.string.september),
                context.getResources().getString(R.string.october),
                context.getResources().getString(R.string.november),
                context.getResources().getString(R.string.december)
        };
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int month = Math.round(value);

        return mMonths[(month % mMonths.length) - 1];
    }
}
