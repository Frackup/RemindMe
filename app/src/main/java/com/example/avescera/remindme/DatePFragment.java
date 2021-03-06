package com.example.avescera.remindme;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by a.vescera on 28/11/2016.
 * DateFragment allows to display and catch the date agenda widget
 */

public class DatePFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    //private DatePicker dp;
    private OnDatePickedListener mCallback;
    private Integer mLayoutId;

    private int cYear;
    private int cMonth;
    private int cDay;

    // Interface definition
    public interface OnDatePickedListener {
        void onDatePicked(int textId, int year, int month, int day);
    }

    // make sure the Activity implement the OnDatePickedListener
    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnDatePickedListener)activity;
        }

        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + R.string.error_dp_message);
        }
    }*/
    // make sure the Activity implement the OnDatePickedListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnDatePickedListener)context;
        }

        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + R.string.error_dp_message);
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        initVariables();

        // This to grey all dates before today's date.
        //datePickerDialog.getDatePicker().setMinDate(curDate);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, cYear, cMonth, cDay);
    }

    private void initVariables() {

        View view = getActivity().getLayoutInflater().inflate(R.layout.datepfragment, null);

        final Calendar cal = Calendar.getInstance();
        mCallback = (OnDatePickedListener)getActivity();
        Bundle bundle = this.getArguments();
        mLayoutId = bundle.getInt("layoutId");
        cYear = cal.get(Calendar.YEAR);
        cMonth = cal.get(Calendar.MONTH);
        cDay = cal.get(Calendar.DAY_OF_MONTH);

        //dp = (DatePicker) view.findViewById(R.id.datePicker);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mCallback != null) {
            // Use the date chosen by the user.
            mCallback.onDatePicked(mLayoutId, year, monthOfYear, dayOfMonth);
        }
    }
}
