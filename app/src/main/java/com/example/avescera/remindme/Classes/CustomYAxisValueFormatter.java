package com.example.avescera.remindme.Classes;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by Frackup on 12/12/2016.
 */

public class CustomYAxisValueFormatter implements YAxisValueFormatter {

    private DecimalFormat mFormat;

    public CustomYAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0"); // sets precision to 1
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return mFormat.format(value);
    }
}
