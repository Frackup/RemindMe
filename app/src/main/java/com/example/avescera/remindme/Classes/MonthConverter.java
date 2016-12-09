package com.example.avescera.remindme.Classes;

import android.content.Context;

import com.example.avescera.remindme.R;

/**
 * Created by a.vescera on 09/12/2016.
 */

public class MonthConverter {
    private Context context;

    public MonthConverter(Context context){
        this.context = context;
    }

    public String convert(int date){
        switch (date) {
            case 1:
                return context.getResources().getString(R.string.january);
            case 2:
                return context.getResources().getString(R.string.february);
            case 3:
                return context.getResources().getString(R.string.march);
            case 4:
                return context.getResources().getString(R.string.april);
            case 5:
                return context.getResources().getString(R.string.may);
            case 6:
                return context.getResources().getString(R.string.june);
            case 7:
                return context.getResources().getString(R.string.july);
            case 8:
                return context.getResources().getString(R.string.august);
            case 9:
                return context.getResources().getString(R.string.september);
            case 10:
                return context.getResources().getString(R.string.october);
            case 11:
                return context.getResources().getString(R.string.november);
            case 12:
                return context.getResources().getString(R.string.december);
            default:
                return context.getResources().getString(R.string.january);
        }
    }
}
