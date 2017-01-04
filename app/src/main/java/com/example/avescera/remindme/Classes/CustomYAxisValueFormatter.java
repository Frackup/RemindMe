package com.example.avescera.remindme.Classes;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Frackup on 12/12/2016.
 * This custom axis class is built to make values appearing on graph with decimals up to 2.
 */

public class CustomYAxisValueFormatter implements IValueFormatter {

    private final DecimalFormat mFormat;

    public CustomYAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.00"); // sets precision to 2
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }
}
