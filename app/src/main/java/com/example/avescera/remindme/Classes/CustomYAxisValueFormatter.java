package com.example.avescera.remindme.Classes;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Frackup on 12/12/2016.
 */

public class CustomYAxisValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public CustomYAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0"); // sets precision to 1
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }
}
